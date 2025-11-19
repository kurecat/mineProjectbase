package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.service.FileStorageService;
import com.human.web_board.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;

    // 회원 가입 폼
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberForm", new MemberSignupReq());
        return "members/signup";
    }

    // 회원 가입 처리
    @PostMapping("/signup")// 수정: /members/signup
    public String signup(MemberSignupReq req, Model model,
                         @RequestParam(value = "profileImage", required = false) MultipartFile profileImage) {
        log.info("회원가입 요청: {}", req);

        // 비밀번호 확인
        if (!req.getPwd().equals(req.getPasswordCheck())) {
            log.warn("회원가입 실패 - 비밀번호 불일치: {}", req.getEmail());
            return "redirect:/members/signup?signupFail=passwordMismatch";
        }

        // 이메일 중복 확인
        if (memberService.isEmailExists(req.getEmail())) {
            log.warn("회원가입 실패 - 이메일 중복: {}", req.getEmail());
            return "redirect:/members/signup?signupFail=emailExists";
        }

        // 닉네임 중복 확인
        if (memberService.isNicknameExists(req.getNickname())) {
            log.warn("회원가입 실패 - 닉네임 중복: {}", req.getNickname());
            return "redirect:/members/signup?signupFail=nicknameExists";
        }

        // 회원가입 시도
        String newImagePath = "";
        if (profileImage != null && !profileImage.isEmpty()) {
            newImagePath = fileStorageService.saveImage(profileImage, "members");
        }

        req.setProfileImg(newImagePath);

        try {
            memberService.signup(req);
        } catch (IllegalArgumentException e) {
            log.warn("회원가입 실패 - 예외 발생: {}", e.getMessage());
            return "redirect:/members/signup?signupFail=exception";
        }

        // 성공 시 로그인 페이지로 이동
        return "redirect:/login?signupSuccess=true";
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
    public String edit(@PathVariable Long id,
                       @ModelAttribute MemberSignupReq req,
                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                       Principal principal,
                       Model model) {

        MemberRes member = memberService.getById(id);

        // 본인 확인
        if (!member.getEmail().equals(principal.getName())) {
            model.addAttribute("error", "본인만 수정할 수 있습니다.");
            return "member/myPage";
        }

        // 비밀번호 확인
        if (!req.getPwd().equals(req.getPasswordCheck())) {
            log.warn("회원가입 실패 - 비밀번호 불일치: {}", req.getEmail());
            return "redirect:/members/" + id + "?signupFail=passwordMismatch";
        }

        // 닉네임 중복 확인
        if (memberService.isNicknameExists(req.getNickname())) {
            log.warn("회원가입 실패 - 닉네임 중복: {}", req.getNickname());
            return "redirect:/members/" + id + "?signupFail=nicknameExists";
        }

        String currentImagePath = member.getProfileImg();
        String newImagePath = currentImagePath;

        if (profileImage != null && !profileImage.isEmpty()) {
            newImagePath = fileStorageService.saveImage(profileImage, "members");
            if (currentImagePath != null && !currentImagePath.isEmpty()) {
                fileStorageService.deleteIfExists(currentImagePath);
            }
        }

        req.setProfileImg(newImagePath);

        try {
            memberService.update(req, id);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "member/myPage";
        }

        return "redirect:/members/" + id + "?updateSuccess=true";
    }

    // 회원 삭제 처리
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, Principal principal, Model model) {
        MemberRes member = memberService.getById(id);

        // 본인 확인
        if (!member.getEmail().equals(principal.getName())) {
            model.addAttribute("error", "본인만 삭제할 수 있습니다.");
            return "member/myPage";
        }

        memberService.delete(id);
        return "redirect:/logout";
    }

    // 회원 상세
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, Principal principal) {
        if (principal != null) {
            String email = principal.getName();
            MemberRes memberRes = memberService.getByEmail(email);
            System.out.println(memberRes);
            model.addAttribute("loginMember", memberRes);
        }
        model.addAttribute("member", memberService.getById(id));
        return "members/myPage";
    }

    // 닉네임 중복 검사
    @GetMapping("/api/members/check-nickname")
    @ResponseBody
    public boolean checkNickname(@RequestParam String nickname) {
        boolean exists = memberService.isNicknameExists(nickname);
        log.info("닉네임 중복 체크: {}, 존재 여부: {}", nickname, exists);
        return exists;
    }

    // 이메일 중복 검사
    @GetMapping("/api/members/check-email")
    @ResponseBody
    public boolean checkEmail(@RequestParam String email) {
        boolean exists = memberService.isEmailExists(email);
        log.info("이메일 중복 체크: {}, 존재 여부: {}", email, exists);
        return exists;
    }

    @PostMapping("/{id}/verify-password")
    @ResponseBody
    public boolean verifyPassword(@PathVariable Long id,
                                  @RequestParam String pwd,
                                 Principal principal,
                                 RedirectAttributes redirectAttributes) {
        MemberRes loginMember = memberService.getByEmail(principal.getName());
        return loginMember.getId().equals(id);
    }


}
