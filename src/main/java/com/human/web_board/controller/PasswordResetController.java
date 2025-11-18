package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.PasswordResetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    // 비밀번호 찾기 폼
    @GetMapping("/reset-password")
    public String showResetForm() {
        return "login/Findpw";  // templates/login/Findpw.html
    }

    // 비밀번호 재설정 처리
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String email,
                                @RequestParam String password,
                                @RequestParam String passwordCheck) {

        log.info("비밀번호 재설정 요청: {}", email);

        // 1. 비밀번호 일치 확인
        if (!password.equals(passwordCheck)) {
            log.warn("비밀번호 불일치");
            return "redirect:/reset-password?resetFail=passwordMismatch";
        }

        // 2. 이메일 조회
        MemberRes member = passwordResetService.findByEmail(email);
        if (member == null) {
            log.warn("이메일 없음: {}", email);
            return "redirect:/reset-password?resetFail=emailNotFound";
        }

        // 3. 비밀번호 변경
        boolean success = passwordResetService.resetPassword(member.getId(), password);

        if (success) {
            log.info("비밀번호 재설정 성공");
            return "redirect:/login?resetSuccess=true";
        } else {
            log.error("비밀번호 재설정 실패(DB 업데이트 실패)");
            return "redirect:/reset-password?resetFail=updateError";
        }
    }
}
