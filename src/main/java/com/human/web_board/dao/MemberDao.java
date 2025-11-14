package com.human.web_board.dao;

import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSummaryRes;
import com.human.web_board.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.intellij.lang.annotations.Language;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// DB와 정보를 주고 받기 위한 SQL 작성 구간
@Repository     //  Spring Container 에 Bean 객체 등록, 싱글톤 객체가 됨
@RequiredArgsConstructor  // 생성자를 통한 의존성 주입을 하기 위해서 사용
@Slf4j  // log 메세지 출력을 지원하기 위한  lombok 기능
public class MemberDao {
    private final JdbcTemplate jdbc;  // JdbcTemplate을 의존성 주입 받음

    // 회원 가입
    public Long save(MemberSignupReq m) {
        String sql = "INSERT INTO MEMBERS(ID, EMAIL, PWD, NICKNAME, GRADE, REG_DATE, POINT) "
                + "VALUES (MEMBERS_SEQ.NEXTVAL, ?, ?, ?, ?, SYSTIMESTAMP, 0)";
        jdbc.update(sql, m.getEmail(), m.getPwd(), m.getNickname(), m.getGrade());
        return jdbc.queryForObject("SELECT MEMBERS_SEQ.CURRVAL FROM dual", Long.class);
    }


    // 이메일로 회원 조회
    public MemberRes findByEmail(String email) {
        @Language("SQL")
        String sql = "SELECT * FROM members WHERE email=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), email);
        return list.isEmpty() ? null : list.get(0); // 조회 시 결과가 없는 경우 null을 넣기 위해서
    }

    // ID로 회원 조회
    public MemberRes findById(Long id) {
        @Language("SQL")
        String sql = "SELECT * FROM members WHERE id=?";
        List<MemberRes> list = jdbc.query(sql, new MemberRowMapper(), id);
        return list.isEmpty() ? null : list.get(0); // 조회 시 결과가 없는 경우 null을 넣기 위해서
    }

    // 전체 회원 조회
    public List<MemberRes> findAll() {
        @Language("SQL")
        String sql = "SELECT * from members ORDER BY id DESC";
        return jdbc.query(sql, new MemberRowMapper());
    }

    public boolean delete(Long id) {
        String sql = "DELETE FROM members WHERE id=?";
        int affected = jdbc.update(sql, id);
        return affected > 0;
    }

//    public boolean update(MemberSignupReq req, Long id) {
//        String sql = "UPDATE members SET email=?, pwd=?, nickname=? WHERE id=?";
//        int affected = jdbc.update(sql, req.getEmail(), req.getPwd(), req.getNickname(), id);
//        return affected > 0;
//    }

    public int update(Long id, MemberSignupReq req) {
        StringBuilder sql = new StringBuilder("UPDATE members SET nickname = ?");
        List<Object> args = new ArrayList<>();
        args.add(req.getNickname());

        if (req.getPwd() != null && !req.getPwd().isEmpty()) {
            sql.append(", pwd = ?");
            args.add(req.getPwd());
        }
        if (req.getProfileImg() != null && !req.getProfileImg().isEmpty()) {
            sql.append(", profile_img = ?");
            args.add(req.getProfileImg());
        }

        sql.append(" WHERE id = ?");
        args.add(id);

        return jdbc.update(sql.toString(), args.toArray());
    }


    public List<MemberSummaryRes> findHighScores(int offset, int rowNum) {
        @Language("SQL")
        String sql = """
        SELECT * FROM (
            SELECT ROWNUM AS rn, NICKNAME, POINT
            FROM (
                SELECT NICKNAME, POINT
                FROM members
                ORDER BY POINT DESC
            )
            WHERE ROWNUM <= ?
        )
        WHERE rn > ?
        """;
        return jdbc.query(sql, new MemberSummaryResRowMapper(), offset + rowNum, offset);
    }

    // Mapper 메서드  DB -> Member
    static class MemberRowMapper implements RowMapper<MemberRes> {
        @Override
        public MemberRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new MemberRes(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("pwd"),
                    rs.getString("nickname"),   // nickname
                    rs.getString("grade"),      // grade 추가
                    rs.getTimestamp("reg_date").toLocalDateTime(), // regDate
                    rs.getString("profile_img")
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
