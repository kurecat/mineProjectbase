package com.human.web_board.config;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.security.CustomUserDetails;
import com.human.web_board.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.Cookie;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico", "/smarteditor2/**").permitAll()
                        .requestMatchers("/", "/main", "/login", "/members/signup/**", "/reset-password",
                                "/api/**", "/posts/**", "/comments/**", "/board/detail/**", "/board/**", "/board/write",
                                "/upload-images-dragdrop").permitAll()
                        .requestMatchers("/image/*", "/ajax/post-list", "/main/search").permitAll()
                        .requestMatchers("/admin/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                        .successHandler((request, response, authentication) -> {
                            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();

                            MemberRes loginMember = new MemberRes(
                                    user.getId(),
                                    user.getEmail(),
                                    user.getPassword(),
                                    user.getNickname(),
                                    user.getGrade(),
                                    user.getRegDate(),
                                    user.getPoint(),
                                    user.getProfileImg()
                            );

                            request.getSession().setAttribute("loginMember", loginMember);

                            // Remember me 체크박스 기반 이메일 쿠키 저장/삭제
                            String remember = request.getParameter("remember-me");
                            if ("on".equals(remember)) {
                                Cookie rememberCookie = new Cookie("rememberId", user.getEmail());
                                rememberCookie.setMaxAge(60 * 60 * 24 * 7); // 7일
                                rememberCookie.setPath("/");
                                response.addCookie(rememberCookie);
                            } else {
                                Cookie rememberCookie = new Cookie("rememberId", null);
                                rememberCookie.setMaxAge(0);
                                rememberCookie.setPath("/");
                                response.addCookie(rememberCookie);
                            }

                            response.sendRedirect("/main");
                        })
                        .failureUrl("/login?loginFail=true")
                        .permitAll()
                )
                .rememberMe(rem -> rem
                        .key("uniqueAndSecret")
                        .rememberMeParameter("remember-me")
                        .userDetailsService(customUserDetailsService)
                        .tokenValiditySeconds(60 * 60 * 24 * 7)
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            // 세션 무효화
                            request.getSession().invalidate();

                            // 세션 쿠키만 삭제
                            var cookies = request.getCookies();
                            if (cookies != null) {
                                for (var cookie : cookies) {
                                    if ("JSESSIONID".equals(cookie.getName())) {
                                        cookie.setMaxAge(0);
                                        cookie.setValue(null);
                                        cookie.setPath("/");
                                        response.addCookie(cookie);
                                    }
                                }
                            }

                            // 이메일 쿠키는 그대로 유지 → 체크박스와 이메일 유지 가능

                            response.sendRedirect("/login");
                        })
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());
        return authBuilder.build();
    }
}
