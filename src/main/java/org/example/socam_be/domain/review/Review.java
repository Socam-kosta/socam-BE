package org.example.socam_be.domain.review;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(nullable = false)
    private String email; //작성자 이메일

    @Column(nullable = false)
    private String lectureId; // 강의 ID (FK)

    private LocalDate sign; //수강일

    private String filepath; // 수료증 경로

    private int starRating; // 별점

    @Column(length = 1000)
    private String contents; // 리뷰내용

    @Column(name = "is_checked")
    private boolean isChecked; // 관리자 검수여부

    @Enumerated(EnumType.STRING)
    private ReviewStatus status; //상태(PENDING, APPROVED, REJECTED)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.status = this.status == null ? ReviewStatus.PENDING : this.status;
    }
}
