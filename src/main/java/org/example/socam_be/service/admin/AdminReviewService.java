package org.example.socam_be.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.review.Review;
import org.example.socam_be.domain.review.ReviewStatus;
import org.example.socam_be.dto.admin.ReviewResponseDto;
import org.example.socam_be.dto.admin.ReviewUpdateRequestDto;
import org.example.socam_be.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReviewService {

    private final ReviewRepository reviewRepository;

    // ✅ [ADM004] 상태가 PENDING인 리뷰 목록 조회
    // ================================================================
    // 📌 추가된 기능: 관리자 화면에서 OCR 1차 검증 후 대기 중인 리뷰들을 조회
    //      - 이후 관리자가 실제 수료증 파일을 보고 승인/반려를 결정할 때 사용
    public List<ReviewResponseDto> getPendingReviews() {
        List<Review> reviews = reviewRepository.findByStatus(ReviewStatus.PENDING);


        // DTO 변환
        return reviews.stream()
            .map(r -> ReviewResponseDto.builder()
                .reviewId(r.getReviewId())
                .email(r.getEmail())
                .lectureId(r.getLectureId())
                .starRating(r.getStarRating())
                .contents(r.getContents())
                // ✅ [추가 가능] 수료증 파일 경로 표시 (관리자 미리보기용)
                .filepath(r.getFilepath())
                .status(r.getStatus().name())
                .createdAt(r.getCreatedAt())
                .build())
            .toList();
    }

    // =====================================================================
    // ✅ [ADM005] 리뷰 승인/거절 처리 로직 (PATCH /api/reviews/{reviewId}/status)
    // =====================================================================
    // 📌 추가된 기능:
    //      - 관리자가 리뷰 상태를 직접 변경할 수 있는 기능
    //      - 금지어 자동 감지로 부적절한 리뷰는 자동 반려 처리
    //      - 검수 완료 시각 기록
    @Transactional
    public ReviewResponseDto updateReviewStatus(Long reviewId, ReviewUpdateRequestDto requestDto) {
        // 1️⃣ 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new IllegalArgumentException("리뷰를 찾을 수 없습니다. ID: " + reviewId));

        // 2️⃣ 금지어 자동 거절 처리
        if (containsForbiddenWords(review.getContents())) {
            review.setStatus(ReviewStatus.REJECTED);
        }
        // 3️⃣ 관리자가 직접 승인/거절한 경우
        else {
            review.setStatus(ReviewStatus.valueOf(requestDto.getStatus().toUpperCase()));
        }

        // 4️⃣ 검수 완료 및 업데이트 시간 변경
        review.setIsChecked(true);
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(review);

        // 5️⃣ 응답 DTO 생성 후 반환
        return ReviewResponseDto.builder()
            .reviewId(review.getReviewId())
            .email(review.getEmail())
            .lectureId(review.getLectureId())
            .starRating(review.getStarRating())
            .contents(review.getContents())
            .isChecked(review.getIsChecked() != null ? review.getIsChecked() : false)
            .status(review.getStatus().name())
            .createdAt(review.getCreatedAt())
            .updatedAt(review.getUpdatedAt())
            .build();
    }

    // =====================================================================
    // ✅ 금지어 자동 거절 함수 (음란성/광고성 단어 감지)
    // =====================================================================
    private boolean containsForbiddenWords(String contents) {
        String[] forbiddenWords = {"광고", "음란", "성인", "불법", "19금"};
        for (String word : forbiddenWords) {
            if (contents != null && contents.contains(word)) {
                return true;
            }
        }
        return false;
    }
    // ===============================================================
// ✅ [ADM006] 상태별 리뷰 목록 조회 (APPROVED / REJECTED / PENDING)
// ===============================================================
    @Transactional(readOnly = true)
    public List<ReviewResponseDto> getReviewsByStatus(ReviewStatus status) {
        return reviewRepository.findByStatus(status).stream()
            .map(ReviewResponseDto::new)
            .toList();
    }

}
