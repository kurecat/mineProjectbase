package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
@Slf4j
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Long> signup(@RequestBody MemberSignupReq req) {
        log.info("회원가입 요청: {}", req);
        return ResponseEntity.ok(memberService.signup(req));
    }

    @GetMapping
    public ResponseEntity<List<MemberRes>> list() {
        return ResponseEntity.ok(memberService.list());
    }

    @GetMapping("/by-email/{email}")
    public ResponseEntity<MemberRes> findByEmail(@PathVariable String email) {
        return ResponseEntity.ok(memberService.getByEmail(email));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberRes> findById(@PathVariable Long id) {
        return ResponseEntity.ok(memberService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        memberService.delete(id);
        return ResponseEntity.ok(true);
    }
}
