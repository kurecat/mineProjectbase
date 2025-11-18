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

/**
 * 게시판 관련 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/board") // "/board"로 시작하는 모든 요청 처리
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /**
     * 글쓰기 폼(write.html)을 보여주는 메소드 (GET)
     */
    @GetMapping("/write")
    public String showWriteForm(Model model) {
        // 폼 바인딩용 DTO
        model.addAttribute("postForm", new PostFormDto());
        return "write";   // templates/write.html
    }

    /**
     * 작성된 글쓰기 폼을 제출(submit)받는 메소드 (POST)
     */
    @PostMapping("/write")
    public String handleSubmitWriteForm(
            @ModelAttribute("postForm") PostFormDto postFormDto,
            HttpSession session
    ) {
        // 로그인한 사용자만 글쓰기 허용 (필요 없다면 이 if 문 제거해도 됨)
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        // TODO: PostFormDto → PostCreateReq 변환해서 저장하거나
        //       PostService에 save(PostFormDto) 메소드 구현
        postService.save(postFormDto);

        return "redirect:/board/list";
    }

    /**
     * 게시판 목록 페이지
     */
    @GetMapping("/list")
    public String showBoardList(Model model) {
        // TODO: 필요에 따라 페이징으로 변경 가능
        model.addAttribute("posts", postService.findAll());
        // list.html 또는 post/list.html 중 네가 쓰는 템플릿 이름에 맞춰 바꿔도 됨
        return "postlist";  // 현재 네 코드 기준 유지
    }

    /**
     * 게시물 상세 페이지
     * - 조회수 증가
     * - 게시물 정보
     * - 댓글 목록
     * - 로그인 회원 정보(템플릿에서 사용 가능)
     */
    @GetMapping("/detail/{id}")
    public String showDetail(
            @PathVariable Long id,
            @RequestParam(value="msg", required=false) String msg,
            Model model,
            HttpSession session
    ) {
        // 조회수 증가
        postService.increaseView(id);

        // 게시글 가져오기
        PostRes post = postService.get(id);

        // 댓글 목록 가져오기
        List<CommentRes> comments = commentService.list(id);
        model.addAttribute("comments", comments);

        // 로그인 회원 (있으면)
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentWrite", new CommentCreateReq());
        model.addAttribute("loginMember", loginMember); // 템플릿에서 로그인 여부 판단 가능

        // detail 화면 템플릿 이름
        // 현재 프로젝트에 post/detail.html 이 있으니까 그걸 쓰고 싶으면 "post/detail" 로 변경
        return "postview";
//        if (msg!= null){
//            return "postview";
//        }else {
//            return "postview";
//        }
        // return "post/detail";
    }

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

        // 본인만 수정 가능
        if (!loginMember.getId().equals(comment.getMemberId())) {
            return "redirect:/board/detail/" + postId + "?error=noPermission";
        }

        // 댓글 업데이트
        CommentCreateReq req = new CommentCreateReq();
        req.setContent(content);
        commentService.update(req, commentId);

        return "redirect:/board/detail/" + postId + "?msg=edited";
    }


    /**
     * 게시물 추천(좋아요) 기능
     * - /board/detail/{id}/recommend
     * - detail.html 에서 fetch 로 호출하는 URL 과 일치하도록 구현
     * - 로그인한 사용자만 추천 가능
     */
    @PostMapping("/detail/{id}/recommend")
    @ResponseBody
    public int recommend(
            @PathVariable Long id,
            HttpSession session
    ) {
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            // 비로그인이면 401 에러 반환 → JS에서 처리 가능
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다.");
        }

        // 추천 수 +1 하고, 증가된 추천 수를 반환
        return postService.increaseRecommendations(id);
    }

    // 게시물 검색 기능
    @GetMapping("/search")
    public String searchPosts(
            @RequestParam(required = false) Long mainCategoryId,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int rowNum,
            Model model, HttpSession session) {
        // 서비스에서 전체 목록 가져와서 모델에 추가
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
