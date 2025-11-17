package com.human.web_board.security;

import com.human.web_board.dto.MemberRes;
import lombok.extern.java.Log;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final MemberRes memberRes;

    public CustomUserDetails(MemberRes memberRes) {
        this.memberRes = memberRes;
    }

    @Override
    public String getUsername() {
        return memberRes.getEmail(); // 로그인 식별자
    }

    @Override
    public String getPassword() {
        return memberRes.getPwd(); // 암호화된 비밀번호
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // 기본 권한
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    // 👉 우리가 쓰고 싶은 정보들
    public Long getId() {
        return memberRes.getId();
    }

    public String getNickname() {
        return memberRes.getNickname();
    }

    public MemberRes getMember() {
        return memberRes;
    }
}