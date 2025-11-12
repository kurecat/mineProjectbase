package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.dto.PostRes;
import com.human.web_board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {
    private final MemberService memberService; // 의존성 주입
    // 회원 가입 폼 페이지
    @GetMapping("/members/new")
    public String signupForm(Model model) {
        // model은 화면과 정보공유를 위해서 사용, 회원 정보를 담을 빈 객체를 생성 후 전달
        model.addAttribute("memberFrom", new MemberSignupReq());
        return "members/new";
    }
    // 회원 가입 처리
    @PostMapping("/members/new")
    public String signup(MemberSignupReq req, Model model) {
        try{
            memberService.signup(req);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "members/new";
        }
        return "redirect:/"; // 가입이 성공하면 로그인 페이지로 이동함.
    }

    // 회원 조회
    @GetMapping("/members/memberlist")
    public String memberlist(HttpSession session,Model model){
        var loginMember = session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/";
        List<MemberRes> list = memberService.list();
        model.addAttribute("members", list);
        return "members/memberlist";
    }

    @GetMapping("/members/memberS/{id}")
    public String findMemberById(@PathVariable Long id, Model model){
        MemberRes memberRes = memberService.getById(id);
        model.addAttribute("member", memberRes);
        return "members/memberS";
    }

    // 포인트랭킹조회
}