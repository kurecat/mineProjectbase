package com.human.web_board.controller;

import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {

    private final CommentService commentService;

    /** 댓글 등록 */
    @PostMapping("/{postId}")
    public String create(
            @PathVariable Long postId,
            CommentCreateReq req,
            HttpSession session
    ) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        System.out.println(loginMember);
        //if (loginMember == null) return "redirect:/login";

        req.setPostId(postId);
        req.setMemberId(loginMember.getId());
        commentService.write(req);

        return "redirect:/board/detail/" + postId + "?msg=created";
    }

    /** 댓글 수정 (상세페이지에서 바로 수정) */
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

    /** 댓글 삭제 */
    @PostMapping("/{commentId}/delete")
    public String delete(
            @PathVariable Long commentId,
            @RequestParam("postId") Long postId,
            HttpSession session
    ) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");

        if (loginMember == null) return "redirect:/login";

        CommentRes comment = commentService.findById(commentId);

        if (!loginMember.getId().equals(comment.getMemberId())) {
            return "redirect:/board/detail/" + postId + "?error=noPermission";
        }

        commentService.delete(commentId);

        return "redirect:/board/detail/" + postId + "?msg=deleted";
    }
}
