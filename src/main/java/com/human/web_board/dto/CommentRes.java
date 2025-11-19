package com.human.web_board.dto;

import lombok.*;

import java.time.LocalDateTime;

// 댓글 조회(수정)
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class CommentRes {
    private Long id;
    private Long postId;
    private String nickname;
    private String content;
    private LocalDateTime createdAt;
    private Long memberId;

}
