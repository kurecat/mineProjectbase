package com.human.web_board.dto;

// (Lombok을 사용하지 않는다면, @Getter/@Setter 대신 직접 getter/setter 메소드를 생성해야 합니다)
import lombok.Getter;
import lombok.Setter;

/**
 * write.html 폼과 컨트롤러 간의 데이터 전송을 위한 DTO (Data Transfer Object)
 */
@Getter
@Setter
public class PostFormDto {

    // <input type="text" th:field="*{title}"> 와 연결
    private String title;

    // <textarea th:field="*{content}"> 와 연결
    private String content;

    // <input type="checkbox" th:field="*{allowComments}"> 와 연결
    private boolean allowComments;

    // <input type="checkbox" th:field="*{extraOption}"> 와 연결
    private boolean extraOption;

    // (참고: 폰트 크기 드롭다운(name="fontSize")도 DTO로 받으려면
    // 여기에 private String fontSize; 필드를 추가하고
    // write.html의 <select>에 th:field="*{fontSize}"를 추가하면 됩니다.)

    // 기본값 설정 (선택 사항)
    public PostFormDto() {
        this.allowComments = true; // '댓글 허용'이 기본으로 체크되도록 설정
    }
}