package com.human.web_board.controller;

import com.human.web_board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainPageController {
    private final PostService postService;

    @GetMapping("/")
    public String mainPage() {
        return "redirect:/boards/1/posts";
    }

    // 로그인 기능
//    @PostMapping("/login")
//    public String login(Model monel, HttpSession session) {
//        // 로그인 서비스
//        return "redirect:/";
//    }

    // 게시판 이동 기능
    @GetMapping("/boards/{id}/posts")
    public String selectBoard(
            @PathVariable Long id,
            @RequestParam(defaultValue = "1") int offset,
            @RequestParam(defaultValue = "10") int rowNum,
            Model model, HttpSession session) {
        // 서비스에서 지정 게시판 정보 & 기타정보 가져와서 모델에 추가
        model.addAttribute(
                "postSummaries",
                postService.list(id, offset, rowNum)
        );
        return "main/main";
    }
    // 게시물 검색 기능
    @GetMapping("/boards/search")
    public String searchPost(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "1") int offset,
            @RequestParam(defaultValue = "10") int rowNum,
            Model model, HttpSession session) {
        // text가 포함된 게시물들 찾아서 모델에 추가
        model.addAttribute(
                "postSummaries",
                postService.searchList(query, offset, rowNum)
        );
        return "main/main";
    }

    // 게시판 내에서 게시물 검색 기능
    @GetMapping("/boards/{id}/search")
    public String searchPost(
            @PathVariable Long id,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "1") int offset,
            @RequestParam(defaultValue = "10") int rowNum,
            Model model, HttpSession session) {
        // text가 포함된 게시물들 찾아서 모델에 추가
        model.addAttribute(
                "postSummaries",
                postService.searchList(id, query, offset, rowNum)
        );
        return "main/main";
    }

}


