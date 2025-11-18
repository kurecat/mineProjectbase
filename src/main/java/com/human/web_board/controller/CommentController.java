package com.human.web_board.controller;

import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 등록
     * - URL 예: POST /comments/5   (5번 게시글에 대한 댓글)
     * - form action: th:action="@{|/comments/${post.id}|}"
     */
    @PostMapping("/{postId}")
    public String create(
            @PathVariable Long postId,
            CommentCreateReq req,
            HttpSession session
    ) {
//        System.out.println(1);
//        System.out.println(session.getAttribute("loginMember"));
//        System.out.println(postId);
//        System.out.println(req.getMemberId());
//        System.out.println(req.getPostId());
//        System.out.println(req.getContent());
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        Long memberId = loginMember.getId();
        req.setMemberId(memberId);
        req.setPostId(postId);
        System.out.println(memberId);

        if (loginMember == null) {
            // 비로그인 → 로그인 페이지로 이동
            return "redirect:/login";
        }

        // 어떤 게시글에, 누가 쓴 댓글인지 세팅
        req.setPostId(postId);
        req.setMemberId(loginMember.getId());

        commentService.write(req);

        // 댓글 작성 후 해당 게시글 상세로 리다이렉트
        return "redirect:/board/detail/" + postId;
    }

    /**
     * 댓글 삭제
     * - URL 예: POST /comments/10/delete
     * - form 에서 postId 를 hidden 으로 같이 넘겨줘야 함
     */
    @PostMapping("/{commentId}/delete")
    public String delete(
            @PathVariable Long commentId,
            @RequestParam("postId") Long postId,
            HttpSession session
    ) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        // 댓글 정보 조회
        CommentRes comment = commentService.findById(commentId);

        // 작성자 본인인지 확인
        if (!loginMember.getId().equals(comment.getMemberId())) {
            // 본인이 아니면 접근 불가 → 그냥 상세 페이지로 돌려보냄 (에러 메시지 붙이고 싶으면 쿼리스트링 사용)
            return "redirect:/board/detail/" + postId + "?error=noPermission";
        }

        // 삭제 수행
        commentService.delete(commentId);

        return "redirect:/board/detail/" + postId;
    }
}
