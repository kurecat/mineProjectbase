package com.human.web_board.dto;

import lombok.*;

// 댓글 생성
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CommentCreateReq {
    private Long post_Id;
    private Long member_Id;
    private String content;
}
