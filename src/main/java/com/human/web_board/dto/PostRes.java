package com.human.web_board.dto;

import lombok.*;

import java.time.LocalDateTime;

// 게시글 응답
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PostRes {
    private Long id;    //PK
    private Long memberId;  // FK
    private String title;
    private String content;
    private Long mainCategoryId;
    private String categoryName;
    private Long viewCount;
    private Long recommendationsCount;
    private LocalDateTime createdAt;
    private String nickname;
    private String name;
}
