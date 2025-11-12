package com.human.web_board.service.impl;

import com.human.web_board.dao.MemberDao;
import com.human.web_board.dao.PostDao;
import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;
import com.human.web_board.dto.PostSummaryRes;
import com.human.web_board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostServiceImpl implements PostService {
    private final PostDao postDao;

    private final MemberDao memberDao;
    @Override
    @Transactional
    public Long write(PostCreateReq req) {
        if (memberDao.findById(req.getMember_Id()) == null) {
            throw new IllegalArgumentException("존재하지 않는 회원 입니다.");
        }
        return postDao.save(req);
    }

    @Override
    public PostRes get(Long id) {
        return postDao.findById(id);
    }

    @Override
    public boolean edit(PostCreateReq req, Long id) {
        return postDao.update(id, req.getTitle(), req.getContent());
    }

    @Override
    public boolean delete(Long id) {
        try {
            if (!postDao.delete(id)) {
                throw new IllegalArgumentException("게시글을 삭제 할 수 없습니다.");
            }
            return true;
        } catch (DataAccessException e) {
            log.error("게시글 삭제 예외 발생: {}", e.getCause());
            throw new IllegalArgumentException("게시글을 삭제 할 수 없습니다.");
        }
    }

    @Override
    public List<PostSummaryRes> list(int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findAll(offset, rowNum);
        } catch (IllegalArgumentException e) {
            log.error("게시판 불러오기 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }

    @Override
    public List<PostSummaryRes> list(Long boardId, int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findByCategoryId(boardId, offset, rowNum);
        } catch (IllegalArgumentException e) {
            log.error("게시판 불러오기 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }

    @Override
    public List<PostSummaryRes> searchList(String query, int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findByQuery(query, offset, rowNum);
        } catch (IllegalArgumentException e) {
            log.error("게시글 검색 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }

    @Override
    public List<PostSummaryRes> searchList(Long boardId, String query, int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findByCategoryIdAndQuery(boardId, query, offset, rowNum);
        } catch (IllegalArgumentException e) {
            log.error("게시글 검색 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }

    @Override
    public List<PostSummaryRes> listPopular(int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findPopular(offset, rowNum);
        } catch (IllegalArgumentException e) {
            log.error("게시판 불러오기 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }

    @Override
    public List<PostSummaryRes> listRecommended(int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findRecommended(offset, rowNum);
        } catch (IllegalArgumentException e) {
            log.error("게시판 불러오기 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }

}
