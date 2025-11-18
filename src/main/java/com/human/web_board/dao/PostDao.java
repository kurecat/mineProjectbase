package com.human.web_board.dao;

import com.human.web_board.dto.PostCreateReq;
import com.human.web_board.dto.PostRes;
import com.human.web_board.dto.PostSummaryRes;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostDao {
    private final JdbcTemplate jdbc;

    // 게시글 등록(수정)
    public Long save(PostCreateReq p) {
        @Language("SQL")
        String sql = "INSERT INTO posts(id,member_id,title,content,category_id,view_count,recommendations_count,created_at) VALUES (posts_seq.NEXTVAL, ?, ?, ?,?,?,?,?)";
        jdbc.update(sql, p.getMember_Id(), p.getTitle(), p.getContent(), p.getCategory_id(), 0, 0, LocalDateTime.now());
        return jdbc.queryForObject("SELECT posts_seq.CURRVAL FROM dual", Long.class);
    }

    // 게시글 목록 보기
    public List<PostRes> findAll() {
        @Language("SQL")
        String sql = """
                SELECT p.id,
                       p.title,
                       m.nickname,
                       p.view_count,
                       p.created_at
                FROM posts p
                JOIN members m ON p.member_id = m.id
                ORDER BY p.id DESC
                """;
        return jdbc.query(sql, new PostListMapper());
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

    // id로 모든 게시글 정보 가져 오기(수정)
    public PostRes findById(Long id) {
        @Language("SQL")
        String sql = """
                SELECT
                    p.id,
                    p.member_id,
                    m.nickname,
                    p.title,
                    p.content,
                    p.category_id,
                    p.view_count,
                    p.recommendations_count,
                    p.created_at
                FROM posts p
                JOIN members m ON p.member_id = m.id
                WHERE p.id = ?
                """;

        List<PostRes> list = jdbc.query(sql, new PostResPowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    // 전체 게시판에서 검색

    public List<PostSummaryRes> findSummaries(
            Long mainCategoryId,
            Long categoryId,
            String query,
            int offset,
            int rowNum
    ) {
        StringBuilder sql = new StringBuilder("""
                SELECT * FROM (
                    SELECT ROWNUM AS rn, inner_query.*
                    FROM (
                        SELECT p.id,
                               c.name AS category_name,
                               p.title,
                               m.NICKNAME,
                               p.VIEW_COUNT,
                               p.RECOMMENDATIONS_COUNT,
                               p.CREATED_AT
                        FROM POSTS p
                        JOIN members m ON p.member_id = m.id
                        JOIN CATEGORY c ON p.CATEGORY_ID = c.id
                        JOIN MAIN_CATEGORY mc ON c.main_category_id = mc.id
                        WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        if (mainCategoryId != null) {
            sql.append(" AND c.MAIN_CATEGORY_ID = ?");
            params.add(mainCategoryId);
        } else if (categoryId != null) {
            sql.append(" AND p.CATEGORY_ID = ?");
            params.add(categoryId);
        }

        if (query != null && !query.isBlank()) {
            sql.append(" AND p.TITLE LIKE ?");
            params.add("%" + query + "%");
        }

        sql.append("""
                        ORDER BY p.id DESC
                    ) inner_query
                    WHERE ROWNUM <= ?
                )
                WHERE rn > ?
                """);

        params.add(offset + rowNum);
        params.add(offset);

        return jdbc.query(sql.toString(), new PostSummaryResRowMapper(), params.toArray());
    }

    public List<PostSummaryRes> findPopular(int offset, int rowNum) {
        @Language("SQL")
        String sql = """
                SELECT p.id,
                       c.name AS category_name,
                       p.title,
                       m.NICKNAME,
                       p.VIEW_COUNT,
                       p.RECOMMENDATIONS_COUNT,
                       p.CREATED_AT
                FROM POSTS p
                JOIN members m ON p.member_id = m.id
                JOIN CATEGORY c ON p.CATEGORY_ID = c.id
                WHERE ROWNUM BETWEEN ? and ?
                ORDER BY p.VIEW_COUNT DESC
                """;
        return jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                offset,
                offset + rowNum
        );
    }

    public List<PostSummaryRes> findRecommended(int offset, int rowNum) {
        @Language("SQL")
        String sql = """
                SELECT p.id,
                       c.name AS category_name,
                       p.title,
                       m.NICKNAME,
                       p.VIEW_COUNT,
                       p.RECOMMENDATIONS_COUNT,
                       p.CREATED_AT
                FROM POSTS p
                JOIN members m ON p.member_id = m.id
                JOIN CATEGORY c ON p.CATEGORY_ID = c.id
                WHERE ROWNUM BETWEEN ? and ?
                ORDER BY p.RECOMMENDATIONS_COUNT DESC
                """;
        return jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                offset,
                offset + rowNum
        );
    }

    // 조회수 증가
    public void increaseViewCount(Long postId) {
        String sql = "UPDATE posts SET view_count = view_count + 1 WHERE id = ?";
        jdbc.update(sql, postId);
    }

    // 추천수 증가
    public void increaseRecommendationsCount(Long postId) {
        String sql = "UPDATE posts SET recommendations_count = recommendations_count + 1 WHERE id = ?";
        jdbc.update(sql, postId);
    }

    // 추천수 조회
    public int getRecommendationsCount(Long postId) {
        String sql = "SELECT recommendations_count FROM posts WHERE id = ?";
        return jdbc.queryForObject(sql, Integer.class, postId);
    }

    static class PostResPowMapper implements RowMapper<PostRes> {
        @Override
        public PostRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostRes post = new PostRes();
            post.setId(rs.getLong("id"));
            post.setMemberId(rs.getLong("member_id"));
            post.setTitle(rs.getString("title"));
            post.setContent(rs.getString("content"));
            post.setCategoryId(rs.getLong("category_id"));
            post.setViewCount(rs.getLong("view_count"));
            post.setRecommendationsCount(rs.getLong("recommendations_count"));
            post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            post.setNickname(rs.getString("nickname"));
            return post;
        }
    }

    static class PostListMapper implements RowMapper<PostRes> {
        @Override
        public PostRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            PostRes post = new PostRes();
            post.setId(rs.getLong("id"));
            post.setTitle(rs.getString("title"));
            post.setNickname(rs.getString("nickname"));
            post.setViewCount(rs.getLong("view_count"));
            post.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
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
    // ==================================================================
    // [수정된 메소드]
    // AJAX용 (String category) findSummaries
    // ==================================================================
    public List<PostSummaryRes> findSummaries(String category, int offset, int rowNum) {
        StringBuilder sql = new StringBuilder();

        // 파라미터를 순서대로 담을 List 생성
        List<Object> params = new ArrayList<>();

        // 1. SQL문 조립 (Oracle 페이지네이션)
        sql.append("SELECT * FROM ( ");
        sql.append("  SELECT ROWNUM AS rn, inner_query.* FROM ( ");
        sql.append("    SELECT p.id, c.name AS category_name, p.title, m.NICKNAME, ");
        sql.append("           p.VIEW_COUNT, p.RECOMMENDATIONS_COUNT, p.CREATED_AT ");
        sql.append("    FROM POSTS p ");

        // [수정] JOIN members m ON p.member_id = m.id (다른 쿼리와 통일)
        sql.append("    JOIN members m ON p.member_id = m.id ");

        sql.append("    JOIN CATEGORY c ON p.CATEGORY_ID = c.id ");
        sql.append("    JOIN MAIN_CATEGORY mc ON c.main_category_id = mc.id ");
        sql.append("    WHERE 1=1 ");

        // [수정] 필터 기준을 c.name -> mc.NAME 으로 변경 (예: '꿀팁')
        if (category != null && !category.isEmpty()) {
            sql.append(" AND mc.NAME = ? "); // MAIN_CATEGORY의 이름으로 검색
            params.add(category);            // 파라미터 List에 값 추가
        }

        // [수정] ORDER BY p.id DESC (최신순 정렬)
        sql.append("    ORDER BY p.id DESC ");

        sql.append("  ) inner_query ");
        sql.append("  WHERE ROWNUM <= ? "); // 페이지 처리 파라미터
        sql.append(") WHERE rn > ? ");      // 페이지 처리 파라미터

        // [수정] 파라미터 순서 및 변수 적용
        params.add(offset + rowNum); // endRow
        params.add(offset);          // startRow

        // 3. 쿼리 실행
        // [수정] PostSummaryResRowMapper 사용
        return jdbc.query(
                sql.toString(),           // 동적으로 완성된 SQL문
                new PostSummaryResRowMapper(),
                params.toArray()          // 파라미터 List를 배열로 변환
        );
    }

    public int countSummaries(String category) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM POSTS p ");
        sql.append("JOIN CATEGORY c ON p.CATEGORY_ID = c.id ");
        sql.append("JOIN MAIN_CATEGORY mc ON c.main_category_id = mc.id ");
        sql.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            sql.append(" AND mc.NAME = ? ");
            params.add(category);
        }

        return jdbc.queryForObject(sql.toString(), Integer.class, params.toArray());
    }
}
