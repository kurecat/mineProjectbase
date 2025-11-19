package com.human.web_board.controller;

import com.human.web_board.dto.*;
import com.human.web_board.service.CommentService;
import com.human.web_board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /** 글쓰기 폼 */
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        model.addAttribute("postForm", new PostFormDto());
        return "write";   // write.html
    }

    /** 글 저장 처리 (🔥핵심 수정 부분) */
    @PostMapping("/save")
    public String savePost(
            @ModelAttribute("postForm") PostFormDto postFormDto,
            HttpSession session
    ) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }
        // 게시글 Category 저장
        Long categoryId = postFormDto.getCategoryId();
        // 작성자 ID 저장
        postFormDto.setMemberId(loginMember.getId());

        // 저장 실행
        postService.save(postFormDto);
        System.out.println("선택된 카테고리 ID = " + categoryId);
        return "redirect:/board/list?msg=created";
    }

    /** 게시판 목록 */
    @GetMapping("/list")
    public String showBoardList(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "postlist";
    }

    /** 게시글 상세 */
    @GetMapping("/detail/{id}")
    public String showDetail(
            @PathVariable Long id,
            @RequestParam(value="msg", required=false) String msg,
            Model model,
            HttpSession session
    ) {
        postService.increaseView(id);
        PostRes post = postService.get(id);
        List<CommentRes> comments = commentService.list(id);

        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentWrite", new CommentCreateReq());
        model.addAttribute("loginMember", loginMember);

        return "postview"; // 상세페이지
    }

    /** 댓글 수정 */
    @PostMapping("/{commentId}/edit")
    public String edit(
            @PathVariable Long commentId,
            @RequestParam("postId") Long postId,
            @RequestParam("content") String content,
            HttpSession session
    ) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) return "redirect:/login";

        CommentRes comment = commentService.findById(commentId);
        if (!loginMember.getId().equals(comment.getMemberId())) {
            return "redirect:/board/detail/" + postId + "?error=noPermission";
        }

        CommentCreateReq req = new CommentCreateReq();
        req.setContent(content);
        commentService.update(req, commentId);

        return "redirect:/board/detail/" + postId + "?msg=edited";
    }

    /** 추천 기능 */
    @PostMapping("/detail/{id}/recommend")
    @ResponseBody
    public int recommend(
            @PathVariable Long id,
            HttpSession session
    ) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");

        if (loginMember == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        return postService.increaseRecommendations(id);
    }

    /** 게시물 검색 */
    @GetMapping("/search")
    public String searchPosts(
            @RequestParam(required = false) Long mainCategoryId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int rowNum,
            Model model, HttpSession session) {

        model.addAttribute(
                "postSummaries",
                postService.listSummaries(
                        mainCategoryId,
                        categoryId,
                        query,
                        offset,
                        rowNum));

        return "main/main";
    }
}
