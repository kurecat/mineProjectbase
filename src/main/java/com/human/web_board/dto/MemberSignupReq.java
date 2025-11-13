package com.human.web_board.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberSignupReq {
    private String email;
    private String pwd;
    private String nickname;  // name → nickname으로 변경
    private String grade;     // grade 추가
}

