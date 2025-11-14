package com.human.web_board.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String saveImage(MultipartFile file, String subDir);
    void deleteIfExists(String relativePath);
}