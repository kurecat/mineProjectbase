package com.human.web_board.service.impl;

import com.human.web_board.dao.MemberDao;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {
    private final MemberDao memberDao;

    @Override
    public Long signup(MemberSignupReq req) {
        if (memberDao.findByEmail(req.getEmail()) != null) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
        return memberDao.save(req);
    }

    @Override
    public MemberRes login(String email, String pwd) {
        MemberRes member = memberDao.findByEmail(email);
        if (member == null || !member.getPwd().equals(pwd)) return null;
        return member;
    }

    @Override
    public MemberRes getByEmail(String email) {
        MemberRes member = memberDao.findByEmail(email);
        if (member == null) throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        return member;
    }

    @Override
    public MemberRes getById(Long id) {
        MemberRes member = memberDao.findById(id);
        if (member == null) throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        return member;
    }

    @Override
    public List<MemberRes> list() {
        return memberDao.findAll();
    }

    @Override
    public boolean delete(Long id) {
        boolean success = memberDao.delete(id);
        if (!success) throw new IllegalArgumentException("회원 삭제 실패");
        return true;
    }

    @Override
    public boolean update(MemberSignupReq req, Long id){
        boolean success = memberDao.update(req, id);
        if(!success) throw new IllegalArgumentException("회원 수정 실패");
        return true;
    }
}
