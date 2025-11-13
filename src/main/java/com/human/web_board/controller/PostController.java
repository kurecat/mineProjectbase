package com.human.web_board.controller;

import com.human.web_board.dto.PostFormDto;
import com.human.web_board.dto.PostRes;
import com.human.web_board.dto.PostSummaryRes;
import com.human.web_board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 게시판 관련 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/board") // "/board"로 시작하는 모든 요청 처리
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    /**
     * 글쓰기 폼(write.html)을 보여주는 메소드 (GET)
     */
    @GetMapping("/write")
    public String showWriteForm(Model model) {

        // 폼 바인딩용 DTO
        model.addAttribute("postForm", new PostFormDto());

        return "write";
    }

    /**
     * 작성된 글쓰기 폼을 제출(submit)받는 메소드 (POST)
     */
    @PostMapping("/write")
    public String handleSubmitWriteForm(@ModelAttribute("postForm") PostFormDto postFormDto) {

        // DTO 바인딩 확인용 로그
        System.out.println("--- 폼 데이터 도착 ---");
        System.out.println("제목: " + postFormDto.getTitle());
        System.out.println("내용: " + postFormDto.getContent());
        System.out.println("댓글 허용: " + postFormDto.isAllowComments());
        System.out.println("기타 옵션: " + postFormDto.isExtraOption());
        System.out.println("---------------------");

        // TODO: DB 저장
        // postService.save(postFormDto);

        return "redirect:/board/list";
    }

    /**
     * 게시판 목록 페이지
     */
    @GetMapping("/list")
    public String showBoardList(Model model) {
        // TODO: 게시물 목록을 모델에 추가
        // model.addAttribute("posts", postService.findAll());
        return "list";
    }

    /**
     * 게시물 상세 페이지
     */
    @GetMapping("/detail/{id}")
    public String showDetail(@PathVariable Long id, Model model) {
        //조회수 증가
        postService.increaseView(id);
        PostRes post = postService.get(id);
        model.addAttribute("post",post);
        // TODO: 상세 정보 + 댓글 조회
        // model.addAttribute("post", postService.getById(id));
        return "detail";
    }

    /**
     * ⭐ AJAX 기반 검색 기능 (페이지 이동 없음)
     * - 게시물 목록 화면(list.html)
     * - 게시물 상세 화면(detail.html)
     * 두 곳에서 모두 사용 가능
     */
    @GetMapping("/search-ajax")
    @ResponseBody
    public List<PostSummaryRes> searchAjax(@RequestParam String query) {

        // 제목/내용에 query가 포함된 게시물 10개 검색
        return postService.searchList(query, 0, 10);
    }
}
