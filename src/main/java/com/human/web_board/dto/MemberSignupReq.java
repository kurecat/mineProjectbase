package com.human.web_board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignupReq {
    private int grade = 1;
    private String email;
    private String pwd;
    private String passwordCheck; // 추가 비밀번호 검증
    private String nickname;  // name → nickname으로 변경
    private String profileImg; // 프로필 이미지 추가
}

