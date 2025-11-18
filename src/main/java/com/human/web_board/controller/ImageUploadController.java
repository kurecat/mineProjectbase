package com.human.web_board.controller; // ◀ 본인의 컨트롤러 패키지 경로로 수정

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController // ◀ JSON을 반환하므로 @RestController
public class ImageUploadController {

    // application.properties에서 설정한 파일 저장 경로를 가져옵니다.
    @Value("${app.upload.dir}")
    private String uploadDir;

    @PostMapping("/upload-images-dragdrop")
    public ResponseEntity<?> uploadDragDropImages(@RequestParam("images") List<MultipartFile> images) {

        if (images == null || images.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "이미지가 없습니다."));
        }

        List<String> uploadedUrls = new ArrayList<>();

        try {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    // 1. 고유한 파일명 생성
                    String originalName = image.getOriginalFilename();
                    String extension = "";
                    if (originalName != null && originalName.contains(".")) {
                        extension = originalName.substring(originalName.lastIndexOf("."));
                    }
                    String savedName = UUID.randomUUID().toString() + extension;

                    // 2. 실제 파일 저장 경로 계산
                    // (WebConfig의 uploadDir 경로와 일치해야 함)
                    Path destinationFile = Paths.get(uploadDir, savedName).toAbsolutePath();

                    // 3. 파일 저장
                    image.transferTo(destinationFile.toFile());

                    // 4. 클라이언트가 접근할 웹 URL 생성 (WebConfig의 /uploads/ 와 일치)
                    String imageUrl = "/uploads/" + savedName;
                    uploadedUrls.add(imageUrl);
                }
            }

            // 5. 성공 시, JSON으로 URL 목록 반환
            // JS의 fetch().then(data => ...)에서 data.urls로 이 목록을 받습니다.
            return ResponseEntity.ok(Map.of("urls", uploadedUrls));

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "이미지 업로드 실패: " + e.getMessage()));
        }
    }
}