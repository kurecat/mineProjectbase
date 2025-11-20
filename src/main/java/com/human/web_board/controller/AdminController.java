package com.human.web_board.controller;

import com.human.web_board.service.MemberService;
import com.human.web_board.service.PostService;
import com.human.web_board.service.CommentService; // 댓글 서비스 (이름 확인 필요)
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    // 생성자 주입
    public AdminController(MemberService memberService, PostService postService, CommentService commentService) {
        this.memberService = memberService;
        this.postService = postService;
        this.commentService = commentService;
    }

    // 관리자 메인 페이지
    @GetMapping
    public String adminPage(Model model) {

        // 전체 회원 목록
        model.addAttribute("members", memberService.findAllMembers());

        // 전체 게시글 목록
        model.addAttribute("posts", postService.findAllPosts());

        // 전체 댓글 목록
        model.addAttribute("comments", commentService.findAllComments());

        return "adminPage"; // admin.html 파일 경로
    }

    // 1. 회원 강퇴
    @PostMapping("/member/delete/{id}")
    public String deleteMember(@PathVariable Long id) {
        memberService.delete(id); // 회원 삭제 서비스 호출
        return "redirect:/admin"; // 삭제 후 관리자 페이지로 복귀
    }

    // 2. 게시글 삭제
    @PostMapping("/post/delete/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.delete(id); // 게시글 삭제 서비스 호출
        return "redirect:/admin?section=post";
    }

    // 3. 댓글 및 신고 댓글 삭제
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(
            @PathVariable Long id,
            @RequestParam(required = false) String redirectSection // 파라미터 추가
    ) {
        commentService.delete(id);

        // 신고 관리 탭에서 삭제했다면 거기로 복귀, 아니면 기본 댓글 관리 탭으로
        if ("report".equals(redirectSection)) {
            return "redirect:/admin?section=report";
        }
        return "redirect:/admin?section=comment";
    }
}