package com.human.web_board.dto;

import lombok.*;

import java.time.LocalDateTime;

// 게시글 응답
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class MemberSummaryRes {
    private String nickname;
    private Long point;
}
