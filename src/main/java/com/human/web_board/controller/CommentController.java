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
    @PostMapping("/{commentId}/delete")
    public String delete(@PathVariable("commentId") Long commentId, @RequestParam("postId") Long postId, HttpSession session){
        MemberRes member = (MemberRes) session.getAttribute("loginMember");
        if (member == null) return "redirect:/";

        commentService.delete(commentId);

        return "redirect:/posts/" + postId;
    }


    @GetMapping("/{commentId}/edit")
    public String showEditForm(@PathVariable("commentId") Long commentId, Model model, HttpSession session) {

        if (session.getAttribute("loginMember") == null) return "redirect:/";

        CommentRes comment = commentService.findById(commentId);

        model.addAttribute("comment", comment);

        return "post/edit";
    }

    @PostMapping("/{commentId}/edit")
    public String processEditForm(
            @PathVariable("commentId") Long commentId,
            @ModelAttribute("comment") CommentCreateReq req,
            HttpSession session
    ) {
        if (session.getAttribute("loginMember") == null) {
            return "redirect:/";
        }

        commentService.update(req, commentId);

        return "redirect:/posts/" + req.getPost_Id();
    }
}
