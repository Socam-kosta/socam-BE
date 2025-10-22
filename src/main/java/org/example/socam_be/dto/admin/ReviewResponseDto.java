package org.example.socam_be.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.socam_be.domain.review.ReviewStatus;

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
}
