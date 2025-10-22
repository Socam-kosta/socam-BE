package org.example.socam_be.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.admin.ReviewResponseDto;
import org.example.socam_be.service.admin.AdminReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/reviews") //관리자 리뷰 API
@RequiredArgsConstructor
public class AdmiReviewController {

    private final AdminReviewService adminReviewService;

    //✅  [GET] 상태가 PENDING인 리뷰목록조회
    @GetMapping
    public ResponseEntity<List<ReviewResponseDto>> getPendingReviews(){
        List<ReviewResponseDto> reviews = adminReviewService.getPendingReviews();
        return ResponseEntity.ok(reviews);
    }
}
