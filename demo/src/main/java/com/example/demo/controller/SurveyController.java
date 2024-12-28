package com.example.demo.controller;

import com.example.demo.dto.ChatGptResponse;
import com.example.demo.dto.SurveyDTO;
import com.example.demo.dto.SurveyLimitDTO;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final AccountAnalyzeService accountAnalyzeService;
    private final ChatGptService chatGptService;
    private final MissionService missionService;
    private final FeedbackService feedbackService;
    private final ProfileService profileService;

    private final SurveyService surveyService;

    // 설문조사 작성하고 db에 저장하기 /survey
    @PostMapping
    public SurveyDTO doSurvey(@RequestBody SurveyDTO surveyDTO){
        // 미션, 피드백 문장을 만들기 위한 [ 해당 회원 id / 미션, 피드백 시작 날짜 정보 ]
        String selectedMemberId = surveyDTO.getSurveyId();
        String startDate = accountAnalyzeService.findThisWeek(selectedMemberId);
        
        if(surveyService.findBySurveyId(selectedMemberId) == null){ // 중간데모용 if문 (survey가 이미 있으면 설문조사 저장x, 미션 로직 x / 없으면 실행)
            System.out.println(surveyDTO);
            surveyService.save(surveyDTO);
            
            // 설문조사 끝나자 마자 최근 일주일 간의 결제내역 분석결과를 기반으로 미션 생성 (payment 객체 불러와서 AA table에 분석 결과가 저장되어있는 상태에서)
            
            // 1. 미션 로직을 통해 미션 항목 선정
            missionService.mission(selectedMemberId);

            // 2. 선정된 항목으로 미션 문장 만들고 저장
            ChatGptResponse chatGptResponseForMission = null;
            chatGptResponseForMission = chatGptService.askQuestionM(selectedMemberId, startDate);
            String missionContent = chatGptResponseForMission.getChoices().get(0).getMessage().getContent();
            System.out.println(missionContent);
            missionService.saveMissionSen(selectedMemberId, startDate, missionContent);

            // 3. 선정된 항목과 분석 테이블의 totalAmount를 토대로 피드백 문장 만들고 저장
            ChatGptResponse chatGptResponseForFeedback = null;
            chatGptResponseForFeedback = chatGptService.askQuestion(selectedMemberId, startDate);
            String feedbackContent = chatGptResponseForFeedback.getChoices().get(0).getMessage().getContent();
            feedbackService.save(selectedMemberId, feedbackContent, startDate);

            // 4. 이번 주차 미션과 피드백 문장 생성 후 분석 테이블에 이번 주차 항목들의 OkToUse True로 변경
            accountAnalyzeService.changeOkToUseWithTrue(selectedMemberId);

            // 5. profile goalEntry 업데이트
            profileService.updateGoalEntry(selectedMemberId);
        }
        return surveyDTO;
    }

    // 설문조사 보여주기(로그인 id 가져와서) /survey
    @GetMapping
    public SurveyDTO sendSurveyDTO(@RequestParam String memberId){
        return surveyService.findBySurveyId(memberId);
    }


    private final SurveyLimitService surveyLimitService;

    // /survey/limit
    @PostMapping("/limit")
    public SurveyLimitDTO sendSurveyLimit(@RequestBody SurveyDTO surveyDTO){
        int entry1totalAmount = accountAnalyzeService.findbySurveyIdAndEntry(surveyDTO.getSurveyId(), surveyDTO.getGoalEntry1());
        int entry2totalAmount = accountAnalyzeService.findbySurveyIdAndEntry(surveyDTO.getSurveyId(), surveyDTO.getGoalEntry2());
        int entry3totalAmount = accountAnalyzeService.findbySurveyIdAndEntry(surveyDTO.getSurveyId(), surveyDTO.getGoalEntry3());

        return surveyLimitService.save(surveyDTO.getSurveyId(), entry1totalAmount, entry2totalAmount, entry3totalAmount);
    }

}
