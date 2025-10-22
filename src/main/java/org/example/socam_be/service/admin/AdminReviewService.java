package org.example.socam_be.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.review.Review;
import org.example.socam_be.domain.review.ReviewStatus;
import org.example.socam_be.dto.admin.ReviewResponseDto;
import org.example.socam_be.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReviewService {
    private final ReviewRepository reviewRepository;

    public List<ReviewResponseDto> getPendingReviews(){
        //1️⃣ DB에서 상태가 PENDING인 리뷰 조회
        List<Review> reviews = reviewRepository.findByStatus(ReviewStatus.PENDING);

        // 2️⃣ DTO로 변환
        return reviews.stream()
                .map(r -> ReviewResponseDto.builder()
                        .reviewId(r.getReviewId())
                        .email(r.getEmail())
                        .lectureId(r.getLectureId())
                        .starRating(r.getStarRating())
                        .contents(r.getContents())
                        .status(r.getStatus().name())
                        .createdAt(r.getCreatedAt())
                        .build())
                .toList();

    }
}
