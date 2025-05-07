package com.example.checkscam.service.impl;

import com.example.checkscam.component.GptClient;
import com.example.checkscam.dto.OpenRouterMessage;
import com.example.checkscam.dto.request.CheckScamRequestDto;
import com.example.checkscam.dto.request.OpenRouterRequest;
import com.example.checkscam.dto.response.OpenRouterResponse;
import com.example.checkscam.service.*;
import com.example.checkscam.service.deepseek.DeepSeekService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CheckScamServiceImpl implements CheckScamService {
    private final ScamStatsService scamStatsService;
    private final EntityManager manager;
    private final GptClient gptClient;
    private final DeepSeekService deepSeekService;

    @Override
    public String checkScam(CheckScamRequestDto requestDto) {
        Object infoScam = null;
        switch (requestDto.getType()){
            case SDT -> {
                infoScam = scamStatsService.getPhoneScamStatsInfo(requestDto.getInfo());
            }
            case STK -> {
                infoScam = scamStatsService.getBankScamStatsInfo(requestDto.getInfo());
            }
            case URL -> {
                infoScam = scamStatsService.getUrlScamStatsInfo(requestDto.getInfo());
            }
        }
        log.info( infoScam == null ? "khong co thong tin" : infoScam.toString());
        if (!ObjectUtils.isEmpty(infoScam)){
            OpenRouterMessage userMessage = new OpenRouterMessage("system", "Tôi sẽ đưa bạn một đoạn dữ liệu thông tin về lừa đảo ( sđt, stk , url) bạn hãy " +
                    "chỉnh sửa nó thành một đoạn văn bản hoàn chỉnh, dễ hiểu, thân thiện để cho người dùng dễ đọc với fomat như sau: " +
                    "VD:UrlScamStatsInfoDto(id=41597, urlScam=apple.appletwpoorf.com, verifiedCount=1, lastReportAt=null) bạn sẽ chuyển thành như sau:" +
                    "đường dẫn lừa đảo apple.appletwpoorf.com đã được xác thực lừa đảo từ 1 người dùng, chưa có thông tin chi tiết về đường dẫn này" +
                    "VD2: PhoneScamStatsInfoDto(id=20, phoneNumber=0123456787, lastReportAt=2025-05-07T08:52:08, verifiedCount=1, reasonsJson=ReasonsJsonDto(reasons=[ReasonsJsonDto.Reason(name=Giả mạo công an, quantity=1)]))" +
                    "bạn sẽ chuyển thành như sau: số điện thoại 0123456787 đã được xác thực lừa đảo từ 1 người dùng, với lý do giả mạo công an" +
                    "Dữ liệu bạn cần chuyển đổi là: " + infoScam +
                    "Lưu ý chỉ đưa ra câu trả lời theo mẫu và không thêm gì ( có thể sửa lại câu chữ cho dễ hiểu hơn","text");
            log.info( userMessage.toString());
            OpenRouterRequest openRouterRequest = new OpenRouterRequest();
            openRouterRequest.setModel("google/learnlm-1.5-pro-experimental:free"); //deepseek/deepseek-r1-distill-llama-70b:free
            openRouterRequest.setMessages(List.of(userMessage));

            OpenRouterResponse response = deepSeekService.chatCompletions(openRouterRequest);
            log.info("response: " + response.toString());
            return response.getChoices().get(0).getMessage().getContent();
        }else
            return "Hiện tại không có thông tin lừa đảo về" + requestDto.getInfo();
    }
}
