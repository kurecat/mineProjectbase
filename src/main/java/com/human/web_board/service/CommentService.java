package com.human.web_board.service;

import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;

import java.util.List;

public interface CommentService {
    // 댓글 쓰기
    Long write(CommentCreateReq req);

    // 댓글 목록 조회
    List<CommentRes> list(Long postId);

    // 댓글 수정
    boolean update(CommentCreateReq req, Long id);

    // 댓글 삭제
    boolean delete(Long id);

    // id로 댓글찾기
    CommentRes findById(Long id);


}

