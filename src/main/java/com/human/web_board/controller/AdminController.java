package com.human.web_board.controller;

import com.human.web_board.service.MemberService;
import com.human.web_board.service.PostService;
import com.human.web_board.service.CommentService; // 댓글 서비스 (이름 확인 필요)
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @GetMapping
    public String adminPage(Model model) {
        // 1. 모든 회원 리스트 가져오기
        model.addAttribute("members", memberService.findAllMembers());

        // 2. 모든 게시글 리스트 가져오기
        model.addAttribute("posts", postService.findAllPosts());

        // 3. 모든 댓글 리스트 가져오기
        // (참고: 댓글 DTO에 게시글 제목(postTitle)이 포함되어 있어야 화면에 출력 가능)
        model.addAttribute("comments", commentService.findAllComments());

        return "adminPage"; // admin.html 파일 경로
    }
}