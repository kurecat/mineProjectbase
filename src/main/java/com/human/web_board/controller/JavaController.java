package com.human.web_board.controller;

import com.human.web_board.service.MemberService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController // @Controller가 아니라 @RestController여야 합니다 (데이터만 반환)
class ApiMemberController {

    private final MemberService memberService; // (서비스 의존성 주입 필요)

    public ApiMemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // 이메일 존재 여부 확인 API
    @GetMapping("/api/check-email")
    public ResponseEntity<Boolean> checkEmail(@RequestParam String email) {
        // memberService에 이메일로 회원을 찾는 메서드가 있다고 가정 (없으면 false, 있으면 true 반환)
        boolean exists = memberService.checkEmailExists(email);
        return ResponseEntity.ok(exists);
    }
}