package com.human.web_board.service.impl;

import com.human.web_board.dao.MemberDao;
import com.human.web_board.dao.PostDao;
import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostFormDto;
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
    public Long save(PostFormDto form) {
        // PostFormDto → PostCreateReq 로 변환
        PostCreateReq req = new PostCreateReq();
        req.setTitle(form.getTitle());
        req.setContent(form.getContent());
        req.setMemberId(form.getMemberId()); // 로그인 사용자
        req.setMainCategoryId(form.getCategoryId()); // 카테고리 select 값
//        req.setAllowComments(form.isAllowComments());
//        req.setExtraOption(form.isExtraOption());

        // 저장
        return postDao.save(req);
    }

    @Override
    public List<PostRes> findAll() {
        return List.of();
    }

    @Override
    @Transactional
    public Long write(PostCreateReq req) {
        if (memberDao.findById(req.getMemberId()) == null) {
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

    @Override
    public List<PostSummaryRes> listSummaries(Long mainCategoryId, Long categoryId, String query, int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findSummaries(mainCategoryId, categoryId, query, offset, rowNum);
        } catch (DataAccessException e) {
            log.error("게시판 불러오기 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }

    @Override
    public List<PostSummaryRes> listPopular(int offset, int rowNum) {
        List<PostSummaryRes> res;
        try {
            res = postDao.findPopular(offset, rowNum);
        } catch (DataAccessException e) {
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
        } catch (DataAccessException e) {
            log.error("게시판 불러오기 에러 발생: {}", e);
            res = Collections.emptyList();
        }
        return res;
    }
    @Override
    public void increaseView(Long postId) {
        postDao.increaseViewCount(postId);
    }

    @Override
    public int increaseRecommendations(Long postId) {
        postDao.increaseRecommendationsCount(postId);
        return postDao.getRecommendationsCount(postId);
    }

    @Override
    public List<PostSummaryRes> searchList(String query, int offset, int rowNum) {
        return postDao.findSummaries(null, null, query, offset, rowNum);
    }

    @Override
    public List<PostSummaryRes> listSummaries(String category, int offset, int rowNum) {
        try {
            // [수정] DAO 호출 시 3개의 파라미터 전달 (이 부분이 117번째 줄 근처)
            return postDao.findSummaries(category, offset, rowNum);
        } catch (Exception e) {
            log.error("게시판 불러오기 에러 발생 (category: {}): {}", category, e.getMessage());
            return Collections.emptyList(); // 에러 시 빈 리스트 반환
        }
    }

    @Override
    public int countSummaries(String category) {
        try {
            return postDao.countSummaries(category);
        } catch (Exception e) {
            log.error("게시물 개수 세기 에러 발생 (category: {}): {}", category, e.getMessage());
            return 0;
        }
    }

    @Override
    public List<PostRes> listRecent(int limit) {
        return List.of();
    }

    @Override
    public PostRes findById(Long id) {
        return postDao.findById(id);
    }

    @Override
    public boolean update(Long id, String title, String content) {
        return postDao.update(id, title, content);
    }

    @Override
    public List<PostSummaryRes> listSummaries(Long memberId, int offset, int rowNum) {
        return postDao.findByMemberId(memberId, offset, rowNum);
    }


}
