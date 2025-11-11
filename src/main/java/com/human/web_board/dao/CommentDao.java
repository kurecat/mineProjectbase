package com.human.web_board.dao;

import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.intellij.lang.annotations.Language;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentDao {
    private final JdbcTemplate jdbc;

    // 댓글 등록(수정)
    public Long save(CommentCreateReq c) {
        @Language("SQL")
        String sql = "INSERT INTO comments (id, post_id, member_id, content) VALUES (comments_seq.NEXTVAL, ?, ?, ?)";
        jdbc.update(sql, c.getPost_Id(), c.getMember_Id(), c.getContent());
        return jdbc.queryForObject("SELECT comments_seq.CURRVAL FROM dual", Long.class);
    }

    // 댓글 가져오기 (게시글 ID를 통해서 가져 와야 함)(수정)
    public List<CommentRes> findByPostId(Long postId) {
        @Language("SQL")
        String sql = """
            SELECT c.id, c.post_id, c.member_id, c.content, c.created_at 
            FROM comments c JOIN member m ON c.member_id = m.id 
            WHERE c.id = ?
            ORDER BY c.id ASC
        """;
        return jdbc.query(sql, new CommentResMapper(), postId);
    }

    // 댓글 삭제(수정)
    public boolean delete(Long id) {
        @Language("SQL")
        String sql = "DELETE FROM comments WHERE id=?";
        return jdbc.update(sql, id) > 0; // 반환값은 영향을 받는 행의 갯수
    }

    // 댓글 수정(수정)
    public boolean update(CommentCreateReq c, Long id) {
        @Language("SQL")
        String sql = " UPDATE comments SET content = ? WHERE id = ?";
        return jdbc.update(sql, c.getContent(), id) > 0;
    }

    public CommentRes findById(Long id) {
        @Language("SQL")
        String sql = """
            SELECT c.id, c.post_id, c.member_id, c.content, c.created_at 
            FROM comments c JOIN member m ON c.member_id = m.id 
            WHERE c.id = ?
        """;

        try {
            return jdbc.queryForObject(sql, new CommentResMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // mapper 메서드(수정)
    static class CommentResMapper implements RowMapper<CommentRes> {
        @Override
        public CommentRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new CommentRes(
              rs.getLong("id"),
              rs.getLong("post_id"),
              rs.getLong("member_id"),
              rs.getString("content"),
              rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }
}
