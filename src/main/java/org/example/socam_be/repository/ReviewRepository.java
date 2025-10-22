package org.example.socam_be.repository;

import org.example.socam_be.domain.review.Review;
import org.example.socam_be.domain.review.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    //✅ 상태(status) 기준으로 리뷰조회 (예: PENDING, APPROVED, REJECTED)
    List<Review> findByStatus(ReviewStatus status);

    //✅ 이메일 기준으로 리뷰조회 (USER 파트에서도 사용가능)
    List<Review> findByEmail(String email);
}
