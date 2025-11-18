package com.human.web_board.service;

import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostFormDto;
import com.human.web_board.dto.PostRes;
import com.human.web_board.dto.PostSummaryRes;

import java.util.List;

public interface PostService {

    // 게시글 저장 (글쓰기)
    Long save(PostFormDto form);

    // 게시글 전체 목록
    List<PostRes> findAll();

    // 기존 기능 ↓
    Long write(PostCreateReq req);

    List<PostRes> list();

    PostRes get(Long id);

    boolean edit(PostCreateReq req, Long id);

    boolean delete(Long id);

    List<PostSummaryRes> listSummaries(Long mainCategoryId,
                                       Long categoryId,
                                       String query,
                                       int offset,
                                       int rowNum);

    List<PostSummaryRes> listPopular(int offset, int rowNum);

    List<PostSummaryRes> listRecommended(int offset, int rowNum);

    void increaseView(Long postId);

    int increaseRecommendations(Long postId);

    List<PostSummaryRes> searchList(String query, int i, int i1);

    List<PostSummaryRes> listSummaries(String category, int offset, int rowNum);

    int countSummaries(String category);
}

