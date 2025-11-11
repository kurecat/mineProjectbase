package com.human.web_board.dao;

import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;
import com.human.web_board.dto.PostSummaryRes;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import org.intellij.lang.annotations.Language;

@Repository
@RequiredArgsConstructor
public class PostDao {
    private final JdbcTemplate jdbc;

    // id로 게시글 가져 오기(수정)
    public PostRes findById(Long id) {
        @Language("SQL")
        String sql = """
        SELECT p.id, p.title, p.content, p.created_at
        FROM posts p JOIN members m ON p.member_id = m.id
        WHERE p.id =?
        """;
        List<PostRes> list = jdbc.query(sql, new PostResPowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    // 전체 게시글 리스트 가져오기
    public List<PostSummaryRes> findAll(int offset, int rowNum) {
        @Language("SQL")
        String sql = """
                SELECT p.id, c.name AS category_name, p.title, m.NICKNAME, p.VIEW_COUNT, p.RECOMMENDATIONS_COUNT, p.CREATED_AT
                  FROM POSTS p
                  JOIN members m ON p.member_id = m.id
                  JOIN CATEGORY c ON p.CATEGORY_ID = c.id
                  WHERE ROWNUM BETWEEN ? and ?
                  ORDER BY p.id DESC
        """;
        List<PostSummaryRes> postSummaryResList = jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                offset,
                offset + rowNum);
        if (postSummaryResList.isEmpty()) throw new IllegalArgumentException("0개 행이 반환되었습니다");
        return postSummaryResList;
    }

    // 게시판별 게시글 리스트 가져오기
    public List<PostSummaryRes> findByCategoryId(Long categoryId, int offset, int rowNum) {
        @Language("SQL")
        String sql = """
                SELECT p.id, c.name AS category_name, p.title, m.NICKNAME, p.VIEW_COUNT, p.RECOMMENDATIONS_COUNT, p.CREATED_AT
                  FROM POSTS p
                  JOIN members m ON p.member_id = m.id
                  JOIN CATEGORY c ON p.CATEGORY_ID = c.id
                  WHERE p.CATEGORY_ID = ? and ROWNUM BETWEEN ? and ?
                  ORDER BY p.id DESC
                """;
        List<PostSummaryRes> postSummaryResList = jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                categoryId,
                offset,
                offset + rowNum);
        if (postSummaryResList.isEmpty()) throw new IllegalArgumentException("0개 행이 반환되었습니다");
        return postSummaryResList;
    }

    // 전체 게시판에서 검색
    public List<PostSummaryRes> findByQuery(String query, int offset, int rowNum) {
        String sql = """
                SELECT p.id, c.name AS category_name, p.title, m.NICKNAME, p.VIEW_COUNT, p.RECOMMENDATIONS_COUNT, p.CREATED_AT
                  FROM POSTS p
                  JOIN members m ON p.member_id = m.id
                  JOIN CATEGORY c ON p.CATEGORY_ID = c.id
                  WHERE ROWNUM BETWEEN ? and ? and p.TITLE like '%?%'
                  ORDER BY p.id DESC
                """;
        List<PostSummaryRes> postSummaryResList = jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                offset,
                offset + rowNum,
                query);
        if (postSummaryResList.isEmpty()) throw new IllegalArgumentException("0개 행이 반환되었습니다");
        return postSummaryResList;
    }

    // 게시판별 검색
    public List<PostSummaryRes> findByCategoryIdAndQuery(Long categoryId, String query, int offset, int rowNum) {
        String sql = """
                SELECT p.id, c.name AS category_name, p.title, m.NICKNAME, p.VIEW_COUNT, p.RECOMMENDATIONS_COUNT, p.CREATED_AT
                  FROM POSTS p
                  JOIN members m ON p.member_id = m.id
                  JOIN CATEGORY c ON p.CATEGORY_ID = c.id
                  WHERE p.CATEGORY_ID = ? and ROWNUM BETWEEN ? and ? and p.TITLE like '%?%'
                  ORDER BY p.id DESC
                """;
        List<PostSummaryRes> postSummaryResList = jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                categoryId,
                offset,
                offset + rowNum,
                query);
        if (postSummaryResList.isEmpty()) throw new IllegalArgumentException("0개 행이 반환되었습니다");
        return postSummaryResList;
    }

    // 게시글 등록(수정)
    public Long save(PostCreateReq p) {
        @Language("SQL")
        String sql = "INSERT INTO posts(id,member_id,title,content,category_id,view_count,recommendations_count,created_at) VALUES (posts_seq.NEXTVAL, ?, ?, ?,?,?,?,?)";
        jdbc.update(sql, p.getMember_Id(), p.getTitle(), p.getContent(),p.getCategory_id(),0,0, LocalDateTime.now());
        return jdbc.queryForObject("SELECT posts_seq.CURRVAL FROM dual", Long.class);
    }

    // 게시글 수정(수정)
    public boolean update(Long id, String title, String content) {
        @Language("SQL")
        String sql = "UPDATE posts SET title=?, content=? WHERE id=?";
        return jdbc.update(sql, title, content, id) > 0;
    }

    // 게시글 삭제
    public boolean delete(Long id) {
        @Language("SQL")
        String sql = "DELETE FROM posts WHERE id=?";
        return jdbc.update(sql, id) > 0;
    }

    // mapper 메서드 생성(수정)
    static class PostResPowMapper implements RowMapper<PostRes> {
        @Override
        public PostRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PostRes(
                    rs.getLong("id"),
                    rs.getLong("member_id"),
                    rs.getString("title"),
                    rs.getString("content"),
                    rs.getLong("category_id"),
                    rs.getLong("view_count"),
                    rs.getLong("recommendations_count"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }
    static class PostListMapper implements RowMapper<PostRes> {
        @Override
        public PostRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostRes post = new PostRes();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setView_count(rs.getLong("view_count"));
            post.setCreated_at(rs.getTimestamp("created_at").toLocalDateTime());
            return post;
        }
    }

    static class PostSummaryResRowMapper implements RowMapper<PostSummaryRes> {
        @Override
        public PostSummaryRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PostSummaryRes(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("category_name"),
                    rs.getString("nickname"),
                    rs.getLong("view_count"),
                    rs.getLong("recommendations_count"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }
}


