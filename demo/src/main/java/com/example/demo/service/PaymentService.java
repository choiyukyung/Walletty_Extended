package com.example.demo.service;

import com.example.demo.dto.PaymentDTO;
import com.example.demo.entity.PaymentEntity;
import com.example.demo.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service //스프링이 관리해주는 객체 == 스프링 빈
@RequiredArgsConstructor //controller와 같이. final 멤버변수 생성자 만드는 역할
public class PaymentService {

    private final PaymentRepository paymentRepository; // 먼저 jpa, mysql dependency 추가

    public void save(PaymentDTO paymentDTO) {
        // repository의 save 메서드 호출
        PaymentEntity paymentEntity = PaymentEntity.toPaymentEntity(paymentDTO);
        paymentRepository.save(paymentEntity);
        //Repository의 save메서드 호출 (조건. entity객체를 넘겨줘야 함)

    }
}
