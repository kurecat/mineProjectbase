package com.human.web_board.service;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.dto.MemberSummaryRes;

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
    // 회원 삭제 도 넣어놓음
    boolean delete(Long id);
    //  혹시 모르니 멤버 수정도 넣음
    boolean update(MemberSignupReq req, Long id);
    // 닉네임 존재 여부 체크
    boolean isNicknameExists(String nickname);

    // 이메일 존재 여부 체크 추가
    boolean isEmailExists(String email);

    boolean checkEmailExists(String email);

    List<MemberSummaryRes> listHighScores(int offset, int rowNum);

    List<MemberRes> findAllMembers();

}