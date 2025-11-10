package com.human.web_board.service.impl;

import com.human.web_board.dao.MemberDao;
import com.human.web_board.dao.PostDao;
import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;
import com.human.web_board.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public List<PostRes> list() {
        return postDao.findAll();
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
}
