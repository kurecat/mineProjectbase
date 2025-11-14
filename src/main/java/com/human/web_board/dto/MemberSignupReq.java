package com.human.web_board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignupReq {
    private int grade = 1; // 기본값 1 grade 추가
    private String email;
    private String pwd;
    private String nickname;  // name → nickname으로 변경
    private String profileImg; // 프로필 이미지 추가
}

