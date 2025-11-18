package com.human.web_board.service;

import com.human.web_board.dao.MemberDao;
import com.human.web_board.dto.MemberRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final MemberDao memberDao;
    private final PasswordEncoder passwordEncoder;

    // 이메일로 회원 찾기
    public MemberRes findByEmail(String email) {
        return memberDao.findByEmail(email);
    }

    // 비밀번호 변경
    public boolean resetPassword(Long memberId, String newPassword) {
        String encoded = passwordEncoder.encode(newPassword);
        return memberDao.updatePassword(memberId, encoded);
    }
}
