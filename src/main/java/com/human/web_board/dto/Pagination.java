// src/main/java/com/human/web_board/dto/Pagination.java (새 파일)
package com.human.web_board.dto;

import lombok.Getter;

@Getter
public class Pagination {
    private int totalCount;     // 전체 게시물 수
    private int rowNum;         // 한 페이지에 보여줄 게시물 수 (예: 10)
    private int offset;         // 현재 offset (예: 0, 10, 20)
    private int totalPage;      // 총 페이지 수
    private int currentPage;    // 현재 페이지 (1, 2, 3...)

    private int pageBlock = 5;  // 한 번에 보여줄 페이지 번호 개수 (예: [1 2 3 4 5])
    private int startPage;      // 페이지 블록의 시작 번호
    private int endPage;        // 페이지 블록의 끝 번호

    private boolean isFirst;    // 이전 버튼 (<<)
    private boolean isLast;     // 다음 버튼 (>>)
    private int prevOffset;
    private int nextOffset;

    public Pagination(int totalCount, int rowNum, int offset) {
        this.totalCount = totalCount;
        this.rowNum = rowNum;
        this.offset = offset;

        // 1. 총 페이지 수 계산
        this.totalPage = (int) Math.ceil((double) totalCount / rowNum);

        // 2. 현재 페이지 계산 (offset 기준)
        this.currentPage = (offset / rowNum) + 1;

        // 3. 페이지 블록 계산
        this.startPage = ((currentPage - 1) / pageBlock) * pageBlock + 1;
        this.endPage = Math.min(startPage + pageBlock - 1, totalPage);

        // 4. 이전/다음 버튼
        this.isFirst = (startPage == 1);
        this.isLast = (endPage == totalPage);

        // 5. 이전/다음 블록 offset 계산
        // offset은 0보다 작을 수 없음
        this.prevOffset = Math.max(0, offset - (pageBlock * rowNum));
        // offset은 최대 (totalPage - 1) * rowNum 값을 가짐
        this.nextOffset = Math.min(offset + (pageBlock * rowNum), (totalPage - 1) * rowNum);
    }
}