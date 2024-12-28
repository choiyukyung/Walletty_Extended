package com.example.demo.controller;

import com.example.demo.dto.RankingDTO;
import com.example.demo.service.RankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ranking")
public class RankingController {
    private final RankingService rankingService;

    @GetMapping("/top3") // 서비스 전체에서 ranking 탑3 프론트로 보내줌
    public List<RankingDTO> sendRankingTop3(){
        return rankingService.findByRanks();
    }

    @GetMapping("/friend") // 친구들 ranking 프론트로 보내줌
    public List<RankingDTO> sendRankingFriend(){
        return rankingService.findByIsFriend();
    }


}
