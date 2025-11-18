package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    //비밀번호 찾기 폼
    @GetMapping("/reset-password")
    public String showResetForm() {
        return "login/Findpw";  // templates/login/Findpw.html
    }

    //비밀번호 재설정 처리
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String passwordCheck,
                                Model model) {
        log.info("비밀번호 재설정 요청: {}", email);

        // 1. 비밀번호 일치 확인
        if (!password.equals(passwordCheck)) {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다.");
            return "login/Findpw";
        }
        log.info("비밀번호 일치 확인 완료");

        // 2. 이메일 조회
        MemberRes member = passwordResetService.findByEmail(email);
        if (member == null) {
            model.addAttribute("error", "등록되지 않은 이메일입니다.");
            return "login/Findpw";
        }
        log.info("회원 조회 성공: ID={}, 이메일={}", member.getId(), member.getEmail());

        // 3. 비밀번호 변경
        boolean success = passwordResetService.resetPassword(member.getId(), password);
        if (success) {
            log.info("비밀번호 재설정 완료: ID={}, 이메일={}", member.getId(), email);
        } else {
            log.error("비밀번호 재설정 실패: ID={}, 이메일={}", member.getId(), email);
        }

        model.addAttribute("message", "비밀번호가 성공적으로 변경되었습니다.");
        return "redirect:/login";


    }
}
