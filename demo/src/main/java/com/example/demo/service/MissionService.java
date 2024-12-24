package com.example.demo.service;

import com.example.demo.dto.AccountAnalyzeDTO;
import com.example.demo.dto.MissionDTO;
import com.example.demo.dto.SurveyDTO;
import com.example.demo.entity.AccountAnalyzeEntity;
import com.example.demo.entity.MissionEntity;
import com.example.demo.repository.AccountAnalyzeRepository;
import com.example.demo.repository.MissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class MissionService {

    private final MissionRepository missionRepository;
    private final AccountAnalyzeRepository accountAnalyzeRepository;
    private final AccountAnalyzeService accountAnalyzeService;
    private final SurveyService surveyService;

    public void save(MissionDTO missionDTO) {
        MissionEntity missionEntity = MissionEntity.toMissionEntity(missionDTO);
        missionRepository.save(missionEntity);

    }

    public void saveMissionSen(String memberId, String startDate, String content) {

        Optional<MissionEntity> ismissionEntity = missionRepository.findByMemberIdAndStartDate(memberId, startDate);
        if(ismissionEntity.isPresent()) {
            MissionEntity missionEntity = ismissionEntity.get();
            missionEntity.setMissionSen(content);
            missionRepository.save(missionEntity);
        }else{
            System.out.println("미션 테이블에 id랑 날짜가 없는데 미션을 줄 수 없음. db 다시 업데이트");
        }

    }

    public void saveAccept(int missionId) {

        Optional<MissionEntity> ismissionEntity = missionRepository.findByMissionId(missionId);
        if(ismissionEntity.isPresent()) {
            MissionEntity missionEntity = ismissionEntity.get();
            missionEntity.setAccept(true);
            missionRepository.save(missionEntity);
        }else{
            System.out.println("미션 테이블에 missionId가 없음. DB 다시 확인");
        }

    }


    public List<MissionDTO> findByMemberId(String memberId) {
        List<MissionEntity> byMemberId = missionRepository.findByMemberId(memberId);
        List<MissionDTO> mDTO = new ArrayList<>();
        for (MissionEntity ent : byMemberId){
            mDTO.add(MissionDTO.toMissionDTO(ent));
        }
        return mDTO;
    }

    public MissionDTO findByStartDate(String startDate) {

        Optional<MissionEntity> ismissionEntity = missionRepository.findByStartDate(startDate);
        if(ismissionEntity.isPresent()) {
            MissionEntity missionEntity = ismissionEntity.get();
            MissionDTO mdto = MissionDTO.toMissionDTO(missionEntity);
            return mdto;
        }
        else{
            return null;
        }
    }


    public MissionDTO findByMemberIdAndStartDate(String memberId, String startDate) {
        Optional<MissionEntity> ismissionEntity = missionRepository.findByMemberIdAndStartDate(memberId, startDate);
        if(ismissionEntity.isPresent()) {
            MissionEntity missionEntity = ismissionEntity.get();
            MissionDTO mdto = MissionDTO.toMissionDTO(missionEntity);
            return mdto;
        }
        else{
            return null;
        }

    }

    public void isMissionSuccess(int missionId){
        Optional<MissionEntity> ismissionEntity = missionRepository.findByMissionId(missionId);
        if(ismissionEntity.isPresent()) {
            MissionEntity missionEntity = ismissionEntity.get();
            MissionDTO mdto = MissionDTO.toMissionDTO(missionEntity);
            //저번주 미션 아이디를 받아와서 저번주 미션 항목과 금액과 이번주 분석 테이블에 해당 항목과 금액을 비교
            String lastMissionEnd = mdto.getEndDate();
            System.out.println(mdto.getMissionEntry()+mdto.getMemberId()+lastMissionEnd);
            Optional<AccountAnalyzeEntity> isaccountAnalyzeEntity = accountAnalyzeRepository.findByEntryAndMemberIdAndOrderWeek(mdto.getMissionEntry(),mdto.getMemberId(),lastMissionEnd);
            System.out.println(isaccountAnalyzeEntity);
            if(isaccountAnalyzeEntity.isPresent()){
                AccountAnalyzeEntity accountAnalyzeEntity = isaccountAnalyzeEntity.get();
                AccountAnalyzeDTO adto = AccountAnalyzeDTO.toAccountAnalyzeDTO(accountAnalyzeEntity);

                if(adto.getTotalAmount() <= mdto.getMissionMoney()){

                    missionEntity.setSuccess("success");
                    missionRepository.save(missionEntity);

                }
                else{
                    missionEntity.setSuccess("fail");
                    missionRepository.save(missionEntity);
                }
            }else {
                System.out.println("아직 미션 결과를 알 수 없습니다(분석테이블에 미션을 수행한 주의 결제내역이 없습니다.)");
            }
        }
        else{
            System.out.println("잘못된 missionId입니다.");
        }
    }

    public void changeWeek(String memberId, String lastStartDate) {
        Optional<MissionEntity> lastMission =
                missionRepository.findByMemberIdAndStartDate(memberId,lastStartDate);

        if (lastMission.isPresent()) {
            MissionEntity originEntity = lastMission.get();
            originEntity.setNow("false");
        }
    }

    public MissionDTO makeMissionDTO(String memberId, String missionEntry, int missionMoney, String startDate, String endDate){
        MissionDTO missionDTO = new MissionDTO();
        missionDTO.setMemberId(memberId);
        missionDTO.setMissionEntry(missionEntry);
        missionDTO.setMissionMoney(missionMoney);
        missionDTO.setStartDate(startDate);
        missionDTO.setEndDate(endDate);
        missionDTO.setNow("true");

        return missionDTO;

    }

    public MissionDTO compareWithLastWeek(String memberId, List<String> missionCandi, int mission_diff, int missionStart){
        List<AccountAnalyzeDTO> nowDtos = accountAnalyzeService.findByMemberIdAndOkToUse(memberId,false);

        String orderWeek = "";
        for (AccountAnalyzeDTO useEntry : nowDtos) {
            orderWeek = useEntry.getOrderWeek();
        }
        orderWeek = Integer.toString(Integer.parseInt(orderWeek) - 7);

        List<AccountAnalyzeDTO> lastDtos = accountAnalyzeService.findByMemberIdAndOrderWeek(memberId, orderWeek);
        System.out.println(lastDtos);

        int last_diff = 0;
        String missionEntry = "";
        int missionMoney = 0;
        for (AccountAnalyzeDTO now : nowDtos) {
            for (AccountAnalyzeDTO last : lastDtos) {

                if(now.getEntry().equals(last.getEntry()) && missionCandi.contains(now.getEntry())){

                    int diff = now.getTotalAmount() - last.getTotalAmount();
                    if(diff > last_diff){
                        last_diff = diff;
                        missionEntry = now.getEntry();
                        missionMoney = last.getTotalAmount();
                    }
                }
            }
        }
        if(last_diff > mission_diff * 3){
            System.out.println("지난 주와의 사용 금액의 차가 이번 주와 미션 금액의 차의 3배 이상이므로, 지난 주에 많이 사용한 항목을 미션으로 줍니다.");

            missionMoney += ((last_diff / 2) / 1000) * 1000;

            System.out.println(missionEntry);
            System.out.println(missionMoney);

            int missionEnd = missionStart + 6;
            String startDate = Integer.toString(missionStart);
            String endDate = Integer.toString(missionEnd);

            MissionDTO missionDTO = makeMissionDTO(memberId, missionEntry, missionMoney, startDate, endDate);
            //missionService.save(missionDTO);

            return missionDTO;
        }
        else{
            return null;
        }
    }

    public void mission(String memberId) {
        String missionEntry = "";
        int missionMoney = 0;

        // 1. 분석테이블을 보고 사용한 항목은 모두 missionCandi에
        List<AccountAnalyzeDTO> dtos = accountAnalyzeService.findByMemberIdAndOkToUse(memberId,false);
        System.out.println(dtos);

        List<String> missionCandi = new ArrayList<>();

        int missionStart = 0;
        for (AccountAnalyzeDTO useEntry : dtos) {
            missionCandi.add(useEntry.getEntry());
            missionStart = Integer.parseInt(useEntry.getOrderWeek()) + 1;
        }
        System.out.println(missionCandi);

        //2. 지난 미션이 있으면 항목 삭제
        String lastMissionDate = Integer.toString(missionStart - 7);
        MissionDTO lastmission = findByMemberIdAndStartDate(memberId,lastMissionDate);

        if(lastmission != null){


            missionCandi.remove(lastmission.getMissionEntry());
            System.out.println("제거완료");
            System.out.println(lastmission.getMissionEntry());
        }

        System.out.println(missionCandi);

        //3. fixed_entry 삭제
        SurveyDTO surveyDTO = surveyService.findBySurveyId(memberId);

        String[] fixedEntry = surveyDTO.getFixedEntry().split(",");
        for (AccountAnalyzeDTO useEntry : dtos) {
            for (String fEntry : fixedEntry) {
                if (useEntry.getEntry().equals(fEntry)) {
                    missionCandi.remove(fEntry);
                }
            }
        }
        System.out.println(missionCandi);

        // 4. 목표 항목과 소비의 차가 가장 큰 항목을 고름
        // 4-1. 분석 테이블에 지난주가 없으면 그냥 4를 미션으로 줌
        int diff_max = 0;
        for (AccountAnalyzeDTO useEntry : dtos) {
            if(missionCandi.contains(useEntry.getEntry())) {
                if (useEntry.getEntry().equals(surveyDTO.getGoalEntry1())) {
                    if (useEntry.getTotalAmount() - surveyDTO.getGoalMoney1() > diff_max) {
                        diff_max = useEntry.getTotalAmount() - surveyDTO.getGoalMoney1();
                        missionEntry = surveyDTO.getGoalEntry1();
                        missionMoney = surveyDTO.getGoalMoney1();
                    }
                }
                if (useEntry.getEntry().equals(surveyDTO.getGoalEntry2())) {
                    if (useEntry.getTotalAmount() - surveyDTO.getGoalMoney2() > diff_max) {
                        diff_max = useEntry.getTotalAmount() - surveyDTO.getGoalMoney2();
                        missionEntry = surveyDTO.getGoalEntry2();
                        missionMoney = surveyDTO.getGoalMoney2();
                    }
                }
                if (useEntry.getEntry().equals(surveyDTO.getGoalEntry3())) {
                    if (useEntry.getTotalAmount() - surveyDTO.getGoalMoney3() > diff_max) {
                        diff_max = useEntry.getTotalAmount() - surveyDTO.getGoalMoney3();
                        missionEntry = surveyDTO.getGoalEntry3();
                        missionMoney = surveyDTO.getGoalMoney3();
                    }
                }
                System.out.println(missionEntry);
            }
        }
        // 4-1. 분석 테이블에 지난 주가 없으면 그냥 4 줌
        if (diff_max > 0 && lastmission == null) {
            missionMoney += ((diff_max / 2) / 1000) * 1000;

            System.out.println(missionEntry);
            System.out.println(missionMoney);

            int missionEnd = missionStart + 6;
            String startDate = Integer.toString(missionStart);
            String endDate = Integer.toString(missionEnd);

            //MissionDTO에 저장(missionId, memberId, missionEntry, missionMoney, now, startDate까지)
            MissionDTO missionDTO = makeMissionDTO(memberId, missionEntry, missionMoney, startDate, endDate);

            save(missionDTO);

        }//4-2. 분석 테이블에 지난 주가 있으면 지난 주보다 훨씬 많이 쓴 항목을 계산하고 4번과 비교한 후 4번보다 3배 이상 차이가 크다면 미션으로 줌
        else if(diff_max > 0 && lastmission != null){
            MissionDTO missionDTOWithLastWeek = compareWithLastWeek(memberId, missionCandi, diff_max, missionStart);
            if(missionDTOWithLastWeek != null){

                save(missionDTOWithLastWeek);
            }
            else{
                missionMoney += ((diff_max / 2) / 1000) * 1000;

                System.out.println(missionEntry);
                System.out.println(missionMoney);

                int missionEnd = missionStart + 6;
                String startDate = Integer.toString(missionStart);
                String endDate = Integer.toString(missionEnd);


                MissionDTO missionDTO = makeMissionDTO(memberId, missionEntry, missionMoney, startDate, endDate);

                save(missionDTO);
            }
        }
        //이게 5인 상황
        //5. 목표 항목이 모두 다 목표를 지켰으면
        else{
            System.out.println("목표금액보다 많이 쓴 항목이 없다.");

            int maxUse = 0;
            for (AccountAnalyzeDTO useEntry : dtos) {

                if (missionCandi.contains(useEntry.getEntry())) {
                    if (useEntry.getTotalAmount() > maxUse) {
                        missionEntry = useEntry.getEntry();
                        missionMoney = useEntry.getTotalAmount();
                    }
                }
            }
            missionMoney = (int) (missionMoney * 0.9);
            missionMoney = ((missionMoney / 1000) * 1000);

            System.out.println(missionEntry);
            System.out.println(missionMoney);

            int missionEnd = missionStart + 6;
            String startDate = Integer.toString(missionStart);
            String endDate = Integer.toString(missionEnd);


            MissionDTO missionDTO = makeMissionDTO(memberId, missionEntry, missionMoney, startDate, endDate);

            save(missionDTO);
        }


    }

    public int findByMemberIdAndNow(String memberId,String now) {
        Optional<MissionEntity> ismissionEntity = missionRepository.findByMemberIdAndNow(memberId,now);
        if(ismissionEntity.isPresent()) {
            MissionEntity missionEntity = ismissionEntity.get();
            return missionEntity.getMissionId();
        }else{
            System.out.println("미션 테이블에 id랑 날짜가 없는데 미션을 줄 수 없음. db 다시 업데이트");
            return 0;
        }
    }
}
