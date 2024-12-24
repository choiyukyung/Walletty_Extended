package com.example.demo.service;

import com.example.demo.dto.FeedbackDTO;
import com.example.demo.entity.FeedbackCommentEntity;
import com.example.demo.entity.FeedbackEntity;
import com.example.demo.repository.FeedbackCommentRepository;
import com.example.demo.repository.FeedbackRepository;
import com.example.demo.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackCommentRepository feedbackCommentRepository;
    private final MissionRepository missionRepository;

    public void save(String id, String content, String startDate) {
        List<FeedbackCommentEntity> sourceData = feedbackCommentRepository.findAll();

        Random random = new Random();
        int randomIndex = random.nextInt(sourceData.size()); // 무작위 인덱스 선택
        FeedbackCommentEntity randomComment = sourceData.get(randomIndex);

        String sendFront = randomComment.getComment() + " " + content;

        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setMemberId(id);
        feedbackEntity.setContent(content);
        feedbackEntity.setComment(randomComment.getComment());
        feedbackEntity.setSendFront(sendFront);
        feedbackEntity.setStartdate(startDate);
        feedbackEntity.setOkToSend("false");

        feedbackRepository.save(feedbackEntity);

    }

    public void changeWeek(String memberId, String lastStartDate){
        Optional<FeedbackEntity> lastFeedback =
                feedbackRepository.findByMemberIdAndStartdate(memberId,lastStartDate);

        if (lastFeedback.isPresent()) {
            FeedbackEntity originEntity = lastFeedback.get();
            originEntity.setOkToSend("true");
        }
    }

    public FeedbackDTO findByMemberIdAndOkToSend(String memberId, String okToSend) {
        Optional<FeedbackEntity> byMemberIdOkToSend = feedbackRepository.findByMemberIdAndOkToSend(memberId,okToSend);
        if(byMemberIdOkToSend.isPresent()) {
            FeedbackEntity feedbackEntity = byMemberIdOkToSend.get();
            FeedbackDTO fDTO = FeedbackDTO.toFeedbackDTO(feedbackEntity);
            return fDTO;
        }else{
            return null;
        }

    }

    public List<FeedbackDTO> findByMemberId(String memberId) {
        List<FeedbackEntity> byMemberId = feedbackRepository.findByMemberId(memberId);
        List<FeedbackDTO> fDTO = new ArrayList<>();
        for (FeedbackEntity ent : byMemberId){
            fDTO.add(FeedbackDTO.toFeedbackDTO(ent));
        }
        return fDTO;
    }



    /*
    public void saveContent(String content) {
        FeedbackEntity feedbackEntity = new FeedbackEntity();
        feedbackEntity.setContent(content);
        feedbackRepository.save(feedbackEntity);

    }
     */

}
