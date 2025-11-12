package com.human.web_board.dao;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.dto.MemberSummaryRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberDao {

    private final JdbcTemplate jdbc;

    // 회원 가입
    public Long save(MemberSignupReq m) {
        @Language("SQL")
        String sql = "INSERT INTO member(id, email, pwd, name) VALUES (seq_member.NEXTVAL, ?, ?, ?)";
        jdbc.update(sql, m.getEmail(), m.getPwd(), m.getName());
        return jdbc.queryForObject("SELECT seq_member.CURRVAL FROM dual", Long.class);
    }

    // 이메일 조회
    public MemberRes findByEmail(String email) {
        @Language("SQL")
        String sql = "SELECT * FROM member WHERE email=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), email);
        return list.isEmpty() ? null : list.get(0);
    }

    // ID 조회
    public MemberRes findById(Long id) {
        @Language("SQL")
        String sql = "SELECT * FROM member WHERE id=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    // 전체 회원 조회
    public List<MemberRes> findAll() {
        @Language("SQL")
        String sql = "SELECT * FROM member ORDER BY id DESC";
        return jdbc.query(sql, new MemberRowMapper());
    }

    // 회원 삭제
    public boolean delete(Long id) {
        @Language("SQL")
        String sql = "DELETE FROM member WHERE id=?";
        int result = jdbc.update(sql, id); // 삭제된 행 수 반환
        return result > 0;
    }

    // 회원 수정
    public boolean update(MemberSignupReq req, Long id) {
        @Language("SQL")
        String sql = "UPDATE member SET email=?, pwd=?, name=? WHERE id=?";
        int result = jdbc.update(sql, req.getEmail(), req.getPwd(), req.getName(), id);
        return result > 0;
    }

    // Mapper
    static class MemberRowMapper implements RowMapper<MemberRes> {
        @Override
        public MemberRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberRes(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("pwd"),
                    rs.getString("name"),
                    rs.getTimestamp("reg_date").toLocalDateTime()
            );
        }
    }

    static class MemberSummaryResRowMapper implements RowMapper<MemberSummaryRes> {
        @Override
        public MemberSummaryRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberSummaryRes(
                    rs.getString("nickname"),
                    rs.getLong("point")
            );
        }
    }
}
