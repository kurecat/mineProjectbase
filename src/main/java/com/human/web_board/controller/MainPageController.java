package com.human.web_board.controller;

import com.human.web_board.dto.PostSummaryRes;
import com.human.web_board.service.MemberService;
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

import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainPageController {
    private final PostService postService;
    private final MemberService memberService;

    @GetMapping("/")
    public String mainPage() {
        return "redirect:/main";
    }

    // 로그인 기능
//    @PostMapping("/login")
//    public String login(Model monel, HttpSession session) {
//        // 로그인 서비스
//        return "redirect:/";
//    }

    // 전체 or 특정 게시판
    @GetMapping("/main")
    public String selectBoard(
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int rowNum,
            Model model, HttpSession session) {
        // 서비스에서 전체 목록 가져와서 모델에 추가
        List<PostSummaryRes> postSummaries = (id == null)
            ? postService.list(offset, rowNum)
            : postService.list(id, offset, rowNum);
        model.addAttribute("postSummaries", postSummaries);
        model.addAttribute(
            "popularPosts",
            postService.listPopular(1, 10)
        );
        model.addAttribute(
            "recommendedPosts",
            postService.listRecommended(1, 10)
        );
        model.addAttribute(
            "highScores",
            memberService.listHighScores(1, 10)
        );
        return "main/main";
    }

    // 게시물 검색 기능
    @GetMapping("/main/search")
    public String searchPosts(
            @RequestParam(required = false) Long id,
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int rowNum,
            Model model, HttpSession session) {
        // 지정 게시판 내 text가 포함된 게시물들 찾아서 모델에 추가
        List<PostSummaryRes> postSummaries = (id == null)
                ? postService.searchList(query, offset, rowNum)
                : postService.searchList(id, query, offset, rowNum);
        model.addAttribute("postSummaries", postSummaries);
        model.addAttribute(
                "popularPosts",
                postService.listPopular(1, 10)
        );
        model.addAttribute(
                "recommendedPosts",
                postService.listRecommended(1, 10)
        );
        return "main/main";
    }
}


