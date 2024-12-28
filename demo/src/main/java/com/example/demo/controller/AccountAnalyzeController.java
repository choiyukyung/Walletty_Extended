package com.example.demo.controller;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.service.AccountAnalyzeService;
import com.example.demo.service.PaymentService;
import com.example.demo.service.ProfileService;
import com.example.demo.service.StatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AccountAnalyzeController {

    private final PaymentService paymentService;
    private final AccountAnalyzeService accountAnalyzeService;
    private final StatisticsService statisticsService;
    private final ProfileService profileService;

    @GetMapping("/payment/{date}/{memberId}")
    public void paymentForm(@PathVariable("date") String date,@PathVariable("memberId") String memberId) throws IOException, InterruptedException {
        // 트랜잭션은 orderId의 날짜 기준으로 일주일 단위로 가져온다고 가정.
        // 나중에 memberId도 json으로 받은걸로 변경
        String apiUri = "";
        if(date.equals("1")){
            apiUri = "https://api.tosspayments.com/v1/transactions?startDate=2023-11-01T00:00:00&endDate=2023-11-02T21:13:40";
        } else if (date.equals("2")) {
            apiUri = "https://api.tosspayments.com/v1/transactions?startDate=2023-11-02T21:13:40&endDate=2023-11-30T00:00:00";
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiUri))
                .header("Authorization", "Basic dGVzdF9za19leDZCSkdRT1ZEOUVhR3hYNVpSclc0dzJ6TmJnOg==")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        String paymentstr = response.body();
        paymentstr = paymentstr.replace("mId", "mid");

        ObjectMapper mapper = new ObjectMapper();
        try {
            // 스트링에서 DTO로 매핑하기
            List<PaymentDTO> dtos = Arrays.asList(mapper.readValue(paymentstr, PaymentDTO[].class));
            System.out.println(dtos.size() + "개의 dto : " +dtos);

            for(PaymentDTO pay : dtos){
                String data[] = pay.getOrderId().split("-");
                if(pay.getStatus().equals("DONE") && data[0].equals("pay")){  //CANCELED, DONE, WAITING_FOR_DEPOSIT
                    pay.setEntry(data[1]);
                    pay.setOrderDate(data[3]);
                    pay.setMemberId(memberId);
                    paymentService.save(pay);
                }
            }

            // payment 들고올 때 분석 테이블 저장 후
            accountAnalyzeService.saveAmount(memberId, date);

            // profile의 weekTotalAmount 업데이트
            // 회원가입 후 첫 결제내역 분석일 때 프로필 새로 생성 (save) , 처음이 아니라면 update
            profileService.updateWeekTotalAmount(memberId);

            // 통계 테이블 저장
            statisticsService.saveRate(memberId, date);


        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }




}
