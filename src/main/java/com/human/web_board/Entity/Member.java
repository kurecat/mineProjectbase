package com.human.web_board.Entity; // 패키지명은 프로젝트 구조에 맞게 수정

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity // 이 클래스는 DB 테이블입니다! 라는 표시
@Getter @Setter
@NoArgsConstructor // 기본 생성자 필수
@Table(name = "members") // DB 테이블 이름 지정 (생략하면 클래스 이름인 member가 됨)
public class Member {

    @Id // Primary Key (기본키)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 오라클이면 SEQUENCE, MySQL이면 IDENTITY
    private Long id;

    @Column(nullable = false, unique = true) // null 불가, 중복 불가
    private String email;

    @Column(name = "PWD", nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String grade; // "1": 유저, "2": 관리자

    private int point;

    private String profileImg;

    @Column(updatable = false) // 수정 불가 (가입일이니까)
    private LocalDateTime regDate;
}