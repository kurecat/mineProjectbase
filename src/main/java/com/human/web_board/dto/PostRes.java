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
    private Long maincategoryId;  // FK(여기 나중에 수정할 수도 있음! long->string)
    private Long viewCount;
    private Long recommendationsCount;
    private LocalDateTime createdAt;
    private String nickname;
    private String name;
}
