package com.human.web_board.dao;

import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;
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

    // 게시글 등록(수정)
    public Long save(PostCreateReq p) {
        @Language("SQL")
        String sql = "INSERT INTO posts(id,member_id,title,content,category,view_count,recommendations_count,created_at) VALUES (seq_post.NEXTVAL, ?, ?, ?,?,?,?,?)";
        jdbc.update(sql, p.getMemberId(), p.getTitle(), p.getContent(),p.getCategory,0,0, LocalDateTime.now());
        return jdbc.queryForObject("SELECT seq_post.CURRVAL FROM dual", Long.class);
    }

    // 게시글 목록 보기(수정)
    public List<PostRes> findAll() {
        @Language("SQL")
        String sql = """
        SELECT post_id, title, view_count, created_at
        FROM posts
        ORDER BY post_id DESC
        """;
        return jdbc.query(sql, new PostResPowMapper());
    }

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
                    rs.getLong("category"),
                    rs.getLong("view_count"),
                    rs.getLong("recommendation_count"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }
}