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

                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.sameOrigin())
                )

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/css/**", "/js/**", "/images/**",
                                "/webjars/**", "/favicon.ico",
                                "/smarteditor2/**"
                        ).permitAll()
                        .requestMatchers(
                                "/", "/main", "/login",
                                "/members/signup/**", "/reset-password",
                                "/api/**", "/posts/**", "/comments/**",
                                "/board/detail/**","/board/**","/board/write"
                                "/board/detail/**","/board/**",
                                "/upload-images-dragdrop"
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

                            response.sendRedirect("/main");
                        })
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .rememberMe(rem -> rem
                        .key("uniqueAndSecret")          // 임의의 비밀키
                        .rememberMeParameter("remember-me") // form 체크박스 name과 맞춤
                        .tokenValiditySeconds(60 * 60 * 24 * 7) // 7일
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
