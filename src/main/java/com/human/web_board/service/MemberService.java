package com.human.web_board.service;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;

import java.util.List;

// MemberService 인터페이스는 서비스에 필요한 설계 명세 즉, 메서드 이름을 정해서 상속 주기
public interface MemberService {
    // 회원 가입
    Long signup(MemberSignupReq req);

    // 로그인
    MemberRes login(String email, String pwd);

    // 이메일로 회원 조회
    MemberRes getByEmail(String email);

    // ID로 회원 조회
    MemberRes getById(Long id);

    // 회원 목록 보기
    List<MemberRes> list();

    boolean delete(Long id);

    boolean update(MemberSignupReq req, Long id);
}
