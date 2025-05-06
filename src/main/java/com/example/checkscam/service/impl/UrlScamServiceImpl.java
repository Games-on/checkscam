package com.example.checkscam.service.impl;

import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.dto.UrlScamDto;
import com.example.checkscam.dto.UrlScamImportRow;
import com.example.checkscam.entity.*;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.UrlScamRepository;
import com.example.checkscam.repository.UrlScamStatsRepository;
import com.example.checkscam.service.ScamStatsService;
import com.example.checkscam.service.UrlScamService;
import com.example.checkscam.util.DataUtil;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.checkscam.entity.Report;
import jakarta.transaction.Transactional;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UrlScamServiceImpl implements UrlScamService {
    private final UrlScamRepository repository;
    private final ScamStatsService scamStatsService;
    private final UrlScamStatsRepository urlScamStatsRepository;
    private final EntityManager em;

    @Override
    public void handleAfterReport(Report report, Long idScamType) throws CheckScamException {
        UrlScam urlScam = repository.findByInfo(report.getInfo());
        if (urlScam == null) {
            urlScam = new UrlScam();
            urlScam.setInfo(report.getInfo());
            urlScam.setDescription(report.getInfoDescription());
            urlScam.setStats(new UrlScamStats());
        }
        urlScam.setStats(
                new UrlScamStats(
                        scamStatsService.handleStats(
                                new ScamStatsDto(
                                        urlScam.getStats()), report, idScamType)));
        repository.save(urlScam);

        UrlScamDto.builder()
                .id(urlScam.getId())
                .info(urlScam.getInfo())
                .description(urlScam.getDescription())
                .build();

    }

    private static final int EXCEL_COL_URL = 0;

    @Transactional
    @Override
    public void importUrlData(MultipartFile file) throws IOException {
        String originalFilename = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();
        log.info("Bắt đầu quá trình import dữ liệu URL lừa đảo từ file: {}", originalFilename);

        List<UrlScamImportRow> rows;
        if (originalFilename.endsWith(".csv")) {
            rows = parseUrlCsv(file);
        } else if (originalFilename.endsWith(".xlsx")) {
            rows = parseUrlExcel(file);
        } else {
            log.error("Định dạng file không được hỗ trợ: {}. Chỉ hỗ trợ .csv và .xlsx.", originalFilename);
            throw new IllegalArgumentException("Chỉ hỗ trợ file định dạng CSV hoặc XLSX. File nhận được: " + originalFilename);
        }

        // Sử dụng Set để lưu các domain đã chuẩn hóa và duy nhất từ file
        Set<String> uniqueNormalizedDomains = new HashSet<>();
        int skippedRowsDueToEmptyOrInvalid = 0; // Đếm số dòng bị bỏ qua do URL gốc rỗng/không hợp lệ hoặc sau chuẩn hóa bị rỗng

        log.info("Đang đọc và chuẩn hóa URL từ file...");
        for (UrlScamImportRow r : rows) {
            String originalUrlInfo = StringUtils.hasText(r.getDomainOrUrl()) ? r.getDomainOrUrl().trim() : null;

            if (!StringUtils.hasText(originalUrlInfo)) {
                // log.warn("Bỏ qua dòng do thiếu thông tin URL/domain (dữ liệu gốc: {}).", r.getDomainOrUrl());
                skippedRowsDueToEmptyOrInvalid++;
                continue;
            }

            // Chuẩn hóa URL/domain bằng phương thức từ DataUtil
            // Thay thế "com.example.checkscam.util.DataUtil" bằng đường dẫn thực tế đến lớp DataUtil của bạn
            String normalizedDomainInfo = com.example.checkscam.util.DataUtil.extractFullDomain(originalUrlInfo);

            if (!StringUtils.hasText(normalizedDomainInfo)) {
                // log.warn("Bỏ qua dòng do URL/domain sau chuẩn hóa là rỗng hoặc không hợp lệ (dữ liệu gốc: {}).", originalUrlInfo);
                skippedRowsDueToEmptyOrInvalid++;
                continue;
            }
            uniqueNormalizedDomains.add(normalizedDomainInfo);
        }
        log.info("Đã đọc xong file. Số URL/domain duy nhất (sau chuẩn hóa) cần xử lý: {}. Số dòng bị bỏ qua do URL rỗng/không hợp lệ: {}",
                uniqueNormalizedDomains.size(), skippedRowsDueToEmptyOrInvalid);


        List<UrlScam> batch = new ArrayList<>(500);
        int processedForDbInsertion = 0; // Số bản ghi thực sự được thêm vào batch để lưu
        int duplicatesFoundInDb = 0;     // Số bản ghi đã tồn tại trong DB

        log.info("Đang xử lý và lưu các URL/domain duy nhất vào cơ sở dữ liệu...");
        for (String domainToProcess : uniqueNormalizedDomains) {
            // Kiểm tra trùng lặp trong DB cho từng domain duy nhất
            UrlScam existingScam = repository.findByInfo(domainToProcess);

            if (existingScam != null) {
                // log.warn("Domain đã tồn tại trong DB (sau chuẩn hóa: '{}'), bỏ qua.", domainToProcess);
                duplicatesFoundInDb++;
                continue; // Chuyển sang domain tiếp theo trong Set
            }

            // Tạo mới UrlScam với domain đã chuẩn hóa
            UrlScam newScam = new UrlScam();
            newScam.setInfo(domainToProcess);
            // Bạn có thể lưu URL gốc vào trường description nếu muốn, nhưng cần cách lấy lại URL gốc
            // nếu chỉ có domain đã chuẩn hóa. Điều này có thể phức tạp.
            // Hoặc chỉ lưu domain đã chuẩn hóa.

            UrlScamStats stats = new UrlScamStats();
            stats.setUrlScam(newScam); // Quan trọng: thiết lập mối quan hệ
            stats.setVerifiedCount(1);
            stats.setReasonsJson(null);
            stats.setLastReportAt(null);

            newScam.setStats(stats); // CascadeType.ALL sẽ xử lý việc lưu stats

            batch.add(newScam);
            processedForDbInsertion++;

            if (batch.size() >= 500) {
                log.info("Đang lưu {} bản ghi URL lừa đảo vào DB...", batch.size());
                repository.saveAll(batch);
                em.flush();
                em.clear();
                batch.clear();
                log.info("Lưu và xóa batch thành công.");
            }
        }

        if (!batch.isEmpty()) {
            log.info("Đang lưu {} bản ghi URL lừa đảo cuối cùng vào DB...", batch.size());
            repository.saveAll(batch);
            em.flush();
            em.clear();
            log.info("Lưu và xóa batch cuối cùng thành công.");
        }

        log.info("Hoàn tất import dữ liệu URL lừa đảo. Số bản ghi mới được thêm vào DB: {}. Số URL/domain duy nhất đã tồn tại trong DB: {}. Số dòng bị bỏ qua (URL rỗng/không hợp lệ từ file): {}",
                processedForDbInsertion, duplicatesFoundInDb, skippedRowsDueToEmptyOrInvalid);
    }
    private List<UrlScamImportRow> parseUrlCsv(MultipartFile file) throws IOException {
        log.debug("Đang phân tích file CSV cho URL: {}", file.getOriginalFilename());
        List<UrlScamImportRow> list = new ArrayList<>();

        // 1. Mở stream hai lần: 1 lần dò delimiter, 1 lần parse thật
        try (InputStream detectStream = file.getInputStream()) {

            // Đọc tối đa 3 dòng đầu tiên để đoán dấu phân tách
            char delimiter = detectCsvDelimiter(detectStream);
            log.info("Đã nhận diện delimiter '{}' cho file {}", delimiter, file.getOriginalFilename());

            // 2. Parse chính thức
            try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
                 CSVParser csvParser = CSVFormat.DEFAULT
                         .withDelimiter(delimiter)
                         .withSkipHeaderRecord(true)       // bỏ header nếu có
                         .withIgnoreEmptyLines()
                         .withIgnoreSurroundingSpaces()
                         .withTrim()
                         .parse(reader)) {

                for (CSVRecord record : csvParser) {
                    if (record.size() == 0) continue;   // dòng trống thực sự

                    String firstCol = record.get(0);    // chỉ lấy cột đầu
                    if (!StringUtils.hasText(firstCol)) {
                        log.warn("Bỏ qua dòng do cột đầu rỗng (số dòng {} trong file)", record.getRecordNumber());
                        continue;
                    }

                    UrlScamImportRow row = new UrlScamImportRow();
                    row.setDomainOrUrl(firstCol);
                    list.add(row);
                }
                log.info("Phân tích xong {} dòng từ file CSV: {}", list.size(), file.getOriginalFilename());
            }
        } catch (IOException e) {
            log.error("Lỗi khi phân tích file CSV URL: {}", file.getOriginalFilename(), e);
            throw new IOException("Không thể phân tích file CSV (URL): " + e.getMessage(), e);
        }
        return list;
    }

    /**
     * Dò delimiter của file CSV dựa trên dữ liệu vài dòng đầu.
     * Trả về ';' nếu đếm được ';' nhiều hơn ',', ngược lại trả về ','.
     */
    private char detectCsvDelimiter(InputStream is) throws IOException {
        // Đọc dòng bằng BufferedReader nhưng không consume toàn bộ stream
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String line;
            int linesRead = 0;
            int commaCount = 0;
            int semicolonCount = 0;

            while ((line = br.readLine()) != null && linesRead < 3) {  // chỉ cần 1‑3 dòng đầu
                linesRead++;
                commaCount     += line.chars().filter(ch -> ch == ',').count();
                semicolonCount += line.chars().filter(ch -> ch == ';').count();
            }
            return semicolonCount > commaCount ? ';' : ',';
        }
    }

    private List<UrlScamImportRow> parseUrlExcel(MultipartFile file) throws IOException {
        log.debug("Đang thử phân tích file Excel cho URL: {}", file.getOriginalFilename());
        List<UrlScamImportRow> list = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream();
             XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Bỏ qua dòng tiêu đề
            } else {
                log.warn("File Excel (URL) trống hoặc không có dòng tiêu đề: {}", file.getOriginalFilename());
                return list;
            }

            while (rowIterator.hasNext()) {
                Row excelRow = rowIterator.next();
                UrlScamImportRow dto = new UrlScamImportRow();
                String urlData = getCellText(excelRow, EXCEL_COL_URL); // Vẫn đọc cột đầu tiên cho Excel
                dto.setDomainOrUrl(urlData);
                list.add(dto);
            }
            log.info("Phân tích thành công {} dòng từ file Excel (URL): {}", list.size(), file.getOriginalFilename());
        } catch (Exception e) {
            log.error("Lỗi khi phân tích file Excel cho URL lừa đảo: {}", file.getOriginalFilename(), e);
            throw new IOException("Không thể phân tích file Excel (URL): " + e.getMessage(), e);
        }
        return list;
    }

    private String getCellText(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        if (cell == null) {
            return "";
        }

        String cellValue;
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
                    org.apache.poi.ss.usermodel.DataFormatter dateFormatter = new org.apache.poi.ss.usermodel.DataFormatter();
                    cellValue = dateFormatter.formatCellValue(cell);
                } else {
                    BigDecimal bd = new BigDecimal(cell.getNumericCellValue());
                    cellValue = bd.toPlainString();
                    if (cellValue.contains(".") && cellValue.endsWith("0")) {
                        if (bd.stripTrailingZeros().scale() <= 0) {
                            cellValue = bd.stripTrailingZeros().toPlainString();
                        }
                    }
                }
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:
            default:
                cellValue = "";
                break;
        }
        return cellValue.trim();
    }
}
