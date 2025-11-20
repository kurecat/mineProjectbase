package com.human.web_board.service;

import com.human.web_board.dao.MemberDao;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberDao memberDao;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        MemberRes member = memberDao.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + email);
        }

        // ▼ [추가] 권한 부여 로직
        List<GrantedAuthority> authorities = new ArrayList<>();

        // DB의 grade가 "관리자"와 글자가 똑같으면 ROLE_ADMIN 부여
        if ("관리자".equals(member.getGrade())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }

        return new CustomUserDetails(member);
    }
}