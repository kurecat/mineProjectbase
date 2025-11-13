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

import javax.swing.plaf.PanelUI;

@Controller @RequiredArgsConstructor @RequestMapping("comments")
public class CommentController {
    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("{postId}")
    public String create(@PathVariable Long postId, CommentCreateReq req, HttpSession session){
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        if (session.getAttribute("loginMember") == null) return "redirect:/";
        req.setPost_Id(postId);
        req.setMember_Id(member.getId());
        commentService.write(req);
        return "redirect:/posts/" + postId;
    }
    // 댓글 삭제
    //<input type="hidden" name="postId" value="3" /> 삭제버튼 폼에 삽입
    @PostMapping("/{commentId}/delete")
    public String delete(@PathVariable("commentId") Long commentId, @RequestParam("postId") Long postId, HttpSession session){
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/login"; // 로그인 안 했으면 로그인 페이지로
        }
        CommentRes comment = commentService.findById(commentId);
//        if (comment == null) {
//            return "redirect:/posts/" + postId + "?error=댓글이 존재하지 않습니다."; // 댓글 없으면
//        }
        if (!member.getId().equals(comment.getMember_Id())) {
            return "redirect:/posts/" + postId + "?error=작성자가 아닙니다."; // 작성자가 아니면 접근 금지
        }
        commentService.delete(commentId);
        return "redirect:/posts/" + postId;
    }
    // 댓글 수정
    @GetMapping("/{commentId}/edit")
    public String showEditForm(@PathVariable("commentId") Long commentId, Model model, HttpSession session) {
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        if (member == null) {
            return "redirect:/"; // 로그인 안 했으면 홈으로
        }
          CommentRes comment = commentService.findById(commentId);
//        if (comment == null) {
//            return "redirect:/?error=notfound"; // 댓글 없으면 홈으로
//        }
        if (!member.getId().equals(comment.getMember_Id())) {
            return "redirect:/?error=forbidden"; // 작성자가 아니면 접근 금지
        }
        model.addAttribute("comment", comment);
        return "post/edit";
    }

    @PostMapping("/{commentId}/edit")
    public String processEditForm(
            @PathVariable("commentId") Long commentId,
            @ModelAttribute("comment") CommentCreateReq req,
            HttpSession session
    ) {
        MemberRes member = (MemberRes) session.getAttribute("loginMember"); // 로그인 여부 체크
        if (member == null) {
            return "redirect:/";
        }
        CommentRes comment = commentService.findById(commentId);
//        if (comment == null) {
//            return "redirect:/?error=notfound"; // 댓글 없으면 홈으로
//        }

        if (session.getAttribute("loginMember") == null) {
            return "redirect:/";
        }
        if (!member.getId().equals(comment.getMember_Id())) {
            return "redirect:/?error=forbidden"; // 작성자가 아니면 접근 금지
        }
        commentService.update(req, commentId);  // 댓글 수정
        return "redirect:/posts/" + req.getPost_Id();
    }
}
