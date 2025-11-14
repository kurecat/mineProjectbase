package com.human.web_board.controller;

import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.FileStorageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import com.human.web_board.dto.MemberSignupReq;
import com.human.web_board.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;
    private final FileStorageService fileStorageService;

    // 회원 가입 폼
    @GetMapping("/signup")
    public String signupForm(Model model) {
        model.addAttribute("memberForm", new MemberSignupReq());
        return "signup"; // 파일명과 일치
    }


    // 회원 가입 처리
    @PostMapping("/signup")
    public String signup(MemberSignupReq req, Model model) {
        System.out.println("PWD 확인: " + req.getPwd());

        log.info("회원가입 요청 들어옴: {}", req);
        try {
            memberService.signup(req);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "signup"; // 파일명과 일치
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
    public String edit(@PathVariable Long id,
                       @ModelAttribute MemberSignupReq req,
                       @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
                       Model model) {

        MemberRes member = memberService.getById(id);
        //업로드(선택) : 이미지가 있으면 저장 후 상대 경로 확보
        String currentImagePath = member.getProfileImg(); // 💡 기존 이미지 경로
        String newImagePath = currentImagePath; // 기본값은 기존 경로로 설정
        if (profileImage != null && !profileImage.isEmpty()) {
            newImagePath = fileStorageService.saveImage(profileImage, "members");

            // 2. 💡 기존 파일 삭제 로직 추가 (새 파일이 성공적으로 저장된 경우)
            if (currentImagePath != null && !currentImagePath.isEmpty()) {
                fileStorageService.deleteIfExists(currentImagePath);
            }
        }

        try {
            memberService.update(id, req);
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
        return "members/myPage";
    }
}
