package com.example.demo.service;

import com.example.demo.dto.SafeDTO;
import com.example.demo.entity.SafeEntity;
import com.example.demo.repository.SafeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SafeService {
    private final SafeRepository safeRepository;

    public void saveMissionId(int missionId) {

        SafeEntity safeEntity = new SafeEntity();
        safeEntity.setMissionId(missionId);
        safeEntity.setInSafe(false);
        safeEntity.setOutSafe(false);

        safeRepository.save(safeEntity);

    }

    public void updateInSafe(int missionId) {
        Optional<SafeEntity> isSafeEntity = safeRepository.findByMissionId(missionId);
        if (isSafeEntity.isPresent()) {
            SafeEntity safeEntity = isSafeEntity.get();
            safeEntity.setInSafe(true);

            safeRepository.save(safeEntity);
        } else {
            System.out.println("금고 테이블에 missionId가 없음. DB 다시 확인");
        }
    }

    public void updateOutSafe(int missionId) {
        Optional<SafeEntity> isSafeEntity = safeRepository.findByMissionId(missionId);
        if (isSafeEntity.isPresent()) {
            SafeEntity safeEntity = isSafeEntity.get();
            safeEntity.setOutSafe(true);

            safeRepository.save(safeEntity);
        } else {
            System.out.println("금고 테이블에 missionId가 없음. DB 다시 확인");
        }
    }

    public SafeDTO findByMissionId(int missionId){
        Optional<SafeEntity> safeEntity = safeRepository.findByMissionId(missionId);
        SafeDTO safeDTO = SafeDTO.toSafeDTO(safeEntity.get());
        return safeDTO;
    }

    public List<SafeDTO> findAll() {
        List<SafeEntity> safeEntityList = safeRepository.findAll();
        List<SafeDTO> safeDTOList = new ArrayList<SafeDTO>();
        for(SafeEntity ent : safeEntityList){
            safeDTOList.add(SafeDTO.toSafeDTO(ent));
        }
        return safeDTOList;
    }

    public List<SafeDTO> findAllByMemberId(String memberId) {
        List<SafeEntity> safeEntityList = safeRepository.findAll();
        List<SafeDTO> safeDTOList = new ArrayList<SafeDTO>();
        for(SafeEntity ent : safeEntityList){
            safeDTOList.add(SafeDTO.toSafeDTO(ent));
        }
        return safeDTOList;
    }
}
