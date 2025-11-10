package com.human.web_board.dto;

import lombok.*;

// 게시글 생성 요청 DTO
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class PostCreateReq {
    private Long memberId;
    private String title;
    private String content;
    public Long getCategory;    // 추가
}
