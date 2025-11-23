package org.example.socam_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.service.S3Uploader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private final S3Uploader s3Uploader;

    @PostMapping("/upload")
    public ResponseEntity<String> upload(@RequestPart MultipartFile file) throws IOException {
        String url = s3Uploader.upload(file, "uploads");
        return ResponseEntity.ok(url);
    }
}
