package com.example.demo.service;

import com.example.demo.dto.StatisticsDTO;
import com.example.demo.entity.AccountAnalyzeEntity;
import com.example.demo.entity.ProfileEntity;
import com.example.demo.entity.StatisticsEntity;
import com.example.demo.repository.AccountAnalyzeRepository;
import com.example.demo.repository.ProfileRepository;
import com.example.demo.repository.StatisticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final StatisticsRepository statisticsRepository;
    private final AccountAnalyzeRepository accountAnalyzeRepository;
    private final ProfileRepository profileRepository;

    public StatisticsDTO findByMemberId(String memberId){
        Optional<StatisticsEntity> statisticsEntity = statisticsRepository.findByMemberId(memberId);
        StatisticsDTO statisticsDTO = StatisticsDTO.toStatisticsDTO(statisticsEntity.get());
        return statisticsDTO;
    }

    public List<StatisticsDTO> findByMemberIdAndWeek(String memberId, String week){
        List<StatisticsEntity> statisticsEntities = statisticsRepository.findByMemberIdAndWeek(memberId, week);
        List<StatisticsDTO> statisticsDTOList = new ArrayList<>();
        for (StatisticsEntity entity : statisticsEntities) {
            statisticsDTOList.add(StatisticsDTO.toStatisticsDTO(entity));
        }
        return statisticsDTOList;
    }

    public void saveRate(String memberId, String date) {
        String week = "0";
        if (date.equals("1")){
            week = "1107";
        } else if (date.equals("2")) {
            week = "1114";
        }

        // 항목 별 사용 금액
        List<AccountAnalyzeEntity> accountAnalyzeEntities = accountAnalyzeRepository.findByMemberIdAndOrderWeek(memberId, week);

        // rate 계산하기 위한 분모
        Optional<ProfileEntity> profile = profileRepository.findByMemberId(memberId);
        Integer total = profile.get().getWeekTotalAmount();

        // rate 계산
        for (AccountAnalyzeEntity acc : accountAnalyzeEntities) {
            double rate = ((double) acc.getTotalAmount() / (double) total * 100.0);

            // 통계DTO
            StatisticsDTO statisticsDTO = new StatisticsDTO(memberId, acc.getEntry(), Math.round(rate * 100) / 100.0, week); //rate 소수점 둘째 자리까지만 보내기
            StatisticsEntity statisticsEntity = StatisticsEntity.toStatisticsEntity(statisticsDTO);
            statisticsRepository.save(statisticsEntity);
        }

    }
}
