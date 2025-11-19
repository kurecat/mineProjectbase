package com.human.web_board.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class MemberRes {
    private Long id;
    private String email;
    private String pwd;
    private String nickname;
    private String grade;
    private LocalDateTime regDate;
    private int point;
    private String profileImg;

    public String getGradeName() {
        return "관리자".equals(grade) ? "관리자" : "사용자";
    }

}
