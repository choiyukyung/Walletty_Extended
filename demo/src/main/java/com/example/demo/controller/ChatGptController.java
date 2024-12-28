package com.example.demo.controller;

import com.example.demo.dto.FeedbackDTO;
import com.example.demo.dto.MemberDTO;
import com.example.demo.dto.MissionDTO;
import com.example.demo.service.FeedbackService;
import com.example.demo.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/chat-gpt")
@RestController
public class ChatGptController {
    private final FeedbackService feedbackService;
    private final MissionService missionService;

    @PostMapping("/feedback") // 프론트에서 회원 id와 함께 피드백 요청하면 해당 회원의 피드백 테이블에 아직 안 보낸 피드백을 골라서 넘겨줌.
    public List<FeedbackDTO> sendFeedback(@RequestBody MemberDTO memberDTO) { // /feedback에서 받은 회원 정보(id만??)로 피드백 보내주기
        // FeedbackDTO feedbackDTO = feedbackService.findByMemberIdAndOkToSend(memberDTO.getMemberId(),"false");
        List<FeedbackDTO> feedbackDTO = feedbackService.findByMemberId(memberDTO.getMemberId());
        return feedbackDTO; // FeedbackDTO 타입의 list를 리턴
    }

    @PostMapping("/mission") // 프론트에서 회원 id와 함께 미션 요청하면 해당 회원의 미션 테이블에서 현재 미션을 골라서 넘겨줌.
    public List<MissionDTO> sendMission(@RequestBody MemberDTO memberDTO) {
        List<MissionDTO> missionDTO = missionService.findByMemberId(memberDTO.getMemberId());
        return missionDTO;
    }


}