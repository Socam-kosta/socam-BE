package org.example.socam_be.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.review.ReviewStatus;
import org.example.socam_be.dto.admin.ReviewResponseDto;
import org.example.socam_be.dto.admin.ReviewUpdateRequestDto;
import org.example.socam_be.service.admin.AdminReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reviews") // 관리자 리뷰 API
@RequiredArgsConstructor
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    // ✅ [ADM004] 상태가 PENDING인 리뷰목록 조회
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getPendingReviews() {
        List<ReviewResponseDto> reviews = adminReviewService.getPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    // =====================================================================
    // ✅ [ADM005] 리뷰 승인/거절 처리 (PATCH /api/reviews/{reviewId}/status)
    // =====================================================================
    @PatchMapping("/{reviewId}/status")
    public ResponseEntity<ReviewResponseDto> updateReviewStatus(
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto requestDto
    ) {
        ReviewResponseDto response = adminReviewService.updateReviewStatus(reviewId, requestDto);
        return ResponseEntity.ok(response);
    }
    // ===============================================================
    // ✅ [ADM006] 상태별 리뷰 목록 조회 (APPROVED / REJECTED / PENDING)
    // ===============================================================
    // 📌 관리자가 특정 상태별로 리뷰를 필터링해서 조회
    //     예시 요청: GET /api/admin/reviews/status/APPROVED
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByStatus(@PathVariable ReviewStatus status) {
        List<ReviewResponseDto> reviews = adminReviewService.getReviewsByStatus(status);
        return ResponseEntity.ok(reviews);
    }

}
