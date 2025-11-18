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

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**", "/js/**", "/images/**",
                                "/webjars/**", "/favicon.ico"
                        ).permitAll()
                        .requestMatchers(
                                "/", "/main", "/login",
                                "/members/signup/**", "/reset-password",
                                "/api/**", "/posts/**", "/comments/**",
                                "/board/detail/**"
                        ).permitAll()
                        .requestMatchers("/image/*", "/ajax/post-list", "/main/search").permitAll()
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("email")
                        .passwordParameter("pwd")
                        .successHandler((request, response, authentication) -> {

                            // 로그인 세션 저장
                            CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
                            request.getSession().setAttribute("loginMember", new MemberRes(
                                    user.getId(), user.getEmail(), user.getPassword(),
                                    user.getNickname(), user.getGrade(),
                                    user.getRegDate(), user.getPoint(), user.getProfileImg()
                            ));

                            // 성공 시 URL 파라미터 추가
                            response.sendRedirect("/main?loginSuccess=true");
                        })
                        .failureUrl("/login?loginFail=true") // JS와 동일하게 맞춤
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
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
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }
}
