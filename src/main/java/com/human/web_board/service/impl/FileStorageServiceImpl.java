package com.human.web_board.service.impl;

import com.human.web_board.service.FileStorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
class FileStorageServiceImpl implements FileStorageService {
    @Value("${app.upload.dir}")
    private String uploadDir;

    @Override
    public String saveImage(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) return null;

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        try {
            Path dir = Paths.get(uploadDir, subDir);
            Files.createDirectories(dir);

            String original = file.getOriginalFilename();
            String ext = "";
            if (original != null && original.lastIndexOf('.') > -1) {
                ext = original.substring(original.lastIndexOf('.')); // .jpg
            }

            String fileName = UUID.randomUUID().toString().replace("-", "") + ext;
            Path target = dir.resolve(fileName);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("업로드 경로: " + uploadDir);
            System.out.println("파일 저장됨: " + target.toAbsolutePath());
            System.out.println("존재 여부: " + target.toFile().exists());

            return subDir + "/" + fileName; // DB에는 상대경로만 저장
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + e.getMessage(), e);
        }

    }

    /* 기존 파일 삭제(교체 시 사용) */
    public void deleteIfExists(String relativePath) {
        if (relativePath == null || relativePath.isBlank()) return;
        try {
            Path p = Paths.get(uploadDir, relativePath);
            Files.deleteIfExists(p);
        } catch (IOException ignore) {}
    }
}

