package com.example.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity // DB 테이블과 1대 1로 매핑되는 객체
@Table(name = "user")
@Getter // Getter, Getter, Builder, Constructor은 lombok 어노테이션으로 관련 코드를 자동 생성
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @ManyToMany // User 객체와 권한 객체의 ManyToMany 관계를 @JoinTable을 통해서 일대다, 다대일 관계의 조인 테이블로 정리했다는 의미
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}
