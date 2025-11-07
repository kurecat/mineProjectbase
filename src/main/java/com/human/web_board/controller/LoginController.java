package com.human.web_board.controller;

import com.human.web_board.dto.LoginReq;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller  // 주로 서버사이드렌더링을 위한 컨트롤러
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final MemberService memberService;  // 생성자 의존성 주입

    @GetMapping("/A")  // 루트 경로에 대한 페이지 이동, http://localhost:8111
    public String loginPage() {
        return "login/login";   // resoures/template/login/login.html
    }

    @PostMapping("/login")  // 로그인 폼 전송
    public String login(@ModelAttribute LoginReq req, HttpSession session, Model model) {
        log.info("로그인 정보 : {}", req);
        MemberRes member = memberService.login(req.getEmail(), req.getPwd());
        if (member == null) {
            model.addAttribute("error", "이메일 또는 비밀번호가 일치하지 않습니다.");
            return "login/login";
        }
        session.setAttribute("loginMember", member);
        return "redirect:/posts";  // 게시글 목록 페이지로 이동
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();  // 저장된 세션 정보를 무효화 시킴
        return "redirect:/";
    }
}