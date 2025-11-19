package com.human.web_board.controller;

import com.human.web_board.dto.Pagination;
import com.human.web_board.service.MemberService;
import com.human.web_board.service.PostService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class MainPageController {
    private final PostService postService;
    private final MemberService memberService;



    // 전체 or 특정 게시판
    @GetMapping("/main")
    public String listPosts(
            @RequestParam(required = false) Long mainCategoryId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "15") int rowNum,
            Model model) {

        // 서비스에서 전체 목록 가져와서 모델에 추가

        model.addAttribute(
                "postSummaries",
                postService.listSummaries(
                        mainCategoryId,
                        categoryId,
                        null,
                        offset,
                        rowNum));
        model.addAttribute(
            "popularPosts",
            postService.listPopular(0, 5)
        );
        model.addAttribute(
            "recommendedPosts",
            postService.listRecommended(0, 5)
        );
        model.addAttribute(
            "highScores",
            memberService.listHighScores(0, 5)
        );
        return "main/main";
    }

    // 게시물 검색 기능
    @GetMapping("/main/search")
    public String searchPosts(
            @RequestParam(required = false) Long mainCategoryId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "15") int rowNum,
            Model model) {
        // 서비스에서 전체 목록 가져와서 모델에 추가

        model.addAttribute(
                "postSummaries",
                postService.listSummaries(
                        mainCategoryId,
                        categoryId,
                        query,
                        offset,
                        rowNum));
        model.addAttribute(
                "popularPosts",
                postService.listPopular(0, 5)
        );
        model.addAttribute(
                "recommendedPosts",
                postService.listRecommended(0, 5)
        );
        model.addAttribute(
                "highScores",
                memberService.listHighScores(0, 5)
        );
        return "main/main";
    }
    // [수정] GET / (최초 페이지 로드용)
    @GetMapping("/")
    public String listPosts(Model model,
                            @RequestParam(value = "category", required = false) String category,
                            @RequestParam(defaultValue = "0") int offset,
                            @RequestParam(defaultValue = "15") int rowNum) {

        // 1. [수정] 총 게시물 수 계산 및 Pagination 객체 생성
        int totalCount = postService.countSummaries(category);
        Pagination pagination = new Pagination(totalCount, rowNum, offset);

        // 2. 목록 및 페이지네이션 정보 전달
        model.addAttribute("postSummaries", postService.listSummaries(category, offset, rowNum));
        model.addAttribute("pagination", pagination); // [수정] 페이지네이션 객체 추가

        // 3. (기존 코드) 인기글, 추천글, 랭킹
        model.addAttribute("popularPosts", postService.listPopular(0, 5));
        model.addAttribute("recommendedPosts", postService.listRecommended(0, 5));
        model.addAttribute("highScores", memberService.listHighScores(0, 5));

        return "main/main"; // (뷰 이름은 "main/main"으로 가정)
    }

    // [수정] GET /ajax/post-list (AJAX 전용)
    @GetMapping("/ajax/post-list")
    public String getPostListFragment(Model model,
                                      @RequestParam(value = "category", required = false) String category,
                                      @RequestParam(defaultValue = "0") int offset,
                                      @RequestParam(defaultValue = "15") int rowNum) {

        // 1. [수정] 총 게시물 수 계산 및 Pagination 객체 생성
        int totalCount = postService.countSummaries(category);
        Pagination pagination = new Pagination(totalCount, rowNum, offset);

        // 2. 목록 및 페이지네이션 정보 전달
        model.addAttribute("postSummaries", postService.listSummaries(category, offset, rowNum));
        model.addAttribute("pagination", pagination); // [수정] 페이지네이션 객체 추가

        // 3. HTML 조각 반환
        return "fragments/postListContent :: postListContentFragment";
    }
}



