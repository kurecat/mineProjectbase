package com.human.web_board.dto;

import lombok.*;

import java.time.LocalDateTime;

// 게시글 응답
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PostSummaryRes {
    private Long id;
    private String title;
    private String nickname;  // 회원 테이블에 있는 정보를 조인해서 가져 옴
    private Long view_count;
    private Long recommendationsCount;
    private LocalDateTime createdAt;
}
