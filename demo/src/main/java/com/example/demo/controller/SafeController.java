package com.example.demo.controller;

import com.example.demo.dto.SafeDTO;
import com.example.demo.service.MissionService;
import com.example.demo.service.ProfileService;
import com.example.demo.service.SafeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/safe")
public class SafeController {

    private final SafeService safeService;
    private final MissionService missionService;
    private final ProfileService profileService;

    @GetMapping("/missionAccept/{missionId}") // 수락 버튼을 눌렀을 때 프론트에서 missionId를 줌
    public void missionAccept(@PathVariable("missionId") int missionId){
        safeService.saveMissionId(missionId); // 금고 테이블에 row 생김
        missionService.saveAccept(missionId); // 미션 테이블에 accept가 true

        // 미션 수락한 경우 profile의 missionCnt 업데이트
        profileService.updateMission(missionId);
    }

    @GetMapping("/moneyIn/{missionId}") // 입금 완료했을 때 프론트에서 missionId를 줌
    public void moneyInSafe(@PathVariable("missionId") int missionId){
        safeService.updateInSafe(missionId);

    }


    @GetMapping("/moneyOutSafe/{missionId}") // 금고에서 돈 돌려받기를 눌렀을 때 프론트에서 missionId를 줌
    public void moneyOutSafe(@PathVariable("missionId") int missionId){
        safeService.updateOutSafe(missionId);

    }

    @GetMapping // 백에서 safeDTO를 list로 보내줌.
    public List<SafeDTO> sendSafeDTO(){
        return safeService.findAll();
    }
}
