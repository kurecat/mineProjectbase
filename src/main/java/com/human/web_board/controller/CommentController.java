package com.human.web_board.controller;

import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;
import com.human.web_board.dto.MemberRes;
import com.human.web_board.service.CommentService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comments")
@Slf4j
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
        if (loginMember == null) return "redirect:/login";

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

        boolean isOwner = loginMember.getId().equals(comment.getMemberId());
        boolean isAdmin = loginMember.getGrade() != null
                && loginMember.getGrade().equals("관리자");

        if (!isOwner && !isAdmin) {
            return "redirect:/board/detail/" + postId + "?error=noPermission";
        }

        commentService.delete(commentId);

        return "redirect:/board/detail/" + postId + "?msg=deleted";
    }
    @PostMapping("/report")
    public String report(
            @RequestParam Long postId,
            @RequestParam String content,
            HttpSession session){
        MemberRes login = (MemberRes) session.getAttribute("loginMember");
        if (login == null) {
            return "redirect:/login";
        }

        CommentCreateReq req = new CommentCreateReq();
        req.setPostId(postId);
        req.setMemberId(login.getId());
        req.setContent("[신고] " + content); // 신고 표시

        commentService.write(req);

        return "redirect:/board/detail/" + postId;
    }
    @DeleteMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable Long id) {
        log.info("관리자 코멘트 삭제 요청: {}", id);

        boolean success = commentService.delete(id);

        if (success) {
            return "삭제 완료";
        } else {
            return "삭제 실패";
        }
    }


}
