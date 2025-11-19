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

    // 한글 권한명 자동 제공
    public String getGradeName() {
        return "ADMIN".equals(grade) ? "관리자" : "사용자";
    }
}
