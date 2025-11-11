package com.human.web_board.controller;
import com.human.web_board.dto.CommentCreateReq;
import com.human.web_board.dto.CommentRes;
import com.human.web_board.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController  // RestFull API 통신을 위한 어노테이션, 내부적으로 JSON 직결화/역직렬화 지원
@RequiredArgsConstructor // 생성자를 통해서 의존성 주입
@RequestMapping("/api/comments")  // CommentRestController의 경로
@Slf4j
public class CommentRestController {
    private final CommentService commentService;

    // 댓글 등록 : POST, 정보가 BODY에 포함
    @PostMapping
    public ResponseEntity<Long> create(@RequestBody CommentCreateReq req) {
        return ResponseEntity.ok(commentService.write(req));
    }

    // 댓글 목록 조회 : GET, 정보가 URL 경로에 포함됨
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<CommentRes>> listByPost(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.list(postId));
    }

    // 댓글 수정 : PUT, 정보가 BODY에 포함 됨
    @PutMapping("/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Long id, @RequestBody CommentCreateReq req) {
        return ResponseEntity.ok(commentService.update(req, id));
    }

    // 댓글 삭제 : DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Long id) {
        log.error("댓글 삭제 : {}", id);
        return ResponseEntity.ok(commentService.delete(id));
    }

}
