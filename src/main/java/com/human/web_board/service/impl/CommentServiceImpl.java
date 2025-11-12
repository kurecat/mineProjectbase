package com.human.web_board.service.impl;

import com.human.web_board.dao.CommentDao;
import com.human.web_board.dao.MemberDao;
import com.human.web_board.dao.PostDao;
import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;
import com.human.web_board.service.CommentService;
import lombok.Lombok;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentDao commentDao;
    private final PostDao postDao;
    private final MemberDao memberDao;
    @Override
    @Transactional
    public Long write(CommentCreateReq req) {
        if(memberDao.findById(req.getMember_Id()) == null) {
            throw new IllegalArgumentException("존재 하지 않은 회원 입니다.");
        }
        if(postDao.findById(req.getMember_Id()) == null) {
            throw new IllegalArgumentException("존재하지 않는 게시글 입니다.");
        }
        return commentDao.save(req);
    }

    @Override
    public List<CommentRes> list(Long postId) {
        return commentDao.findByPostId(postId);
    }

    @Override
    public boolean update(CommentCreateReq req, Long id) {
        return commentDao.update(req, id);
    }

    @Override
    public boolean delete(Long id) {
        return commentDao.delete(id);
    }

    @Override
    public CommentRes findById(Long id) {
        CommentRes comment = commentDao.findById(id);

        if (comment == null) {
            throw new IllegalArgumentException("ID가 " + id + "인 댓글을 찾을 수 없습니다.");
        }
        return comment;
    }
}
