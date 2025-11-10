package com.human.web_board.controller;

import com.human.web_board.dto.PostFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 게시판 관련 요청을 처리하는 컨트롤러
 */
@Controller
@RequestMapping("/board") // "/board"로 시작하는 모든 요청을 이 컨트롤러가 처리
public class PostController {

    /**
     * 글쓰기 폼(write.html)을 보여주는 메소드 (GET 요청)
     *
     * @param model Thymeleaf 템플릿으로 데이터를 전달하기 위한 객체
     * @return 템플릿 파일 이름 ("write")
     */
    @GetMapping("/write")
    public String showWriteForm(Model model) {

        // 1. 폼(th:object)에 바인딩할 비어있는 DTO 객체를 생성합니다.
        PostFormDto postFormDto = new PostFormDto();

        // 2. 모델에 "postForm"이라는 이름으로 DTO를 추가합니다.
        //    (이 이름은 write.html의 <form th:object="${postForm}" ...>와 일치해야 합니다)
        model.addAttribute("postForm", postFormDto);

        // 3. resources/templates/write.html 파일을 렌더링하여 반환합니다.
        return "write";
    }

    /**
     * 작성된 글쓰기 폼을 제출(submit)받는 메소드 (POST 요청)
     *
     * @param postFormDto 폼에서 전송된 데이터가 자동으로 바인딩된 DTO 객체
     * @return 처리가 완료된 후 리다이렉트(redirect)할 URL
     */
    @PostMapping("/write")
    public String handleSubmitWriteForm(@ModelAttribute("postForm") PostFormDto postFormDto) {

        // 1. @ModelAttribute가 DTO에 폼 데이터를 자동으로 채워줍니다.
        //    (SmartEditor 2.0의 onsubmit 스크립트 덕분에 content 내용도 포함됩니다)

        // 2. 데이터가 서버로 잘 전송되었는지 확인 (IntelliJ 콘솔에 출력됨)
        System.out.println("--- 폼 데이터가 컨트롤러에 도착했습니다 ---");
        System.out.println("제목 (title): " + postFormDto.getTitle());
        System.out.println("내용 (content): " + postFormDto.getContent());
        System.out.println("댓글 허용 (allowComments): " + postFormDto.isAllowComments());
        System.out.println("기타 (extraOption): " + postFormDto.isExtraOption());
        System.out.println("---------------------------------------");

        // 3. (필수) 이 부분에서 Service 레이어를 호출하여 DB에 데이터를 저장합니다.
        // 예: postService.saveNewPost(postFormDto);

        // 4. 저장이 완료되면, 게시판 목록 페이지로 사용자를 이동시킵니다.
        //    (새로고침 시 폼이 중복 제출되는 것을 방지하기 위해 "redirect:"를 사용합니다)
        return "redirect:/board/list"; // (게시판 목록 페이지의 URL로 변경하세요)
    }

    // (참고) 게시판 목록 페이지를 위한 GET 매핑 (예시)
    @GetMapping("/list")
    public String showBoardList(Model model) {
        // ... (목록을 불러오는 로직) ...
        return "list"; // (게시판 목록 템플릿 이름. 예: list.html)
    }
}