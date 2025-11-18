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
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**", "/favicon.ico").permitAll()
                        .requestMatchers("/", "/main", "/login", "/members/signup/**","/reset-password" ,"/api/**","/posts/**","/comments/**","/board/detail/**").permitAll()
                        .requestMatchers("/image/*", "/ajax/post-list","/main/search").permitAll()
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
                        .failureUrl("/login?error")  // 실패 시
                        .permitAll()


               .defaultSuccessUrl("/main?loginSuccess=true", true)

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

    // DB 기반 로그인용 AuthenticationManager
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.userDetailsService(customUserDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build(); // .and() 제거
    }
}
