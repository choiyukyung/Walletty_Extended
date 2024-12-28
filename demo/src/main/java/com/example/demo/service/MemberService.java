package com.example.demo.service;

import com.example.demo.dto.MemberDTO;
import com.example.demo.entity.MemberEntity;
import com.example.demo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service //스프링이 관리해주는 객체 == 스프링 빈
@RequiredArgsConstructor //controller와 같이. final 멤버변수 생성자 만드는 역할
public class MemberService {

    private final MemberRepository memberRepository; // 먼저 jpa, mysql dependency 추가

    public void save(MemberDTO memberDTO) {
        // repository의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        //Repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)

    }

    public MemberDTO login(MemberDTO memberDTO){ //entity객체는 service에서만
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberDTO.getMemberId());
        if(byMemberId.isPresent()){
            // 조회 결과가 있다
            MemberEntity memberEntity = byMemberId.get(); // Optional에서 꺼냄
            if(memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                //비밀번호 일치
                //entity -> dto 변환 후 리턴
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                //비밀번호 불일치
                return null;
            }
        } else {
            // 조회 결과가 없다
            return null;
        }
    }

    public MemberDTO postMember(String memberId){
        Optional<MemberEntity> byMemberId = memberRepository.findByMemberId(memberId); //여기는 프론트에서 받아오는 다른 값으로 바꾸기.
        if(byMemberId.isPresent()){
            // 조회 결과가 있다
            MemberEntity memberEntity = byMemberId.get(); // Optional에서 꺼냄
            MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
            return dto;
        } else {
            // 조회 결과가 없다
            return null;
        }
    }
}
