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

    /** 권한 지정: grade 관리자 = ADMIN, 그 외 = USER */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if ("관리자".equals(this.grade)) {
            authorities.add(new SimpleGrantedAuthority("ADMIN"));
        } else {
            authorities.add(new SimpleGrantedAuthority("USER"));
        }
        return authorities;
    }

    /** Spring Security에서 username = email */
    @Override
    public String getUsername() {
        return this.email;
    }

    /** 비밀번호 반환 */
    @Override
    public String getPassword() {
        return this.password;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

}
