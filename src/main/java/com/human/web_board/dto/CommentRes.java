package com.human.web_board.dto;

import com.human.web_board.Entity.Comment;
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
    private String postTitle;

    // ▼ [추가] Entity -> DTO 변환 생성자
    public CommentRes(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();

        // 작성자 정보 연결
        if (comment.getMember() != null) {
            this.memberId = comment.getMember().getId();
            this.nickname = comment.getMember().getNickname();
        }

        // 게시글 정보 연결
        if (comment.getPosts() != null) {
            this.postId = comment.getPosts().getId();
            this.postTitle = comment.getPosts().getTitle(); // ★ 여기서 제목 가져오기
        } else {
            this.postTitle = "(삭제된 게시글)";
        }
    }

}
