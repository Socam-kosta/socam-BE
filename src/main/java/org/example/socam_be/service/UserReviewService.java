package org.example.socam_be.service;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.review.Review;
import org.example.socam_be.domain.review.ReviewStatus;
import org.example.socam_be.dto.CertificateExtractDto;
import org.example.socam_be.dto.user.ReviewRequestDto;
import org.example.socam_be.dto.user.ReviewResponseDto;
import org.example.socam_be.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserReviewService {

  private final ReviewRepository reviewRepository;
  private final OcrService ocrService;  // ✅ OCR 자동 분석 서비스
  private final CertificateValidationService validationService; // ✅ OCR 결과 검증 서비스


  // 리뷰 등록
  public void createReview(ReviewRequestDto dto) {

    // ✅ (1) 별점 유효성 검사 (1~5점)
    if (dto.getStarRating() < 1 || dto.getStarRating() > 5) {
      throw new IllegalArgumentException("별점은 1~5점 사이의 정수만 가능합니다.");
    }

    // ✅ (2) 내용 유효성 검사 (50~500자)
    if (dto.getContents() == null || dto.getContents().length() < 50 || dto.getContents().length() > 500) {
      throw new IllegalArgumentException("리뷰 내용은 최소 50자 이상, 최대 500자 이하여야 합니다.");
    }

    // ✅ (3) 파일 업로드 필수 확인
    MultipartFile file = dto.getCertificateFile();
    if (file == null || file.isEmpty()) {
      throw new IllegalArgumentException("수료증 파일을 첨부해야 합니다.");
    }

    // ✅ (4) 파일 확장자 검사
    String originalName = file.getOriginalFilename();
    if (originalName == null || !originalName.matches(".*\\.(pdf|jpg|jpeg|png)$")) {
      throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. (pdf, jpg, jpeg, png만 가능)");
    }

    // ✅ (5) 파일 저장 (로컬 예시)
    String uploadDir = "uploads/review-certificates/";
    File directory = new File(uploadDir);
    if (!directory.exists()) directory.mkdirs();

    String savedFileName = UUID.randomUUID() + "_" + originalName;
    File destination = new File(uploadDir + savedFileName);
    try {
      file.transferTo(destination);
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 실패: " + e.getMessage());
    }
    // 3️⃣ OCR 분석 (CLOVA OCR 호출)
    CertificateExtractDto ocrResult = ocrService.extractText(destination);

    // 4️⃣ OCR 결과와 DB 비교 (수료증 진위 확인)
    boolean isValid = validationService.validateCertificate(
        ocrResult,
        dto.getEmail(),
        dto.getLectureId()
    );

    // 5️⃣ 결과에 따라 리뷰 상태 결정
    ReviewStatus status = isValid ? ReviewStatus.APPROVED : ReviewStatus.REJECTED;

    // ✅ (6) Review 객체 생성 및 저장
    Review review = Review.builder()
        .email(dto.getEmail())
        .lectureId(dto.getLectureId())
        .starRating(dto.getStarRating())
        .contents(dto.getContents())
        .filepath(destination.getPath())
        .status(ReviewStatus.PENDING)
        .isChecked(false)
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    reviewRepository.save(review);
  }
  // ✅ REV002 강의별 리뷰 조회
  public List<ReviewResponseDto> getReviewsByLecture(String lectureId) {
    List<Review> reviews = reviewRepository.findByLectureId(lectureId);
    return reviews.stream()
        .map(review -> ReviewResponseDto.builder()
            .email(review.getEmail())
            .starRating(review.getStarRating())
            .contents(review.getContents())
            .filepath(review.getFilepath())
            .createdAt(review.getCreatedAt())
            .build())
        .toList();
  }

  // ✅ REV003 리뷰 삭제 (작성자 본인만 가능)
  public void deleteReview(Long reviewId, String email) {
    Review review = reviewRepository.findById(reviewId)
        .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다."));

    if (!review.getEmail().equals(email)) {
      throw new IllegalArgumentException("본인이 작성한 리뷰만 삭제할 수 있습니다.");
    }

    reviewRepository.delete(review);
  }
  // ✅ [REV004] 마이페이지: 내가 쓴 리뷰 목록 조회
  public List<ReviewResponseDto> getMyReviews(String email) {
    List<Review> reviews = reviewRepository.findByEmail(email);

    return reviews.stream()
        .map(review -> ReviewResponseDto.builder()
            .email(review.getEmail())
            .starRating(review.getStarRating())
            .contents(review.getContents())
            .filepath(review.getFilepath())
            .createdAt(review.getCreatedAt())
            .build())
        .toList();
  }

}
