package com.human.web_board.controller;

import com.human.web_board.dto.*;
import com.human.web_board.service.CommentService;
import com.human.web_board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/posts")
@Slf4j
public class PostController {
    private final PostService postService;
    private final CommentService commentService;
    @GetMapping  // 게시글 목록 가져 오기
    public String list(HttpSession session, Model model) {
        // 로그인 여부 확인 var은 타입 추론을 해서 자동으로 형을 찾아 줌
        var loginMember = session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/";  // 세션이 없으면 로그인 페이지로 이동
        List<PostRes> list = postService.list();
        model.addAttribute("posts", list);
        return "post/list";
    }

    // 게시글 쓰기 폼
    @GetMapping("/new")
    public String postWriteForm(HttpSession session, Model model) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/";
        }
        model.addAttribute(new PostCreateReq()); // 이름을 생략하면 postCreateReq로 모델에 등록
        return "post/new";
    }


    // 게시글 쓰기 DB 처리
    @PostMapping("/new")
    public String create(PostCreateReq req, HttpSession session, Model model) {
        MemberRes member = (MemberRes) session.getAttribute("loginMember");

        log.error("게시글 쓰기 {}", req);

        if (member == null) {
            return "redirect:/";
        }
        try {
            req.setMemberId(member.getId()); // 화면에서 정보를 입려 받을 수 없기 때문에 세션 정보에서 추출해서 넣어 줌
            Long postId = postService.write(req);
            return "redirect:/posts";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "posts/new";
        }
    }


    // 게시글 상세 조회 : 게시글 목록에서 해당 게시글을 클릭할 때 호출 됨
    @GetMapping("/{postId}")  // URL 경로에 정보를 싣는 방식, http://localhost:8111/posts/2
    public String detail(@PathVariable Long postId, Model model, HttpSession session) {
        if (session.getAttribute("loginMember") == null) return "redirect:/";

        PostRes post = postService.get(postId);  // 전달 받은 게시글 ID로 게시글 상세 정보를 가져 옴
        if (post == null) return "redirect:/posts"; // 게시글 상세 조회 정보가 없으면 게시글 목록으로 이동

        log.error("게시글 상세 조회 : {}", post);

        // 댓글 목록 가져 오기, 게시글 ID로 댓글 목록 가져 오기
        List<CommentRes> comments = commentService.list(postId);
        model.addAttribute("post", post);  // 게시글 상세 정보 객체 전달
        model.addAttribute("comments", comments); // 댓글 목록 리스트 전달

        // 댓글 등록 폼 바이딩
        CommentCreateReq commentWrite = new CommentCreateReq();
        commentWrite.setPostId(postId); // 댓글 등록 시 게시글 ID 필요
        model.addAttribute("commentWrite", commentWrite);

        return "post/detail";
    }
}