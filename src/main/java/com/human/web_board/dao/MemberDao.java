package com.human.web_board.dao;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import lombok.RequiredArgsConstructor;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberDao {
    private final JdbcTemplate jdbc;

    // 회원 가입
    public Long save(MemberSignupReq m) {
        @Language("SQL")
        String sql = "INSERT INTO members(id, email, pwd, nickname, grade, reg_date, point) " +
                "VALUES (members_seq.NEXTVAL, ?, ?, ?, 1, SYSDATE, 0)";
        jdbc.update(sql, m.getEmail(), m.getPwd(), m.getName());
        return jdbc.queryForObject("SELECT members_seq.CURRVAL FROM dual", Long.class);
    }

    // 회원 조회
    public MemberRes findByEmail(String email) {
        @Language("SQL")
        String sql = "SELECT * FROM members WHERE email=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), email);
        return list.isEmpty() ? null : list.get(0);
    }

    public MemberRes findById(Long id) {
        @Language("SQL")
        String sql = "SELECT * FROM members WHERE id=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public List<MemberRes> findAll() {
        @Language("SQL")
        String sql = "SELECT * FROM members ORDER BY id DESC";
        return jdbc.query(sql, new MemberRowMapper());
    }

    // 회원 삭제
    public boolean delete(Long id){
        @Language("SQL")
        String sql = "DELETE FROM members WHERE id=?";
        return jdbc.update(sql, id) > 0;
    }

    // 회원 수정
    public boolean update(MemberSignupReq m, Long id){
        @Language("SQL")
        String sql = "UPDATE members SET email=?, pwd=?, nickname=? WHERE id=?";
        return jdbc.update(sql, m.getEmail(), m.getPwd(), m.getName(), id) > 0;
    }

    // Mapper
    static class MemberRowMapper implements RowMapper<MemberRes> {
        @Override
        public MemberRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberRes(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("pwd"),
                    rs.getString("nickname"),
                    rs.getTimestamp("reg_date").toLocalDateTime()
            );
        }
    }
}
