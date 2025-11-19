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

    // 게시글 등록
    public Long save(PostCreateReq p) {
        @Language("SQL")
        // 수정됨: main_category_id 컬럼에 값을 직접 저장
        String sql = "INSERT INTO posts(id, member_id, title, content, main_category_id, view_count, recommendations_count, created_at) VALUES (posts_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(sql, p.getMemberId(), p.getTitle(), p.getContent(), p.getMainCategoryId(), 0, 0, LocalDateTime.now());
        return jdbc.queryForObject("SELECT posts_seq.CURRVAL FROM dual", Long.class);
    }

    // 게시글 목록 보기 (관리자용 등 단순 목록)
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

    // 게시글 수정
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

    // id로 모든 게시글 정보 가져 오기 (상세보기)
    public PostRes findById(Long id) {
        @Language("SQL")
        String sql = """
            SELECT
                p.id,
                p.member_id,
                m.nickname,
                p.title,
                p.content,
                p.main_category_id,
                mc.name AS category_name,  -- ★ 여기 추가 (카테고리 이름 조회)
                p.view_count,
                p.recommendations_count,
                p.created_at
            FROM posts p
            JOIN members m ON p.member_id = m.id
            JOIN MAIN_CATEGORY mc ON p.main_category_id = mc.id
            WHERE p.id = ?
            """;

        List<PostRes> list = jdbc.query(sql, new PostResPowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<PostSummaryRes> findByMemberId(Long memberId, int offset, int rowNum) {
        String sql = """
                SELECT *
                  FROM (
                   SELECT ROWNUM AS RN,
                          INNER_QUERY.*
                     FROM (
                      SELECT P.ID,
                             MC.NAME AS CATEGORY_NAME,
                             P.TITLE,
                             P.MEMBER_ID,
                             M.NICKNAME,
                             P.VIEW_COUNT,
                             P.RECOMMENDATIONS_COUNT,
                             P.CREATED_AT
                        FROM POSTS P
                        JOIN MEMBERS M
                      ON P.MEMBER_ID = M.ID
                        JOIN MAIN_CATEGORY MC
                      ON P.MAIN_CATEGORY_ID = MC.ID
                      WHERE p.MEMBER_ID = ?
                       ORDER BY P.ID DESC
                   ) INNER_QUERY
                    WHERE ROWNUM <= ?
                )
                 WHERE RN > ?
                """;
        return jdbc.query(sql, new PostSummaryResRowMapper(), memberId, offset + rowNum, offset);
    }

    // [수정됨] 전체 게시판에서 검색 및 페이징 (CATEGORY 테이블 제거됨)
    public List<PostSummaryRes> findSummaries(
            Long mainCategoryId, // 이제 사용 안 함 (혹은 categoryId와 동일 취급)
            Long categoryId,     // 실제 넘어오는 MAIN_CATEGORY의 ID (1, 2, 3, 4)
            String query,
            int offset,
            int rowNum
    ) {
        StringBuilder sql = new StringBuilder("""
                SELECT * FROM (
                    SELECT ROWNUM AS rn, inner_query.*
                    FROM (
                        SELECT p.id,
                               mc.name AS category_name,
                               p.title,
                               p.MEMBER_ID,
                               m.NICKNAME,
                               p.VIEW_COUNT,
                               p.RECOMMENDATIONS_COUNT,
                               p.CREATED_AT
                        FROM POSTS p
                        JOIN members m on p.member_id = m.id
                        JOIN MAIN_CATEGORY mc on p.main_category_id = mc.id
                        WHERE 1=1
                """);

        List<Object> params = new ArrayList<>();

        // 카테고리 필터링 (category_id가 있으면 main_category_id로 검색)
        if (categoryId != null) {
            sql.append(" AND p.MAIN_CATEGORY_ID = ?");
            params.add(categoryId);
        }

        // 검색어 필터링
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

    // [수정됨] 인기글 조회 (CATEGORY 제거)
    public List<PostSummaryRes> findPopular(int offset, int rowNum) {
        @Language("SQL")
        String sql = """
                SELECT *
                   FROM (
                    SELECT ROWNUM AS RN,
                           SORTED.*
                      FROM (
                       SELECT P.ID,
                              MC.NAME AS CATEGORY_NAME,
                              P.TITLE,
                              P.MEMBER_ID,
                              M.NICKNAME,
                              P.VIEW_COUNT,
                              P.RECOMMENDATIONS_COUNT,
                              P.CREATED_AT
                         FROM POSTS P
                         JOIN MEMBERS M
                       ON P.MEMBER_ID = M.ID
                         JOIN MAIN_CATEGORY MC
                       ON P.MAIN_CATEGORY_ID = MC.ID
                        ORDER BY P.VIEW_COUNT DESC
                    ) SORTED
                     WHERE ROWNUM <= ?
                 )
                  WHERE RN > ?
                """;
        return jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                offset + rowNum,
                offset
        );
    }

    // [수정됨] 추천글 조회 (CATEGORY 제거)
    public List<PostSummaryRes> findRecommended(int offset, int rowNum) {
        @Language("SQL")
        String sql = """
                SELECT *
                   FROM (
                    SELECT ROWNUM AS RN,
                           SORTED.*
                      FROM (
                       SELECT P.ID,
                              MC.NAME AS CATEGORY_NAME,
                              P.TITLE,
                              P.MEMBER_ID,
                              M.NICKNAME,
                              P.VIEW_COUNT,
                              P.RECOMMENDATIONS_COUNT,
                              P.CREATED_AT
                         FROM POSTS P
                         JOIN MEMBERS M
                       ON P.MEMBER_ID = M.ID
                         JOIN MAIN_CATEGORY MC
                       ON P.MAIN_CATEGORY_ID = MC.ID
                        ORDER BY P.RECOMMENDATIONS_COUNT DESC
                    ) SORTED
                     WHERE ROWNUM <= ?
                 )
                  WHERE RN > ?
                """;
        return jdbc.query(
                sql,
                new PostSummaryResRowMapper(),
                offset + rowNum,
                offset
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
            post.setMainCategoryId(rs.getLong("main_category_id"));
            post.setCategoryName(rs.getString("category_name"));
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

    // [수정됨] SQL 별칭(category_name)과 매퍼 일치시킴
    static class PostSummaryResRowMapper implements RowMapper<PostSummaryRes> {
        @Override
        public PostSummaryRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new PostSummaryRes(
                    rs.getLong("id"),
                    rs.getString("title"),
                    rs.getString("category_name"), // SQL Alias가 category_name임
                    rs.getLong("member_id"),
                    rs.getString("nickname"),
                    rs.getLong("view_count"),
                    rs.getLong("recommendations_count"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }

    // ==================================================================
    // [수정됨] AJAX용 (String category) findSummaries
    // CATEGORY 테이블 제거 후 MAIN_CATEGORY 직접 연결
    // ==================================================================
    public List<PostSummaryRes> findSummaries(String category, int offset, int rowNum) {
        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * FROM ( ");
        sql.append("  SELECT ROWNUM AS rn, inner_query.* FROM ( ");
        sql.append("    SELECT p.id, mc.name AS category_name, p.title, p.MEMBER_ID, m.NICKNAME, ");
        sql.append("           p.VIEW_COUNT, p.RECOMMENDATIONS_COUNT, p.CREATED_AT ");
        sql.append("    FROM POSTS p ");
        sql.append("    JOIN members m ON p.member_id = m.id ");
        // [수정] CATEGORY JOIN 제거하고 MAIN_CATEGORY와 직접 조인
        sql.append("    JOIN MAIN_CATEGORY mc ON p.main_category_id = mc.id ");
        sql.append("    WHERE 1=1 ");

        if (category != null && !category.isEmpty()) {
            sql.append(" AND mc.NAME = ? "); // 카테고리 이름(예: '꿀팁')으로 검색
            params.add(category);
        }

        sql.append("    ORDER BY p.id DESC ");
        sql.append("  ) inner_query ");
        sql.append("  WHERE ROWNUM <= ? ");
        sql.append(") WHERE rn > ? ");

        params.add(offset + rowNum);
        params.add(offset);

        return jdbc.query(sql.toString(), new PostSummaryResRowMapper(), params.toArray());
    }

    // [수정됨] 카운트 쿼리 (CATEGORY 제거)
    public int countSummaries(String category) {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM POSTS p ");
        // [수정] CATEGORY JOIN 제거
        sql.append("JOIN MAIN_CATEGORY mc ON p.main_category_id = mc.id ");
        sql.append("WHERE 1=1 ");

        List<Object> params = new ArrayList<>();
        if (category != null && !category.isEmpty()) {
            sql.append(" AND mc.NAME = ? ");
            params.add(category);
        }

        return jdbc.queryForObject(sql.toString(), Integer.class, params.toArray());
    }
}