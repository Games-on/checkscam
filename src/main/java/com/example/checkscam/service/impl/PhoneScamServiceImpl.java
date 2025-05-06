package com.example.checkscam.service.impl;

import com.example.checkscam.dto.PhoneScamImportRow;
import com.example.checkscam.dto.ScamStatsDto;
import com.example.checkscam.entity.*;
import com.example.checkscam.exception.CheckScamException;
import com.example.checkscam.repository.PhoneScamRepository;
import com.example.checkscam.repository.PhoneScamStatsRepository;
import com.example.checkscam.repository.ScamTypesRepository;
import com.example.checkscam.service.PhoneScamService;
import com.example.checkscam.service.ScamStatsService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PhoneScamServiceImpl implements PhoneScamService {
    private final PhoneScamRepository repository;
    private final PhoneScamStatsRepository phoneScamStatsRepository;
    private final ScamTypesRepository scamTypesRepository;
    private final ScamStatsService scamStatsService;
    private final EntityManager em;

    @Override
    @Transactional
    public void handleAfterReport(Report report, Long idScamType) throws CheckScamException {

        PhoneScam phoneScam = repository.findByPhoneNumber(report.getInfo());

        if (phoneScam == null) {
            phoneScam = new PhoneScam();
            phoneScam.setPhoneNumber(report.getInfo());
            phoneScam.setDescription(report.getInfoDescription());
            phoneScam = repository.save(phoneScam);
        }

        ScamStatsDto statsDto = (phoneScam.getStats() != null)
                ? new ScamStatsDto(phoneScam.getStats())
                : new ScamStatsDto();

        ScamStatsDto calculated = scamStatsService.handleStats(statsDto, report, idScamType);

        PhoneScamStats stats = phoneScam.getStats();
        if (stats == null) {
            stats = new PhoneScamStats(calculated);
            stats.setPhoneScam(phoneScam);
        } else {
            stats.apply(calculated);
        }

        stats = phoneScamStatsRepository.save(stats);
        phoneScam.setStats(stats);
        repository.save(phoneScam);
    }

    @Transactional
    @Override
    public void importData(MultipartFile file) throws IOException {

        List<PhoneScamImportRow> rows = parse(file);
        List<PhoneScam> batch = new ArrayList<>(500);
        int skippedRows = 0; // Biến đếm số dòng bị bỏ qua

        for (PhoneScamImportRow r : rows) {

            // *** BỔ SUNG KIỂM TRA PHONE NUMBER ***
            String phoneNumber = r.getPhoneNumber();
            // Sử dụng StringUtils.hasText để kiểm tra cả null, rỗng và chỉ chứa khoảng trắng
            if (!StringUtils.hasText(phoneNumber)) {
                log.warn("Skipping row due to empty or blank phone number: {}", r);
                skippedRows++;
                continue; // Bỏ qua dòng này và chuyển sang dòng tiếp theo
            }
            // Trim phoneNumber để loại bỏ khoảng trắng thừa nếu cần
            phoneNumber = phoneNumber.trim();
            // *************************************

            /* --- TÌM TỒN TẠI/HOẶC TẠO MỚI PHONE_SCAM --- */
            // Sử dụng phoneNumber đã được trim
            PhoneScam scam = repository.findByPhoneNumber(phoneNumber);
            if (scam == null) {
                scam = new PhoneScam();
                // Gán phoneNumber đã được trim
                scam.setPhoneNumber(phoneNumber);
            }
            scam.setDescription(r.getDescription()); // Cân nhắc trim description nếu cần

            /* --- TẠO/UPDATE STATS --- */
            PhoneScamStats stats = scam.getStats();
            if (stats == null) {
                stats = new PhoneScamStats();
                stats.setPhoneScam(scam); // @MapsId
            }
            stats.setVerifiedCount(
                    r.getVerifiedCount() == null ? 0 : r.getVerifiedCount());
            String finalPhoneNumber = phoneNumber;
            stats.setLastReportAt(
                    Optional.ofNullable(r.getLastReportAt())
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .map(s -> { // Thêm try-catch để xử lý lỗi parse date
                                try {
                                    return LocalDateTime.parse(s);
                                } catch (Exception e) {
                                    log.warn("Could not parse lastReportAt date '{}' for phone number '{}'. Setting to null.", s, finalPhoneNumber, e);
                                    return null;
                                }
                            })
                            .orElse(null)
            );

            stats.setReasonsJson(null);

            scam.setStats(stats);
            batch.add(scam);

            /* --- FLUSH THEO LÔ --- */
            if (batch.size() >= 500) { // >= để đảm bảo flush khi đủ 500
                log.info("Flushing batch of {} records...", batch.size());
                repository.saveAll(batch);
                em.flush();
                em.clear();
                batch.clear();
                log.info("Batch flushed and cleared.");
            }
        }

        /* --- LÔ CUỐI CÙNG --- */
        if (!batch.isEmpty()) {
            log.info("Flushing final batch of {} records...", batch.size());
            repository.saveAll(batch);
            em.flush();
            em.clear();
            log.info("Final batch flushed and cleared.");
        }

        if (skippedRows > 0) {
            log.warn("Import completed. Skipped {} rows due to missing or blank phone numbers.", skippedRows);
            // Có thể throw exception ở đây nếu muốn báo lỗi rõ ràng hơn cho người dùng
            // throw new CheckScamException("Import completed with warnings. " + skippedRows + " rows were skipped due to missing phone numbers.");
        } else {
            log.info("Import completed successfully. Processed {} rows.", rows.size());
        }
    }

    // --- parse, parseCsv, parseExcel giữ nguyên ---
    // Lưu ý: Nên xem xét việc trim() giá trị phoneNumber ngay trong các hàm parse
    // để xử lý nhất quán hơn. Ví dụ trong parseCsv:
    // row.setPhoneNumber(rec.get("phoneNumber").trim());

    private List<PhoneScamImportRow> parse(MultipartFile file) throws IOException {
        String name = Optional.ofNullable(file.getOriginalFilename()).orElse("").toLowerCase();
        if (name.endsWith(".csv")) return parseCsv(file);
        else if (name.endsWith(".xlsx")) return parseExcel(file);
        else throw new IllegalArgumentException("Chỉ hỗ trợ file định dạng CSV hoặc XLSX");
    }

    private List<PhoneScamImportRow> parseCsv(MultipartFile file) throws IOException {
        try (Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8)) {
            // Thêm withTrim() để tự động loại bỏ khoảng trắng bao quanh giá trị
            CSVParser csv = CSVFormat.DEFAULT
                    .withFirstRecordAsHeader()
                    .withIgnoreSurroundingSpaces()
                    .withTrim() // Tự động trim các giá trị đọc được
                    .parse(reader);

            return csv.getRecords().stream()
                    .map(rec -> {
                        PhoneScamImportRow row = new PhoneScamImportRow();
                        // Không cần trim() ở đây nữa nếu đã dùng withTrim()
                        row.setPhoneNumber(rec.get("phoneNumber"));
                        row.setDescription(rec.get("description"));
                        row.setVerifiedCount(
                                parseIntSafe(rec.get("verifiedCount"))); // verifiedCount đã được trim
                        row.setLastReportAt(rec.get("lastReportAt")); // lastReportAt đã được trim
                        return row;
                    }).collect(Collectors.toList());
        }
    }

    private List<PhoneScamImportRow> parseExcel(MultipartFile file) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(file.getInputStream())) {
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();

            // Kiểm tra xem có dòng header không
            if (!it.hasNext()) {
                return new ArrayList<>(); // File rỗng hoặc không có header
            }
            it.next(); // Bỏ qua header

            List<PhoneScamImportRow> list = new ArrayList<>();
            int rowNum = 1; // Bắt đầu từ dòng 2 (sau header)
            while (it.hasNext()) {
                Row row = it.next();
                rowNum++;
                PhoneScamImportRow dto = new PhoneScamImportRow();
                // Lấy và trim giá trị
                String phoneNumber = getCell(row, 0).trim();
                String description = getCell(row, 1).trim();
                String verifiedCountStr = getCell(row, 2).trim();
                String lastReportAtStr = getCell(row, 3).trim();

                dto.setPhoneNumber(phoneNumber);
                dto.setDescription(description);
                dto.setVerifiedCount(parseIntSafe(verifiedCountStr));
                dto.setLastReportAt(lastReportAtStr);
                list.add(dto);
            }
            return list;
        }
    }


    /* ------------------------- Tiện ích nhỏ ---------------------------- */
    // Đã được sửa đổi để trả về String và trim() trong hàm parseExcel
    private static String getCell(Row row, int idx) {
        Cell c = row.getCell(idx, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        String cellValue;
        if (c.getCellType() == CellType.NUMERIC) {
            // Xử lý cẩn thận hơn với số, tránh lỗi khoa học (ví dụ: 1.23E10)
            // Có thể dùng DataFormatter nếu cần độ chính xác cao hơn với định dạng Excel
            cellValue = String.valueOf((long) c.getNumericCellValue()); // Giữ nguyên nếu chỉ cần số nguyên dài
            // Hoặc dùng DecimalFormat nếu có thể là số thập phân:
            // java.text.DecimalFormat df = new java.text.DecimalFormat("#");
            // cellValue = df.format(c.getNumericCellValue());
        } else {
            cellValue = c.getStringCellValue();
        }
        // Trả về chuỗi rỗng nếu null, thay vì "null"
        return cellValue == null ? "" : cellValue;
    }

    // Giữ nguyên hàm parseIntSafe
    private static Integer parseIntSafe(String s) {
        // Thêm trim và kiểm tra rỗng trước khi parse
        if (s == null) return null;
        s = s.trim();
        if (s.isEmpty()) return null;
        try {
            return Integer.valueOf(s);
        } catch (NumberFormatException e) {
            log.warn("Could not parse integer value '{}'", s, e); // Ghi log lỗi parse
            return null;
        }
    }

}
