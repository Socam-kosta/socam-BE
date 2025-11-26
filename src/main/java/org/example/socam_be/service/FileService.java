package org.example.socam_be.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Uploader s3Uploader;

    public String uploadCertificate(MultipartFile file) {
        try {
            return s3Uploader.upload(file, "review-certificates");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // ⭐ 운영기관 재직증명서 업로드
    public String uploadOrgCertificate(MultipartFile file) {
        try {
            return s3Uploader.upload(file, "org-certificates");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadLectureImage(MultipartFile file) {
        try {
            return s3Uploader.upload(file, "lecture-images");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}