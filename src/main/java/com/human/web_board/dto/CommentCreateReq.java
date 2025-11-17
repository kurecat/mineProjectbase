package com.human.web_board.dto;

import lombok.*;

// 댓글 생성
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CommentCreateReq {
    private Long postId;
    private Long memberId;
    private String content;
}
