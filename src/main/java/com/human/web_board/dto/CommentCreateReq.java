package com.human.web_board.dto;

import lombok.*;

import java.sql.Timestamp;

// 댓글 생성
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CommentCreateReq {
    private Long postId;
    private Long memberId;
    private String content;
    private Timestamp create_at;
}
