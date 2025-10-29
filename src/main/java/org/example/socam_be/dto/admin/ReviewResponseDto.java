package org.example.socam_be.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.socam_be.domain.review.Review;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponseDto {
    private Long reviewId;
    private String email;
    private String lectureId;
    private LocalDate sign;
    private String filepath;
    private int starRating;
    private String contents;
    private boolean isChecked;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ✅ Review 엔티티 → DTO 변환 생성자
    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.email = review.getEmail();
        this.lectureId = review.getLectureId();
        this.sign = review.getSign();
        this.filepath = review.getFilepath();
        this.starRating = review.getStarRating();
        this.contents = review.getContents();
        this.isChecked = review.isChecked();
        this.status = review.getStatus().name();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
