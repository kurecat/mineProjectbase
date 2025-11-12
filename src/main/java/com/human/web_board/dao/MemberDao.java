package com.human.web_board.dao;

import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.dto.MemberRes;
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

// DB와 정보를 주고 받기 위한 SQL 작성 구간
@Repository     //  Spring Container 에 Bean 객체 등록, 싱글톤 객체가 됨
@RequiredArgsConstructor  // 생성자를 통한 의존성 주입을 하기 위해서 사용
@Slf4j  // log 메세지 출력을 지원하기 위한  lombok 기능
public class MemberDao {
    private final JdbcTemplate jdbc;  // JdbcTemplate을 의존성 주입 받음

    // 회원 가입
    public Long save(MemberSignupReq m) {
        @Language("SQL")
        String sql = "INSERT INTO member(id, email, pwd, name) VALUES (seq_member.NEXTVAL, ?, ?, ?)";
        jdbc.update(sql, m.getEmail(), m.getPwd(), m.getName());
        return jdbc.queryForObject("SELECT seq_member.CURRVAL FROM dual", Long.class);  // Long 타입의 id를 반환
    }

    // 이메일로 회원 조회
    public MemberRes findByEmail(String email) {
        @Language("SQL")
        String sql = "SELECT * FROM member WHERE email=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), email);
        return list.isEmpty() ? null : list.get(0); // 조회 시 결과가 없는 경우 null을 넣기 위해서
    }

    // ID로 회원 조회
    public MemberRes findById(Long id) {
        @Language("SQL")
        String sql = "SELECT * FROM member WHERE id=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), id);
        return list.isEmpty() ? null : list.get(0); // 조회 시 결과가 없는 경우 null을 넣기 위해서
    }

    // 전체 회원 조회
    public List<MemberRes> findAll() {
        @Language("SQL")
        String sql = "SELECT * from member ORDER BY id DESC";
        return jdbc.query(sql, new MemberRowMapper());
    }

    public MemberRes delete(Long id){
        @Language("SQL")
        String sql = "delete from member where id=?";
        return jdbc.queryForObject(sql, new MemberRowMapper());
    }

    public List<MemberSummaryRes> findHighScores(int offset, int rowNum) {
        @Language("SQL")
        String sql = """
            select NICKNAME, POINT
            from members
            where ROWNUM BETWEEN ? and ?
            ORDER By POINT DESC
        """;
        return jdbc.query(sql, new MemberSummaryResRowMapper(), offset, rowNum);
    }

    // Mapper 메서드  DB -> Member
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
