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

    List<PostSummaryRes> list(int offset, int rowNum);

    List<PostSummaryRes> list(Long boardId, int offset, int rowNum);

    List<PostSummaryRes> searchList(String query, int offset, int rowNum);

    List<PostSummaryRes> searchList(Long boardId, String query, int offset, int rowNum);

    List<PostSummaryRes> listPopular(int offset, int rowNum);

    List<PostSummaryRes> listRecommended(int offset, int rowNum);
}