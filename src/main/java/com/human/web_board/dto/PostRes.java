package com.human.web_board.dto;

import com.human.web_board.Entity.Posts;
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

    public PostRes(Posts post) {
        this.id = post.getId();
        if (post.getMember() != null) {
            this.memberId = post.getMember().getId();
            this.nickname = post.getMember().getNickname();
            this.name = post.getMember().getNickname(); // 일단 닉네임과 동일하게 설정
        }
        this.title = post.getTitle();
        this.content = post.getContent();
        this.mainCategoryId = post.getMainCategoryId();
        this.categoryName = post.getCategoryName();
        this.viewCount = post.getViewCount();
        this.recommendationsCount = post.getRecommendationsCount();
        this.createdAt = post.getCreatedAt();

        this.mainCategoryId = post.getMainCategoryId();

        if (post.getMainCategoryId() == 1L) {
            this.categoryName = "꿀팁";
        } else if (post.getMainCategoryId() == 2L) {
            this.categoryName = "질문";
        } else if (post.getMainCategoryId() == 3L) {
            this.categoryName = "후기";
        } else {
            this.categoryName = "잡담";
        }
    }
}
