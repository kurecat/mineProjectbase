package com.human.web_board.dto;

import lombok.*;

// 댓글 생성
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class LoginMember {
    private Long id;
    private String email;
    private String pwd;
    private String nickname;
}
