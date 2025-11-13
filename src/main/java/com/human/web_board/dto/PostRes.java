package com.human.web_board.dto;

import lombok.*;

import java.time.LocalDateTime;

// 게시글 응답
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PostRes {
    private Long id;    //PK
    private Long member_id;  // FK
    private String title;
    private String content;
    private Long category_id;  // FK(여기 나중에 수정할 수도 있음! long->string)
    private Long view_count;
    private Long recommendations_count;
    private LocalDateTime created_at;
    private String nickname;


}
