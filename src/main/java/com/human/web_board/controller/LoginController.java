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

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {
    private final MemberService memberService;

    @GetMapping("/login")
    public String loginPage() {
        return "login/login"; // templates/login/login.html
    }

    @PostMapping("/login")
    public String login(@ModelAttribute LoginReq req, HttpSession session, Model model) {
        log.info("로그인 요청: {}", req);
        MemberRes member = memberService.login(req.getEmail(), req.getPwd());
        if (member == null) {
            model.addAttribute("error", "이메일 또는 비밀번호가 일치하지 않습니다.");
            return "login/login";
        }
        session.setAttribute("loginMember", member);
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
