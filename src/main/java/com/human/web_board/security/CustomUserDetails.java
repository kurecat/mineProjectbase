package com.human.web_board.security;

import com.human.web_board.dto.MemberRes;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Spring Security에서 인증된 사용자 정보를 담는 클래스
 */
@Getter
public class CustomUserDetails implements UserDetails {

    private final Long id;
    private final String email;
    private final String password;   // pwd
    private final String nickname;
    private final String grade;
    private final LocalDateTime regDate;
    private final int point;
    private final String profileImg;

    /** MemberRes → CustomUserDetails 로 변환하는 생성자 */
    public CustomUserDetails(MemberRes member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.password = member.getPwd();
        this.nickname = member.getNickname();
        this.grade = member.getGrade();
        this.regDate = member.getRegDate();
        this.point = member.getPoint();
        this.profileImg = member.getProfileImg();
    }

    /** * 권한 지정 메서드 (수정됨)
     * 1. "관리자" 텍스트 확인
     * 2. "ROLE_" 접두사 붙이기
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // ▼ [수정 1] DB값이 "관리자" 인지 확인 (기존 "2"에서 변경)
        if ("관리자".equals(this.grade)) {
            // ▼ [수정 2] hasRole("ADMIN")이 인식하려면 "ROLE_ADMIN" 이라고 줘야 함
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

}