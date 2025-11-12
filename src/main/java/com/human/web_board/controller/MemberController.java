package com.human.web_board.controller;

import org.springframework.ui.Model;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;

    // 회원 가입 폼
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberForm", new MemberSignupReq());
        return "members/new";
    }

    // 회원 가입 처리
    @PostMapping("/new")
    public String signup(MemberSignupReq req, Model model) {
        log.info("회원가입 요청 들어옴: {}", req);
        try {
            memberService.signup(req);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "members/new";
        }
        return "redirect:/login";
    }

    // 회원 목록
    @GetMapping("/memberlist")
    public String list(Model model) {
        model.addAttribute("member", memberService.list());
        return "members/memberlist";
    }

    // 회원 수정 폼
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("memberForm", memberService.getById(id));
        return "members/edit";
    }

    // 회원 수정 처리
    @PostMapping("/{id}/edit")
    public String edit(@PathVariable Long id, MemberSignupReq req, Model model) {
        try {
            memberService.update(req, id);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "members/edit";
        }
        return "redirect:/members/" + id;
    }

    // 회원 삭제
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/members/memberlist";
    }
    // 회원 상세
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("member", memberService.getById(id));
        return "members/memberS";
    }
}
