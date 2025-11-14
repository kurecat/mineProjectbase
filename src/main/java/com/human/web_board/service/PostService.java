package com.human.web_board.service;

import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;
import com.human.web_board.dto.PostSummaryRes;

import java.util.List;

public interface PostService {
    // 게시글 등록
    Long write(PostCreateReq req);
    // 게시글 목록
    List<PostRes> list();
    // 개별 게시글 보기 (게시글 ID)
    PostRes get(Long id);
    // 게시글 수정
    boolean edit(PostCreateReq req, Long id);
    // 게시글 삭제
    boolean delete(Long id);

    List<PostSummaryRes> listSummaries(Long mainCategoryId,
                                       Long categoryId,
                                       String query,
                                       int offset,
                                       int rowNum);

    List<PostSummaryRes> listPopular(int offset, int rowNum);

    List<PostSummaryRes> listRecommended(int offset, int rowNum);
    // 조회수, 추천수 증가 기능
    void increaseView(Long postId);
    int increaseRecommendations(Long postId);

    List<PostSummaryRes> searchList(String query, int i, int i1);

}