package com.human.web_board.dto;

import com.human.web_board.dto.PostSummaryRes;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PostSummaryRowMapper implements RowMapper<PostSummaryRes> {

    @Override
    public PostSummaryRes mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostSummaryRes res = new PostSummaryRes();
        res.setId(rs.getLong("id"));
        res.setTitle(rs.getString("title"));
        res.setCategoryName(rs.getString("categoryName"));
        res.setNickname(rs.getString("nickname"));
        res.setViewCount(rs.getLong("view_count"));
        res.setRecommendationsCount(rs.getLong("recommendations_count"));
        res.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return res;
    }
}
