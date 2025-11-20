package com.human.web_board.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
@Table(name = "posts")
public class Posts {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ★ 중요: DTO에는 memberId(숫자)였지만, Entity는 Member(객체)로 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT") // 내용은 기니까 TEXT 타입
    private String content;

    private Long mainCategoryId; // 카테고리 ID

    @Transient
    private String categoryName; // 카테고리 이름 (단순 저장을 위해 필드 추가)

    private Long viewCount = 0L; // 조회수 (기본값 0)
    private Long recommendationsCount = 0L; // 추천수 (기본값 0)

    @CreationTimestamp // INSERT 시 시간 자동 저장
    private LocalDateTime createdAt;
}