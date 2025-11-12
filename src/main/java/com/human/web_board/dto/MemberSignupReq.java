package com.human.web_board.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class MemberSignupReq {
    private String email;
    private String pwd;
    private String name;
}
