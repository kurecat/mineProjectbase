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
import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.HashSet;


import java.util.List;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    /** 글쓰기 폼 */
    @GetMapping("/write")
    public String showWriteForm(Model model, HttpSession session) {
        // [1] 로그인 체크
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        model.addAttribute("postForm", new PostFormDto());
        return "write";   // write.html
    }

    /** 글 저장 처리 */
    @PostMapping("/save")
    public String savePost(
            @ModelAttribute("postForm") PostFormDto postFormDto,
            HttpSession session
    ) {
        // [1] 로그인 체크
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        // 게시글 Category 저장
        Long categoryId = postFormDto.getCategoryId();
        // 작성자 ID 저장
        postFormDto.setMemberId(loginMember.getId());

        // 저장 실행
        postService.save(postFormDto);
        System.out.println("선택된 카테고리 ID = " + categoryId);
        return "redirect:/";
    }

    /** 게시판 목록 */
    @GetMapping("/list")
    public String showBoardList(Model model) {
        model.addAttribute("posts", postService.findAll());
        return "postlist";
    }

    /** 게시글 상세 */
    @GetMapping("/detail/{id}")
    public String showDetail(
            @PathVariable Long id,
            @RequestParam(value="msg", required=false) String msg,
            Model model,
            HttpSession session
    ) {
        postService.increaseView(id);
        PostRes post = postService.findById(id); // get(id) -> findById(id) 로 통일 권장
        List<CommentRes> comments = commentService.list(id);

        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");

        model.addAttribute("post", post);
        model.addAttribute("comments", comments);
        model.addAttribute("commentWrite", new CommentCreateReq());
        model.addAttribute("loginMember", loginMember);

        return "postview"; // 상세페이지
    }

    // ... (댓글 수정, 추천, 검색 기능은 기존 유지) ...

    // ==================================================================
    // 🚀 [수정] 게시글 수정 화면 이동 (GET)
    // ==================================================================
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        // [1] 로그인 체크
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        // [2] 게시글 조회
        PostRes post = postService.findById(id);
        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "게시글이 존재하지 않습니다.");
        }

        // [3] 본인 확인 (로그인한 ID vs 글 작성자 ID)
        if (!post.getMemberId().equals(loginMember.getId())) {
            // 권한 없음: 상세 페이지로 돌려보내거나 에러 페이지 이동
            return "redirect:/board/detail/" + id + "?error=noPermission";
        }

        // [4] 수정 화면으로 데이터 전달
        // (write.html을 재사용하므로 "write" 리턴)
        model.addAttribute("post", post);
        return "write";
    }

    // ==================================================================
    // 🚀 [추가] 게시글 실제 수정 처리 (POST)
    // ==================================================================
    @PostMapping("/update")
    public String updatePost(@ModelAttribute PostFormDto postFormDto, HttpSession session) {
        // [1] 로그인 체크
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        // [2] DB 데이터 조회 (본인 확인용)
        PostRes existingPost = postService.findById(postFormDto.getId());

        // [3] 본인 확인 (해킹 방지: Form 조작해서 다른 사람 글 수정 시도 차단)
        if (!existingPost.getMemberId().equals(loginMember.getId())) {
            return "redirect:/board/detail/" + postFormDto.getId() + "?error=noPermission";
        }

        // [4] 업데이트 실행 (Service에 update 메서드가 있어야 함)
        // Service에서 dao.update(dto.getId(), dto.getTitle(), dto.getContent()) 호출
        postService.update(postFormDto.getId(), postFormDto.getTitle(), postFormDto.getContent());

        return "redirect:/board/detail/" + postFormDto.getId() + "?msg=updated";
    }

    // 2. 게시글 삭제 처리 (POST)
    @PostMapping("/delete/{id}")
    public String deletePost(@PathVariable Long id, HttpSession session) {
        // [1] 로그인 체크
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            return "redirect:/login";
        }

        // [2] 게시글 조회
        PostRes post = postService.findById(id);
        if (post == null) {
            return "redirect:/";
        }

        // [3] 권한 확인 (본인 확인 OR 관리자 확인)
        // 작성자가 아니고(AND) 등급이 2도 아니라면 -> 권한 없음
        boolean isAuthor = post.getMemberId().equals(loginMember.getId());
        boolean isAdmin = "관리자".equals(loginMember.getGrade());


        if (!isAuthor && !isAdmin) {
            // 본인도 아니고 관리자도 아니면 쫓아냄
            return "redirect:/board/detail/" + id + "?error=noPermission";
        }

        // [4] 삭제 실행
        postService.delete(id);
        return "redirect:/?msg=deleted";
    }

    // ==================================================================
    // 🚀 [수정/통합] 추천 기능 (AJAX 통신)
    // ==================================================================
    @PostMapping("/recommend/{id}")
    @ResponseBody
    public Map<String, Object> recommend(@PathVariable Long id, HttpSession session) {
        Map<String, Object> result = new HashMap<>();

        // [1] 로그인 체크
        MemberRes loginMember = (MemberRes) session.getAttribute("loginMember");
        if (loginMember == null) {
            result.put("success", false);
            result.put("message", "로그인이 필요한 서비스입니다.");
            return result;
        }

        // [2] 중복 추천 방지 (세션 이용)
        Set<Long> recommendedPosts = (Set<Long>) session.getAttribute("recommendedPosts");
        if (recommendedPosts == null) {
            recommendedPosts = new HashSet<>();
            session.setAttribute("recommendedPosts", recommendedPosts);
        }

        if (recommendedPosts.contains(id)) {
            result.put("success", false);
            result.put("message", "이미 추천했습니다!");
            return result;
        }

        try {
            // [3] 서비스 호출 (수정된 부분)
            // Service가 증가된 숫자를 int로 바로 리턴해주므로, 그걸 받아서 씁니다.
            int newCount = postService.increaseRecommendations(id);

            // 세션에 기록 (중복 방지)
            recommendedPosts.add(id);

            // [4] 결과 반환
            result.put("success", true);
            result.put("newCount", newCount); // 리턴받은 숫자 바로 사용

        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("message", "오류가 발생했습니다.");
        }

        return result;
    }
} // 클래스 끝