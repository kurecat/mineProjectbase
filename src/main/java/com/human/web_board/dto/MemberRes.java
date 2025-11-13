package com.human.web_board.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class MemberRes {
    private Long id;
    private String email;
    private String pwd;
    private String nickname;  // name → nickname
    private String grade;     // grade 추가
    private LocalDateTime regDate;
}
