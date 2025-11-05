package org.example.socam_be.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.user.ReviewRequestDto;
import org.example.socam_be.dto.user.ReviewResponseDto;
import org.example.socam_be.service.UserReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
    import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
@Validated
public class UserReviewController {

  private final UserReviewService userReviewService;
  /**
   * 리뷰 등록
   * - 별점: 1~5점 정수
   * - 내용: 50~500자
   * - 파일: PDF/JPG/PNG 필수
   */
  // ✅ [REV001] 리뷰 등록
  @PostMapping("/add")
  public ResponseEntity<String> addReview(
      @RequestPart("email") String email,
      @RequestPart("lectureId") String lectureId,
      @RequestPart("starRating") int starRating,
      @RequestPart("contents") String contents,
      @RequestPart("certificateFile") MultipartFile certificateFile
  ) {
    ReviewRequestDto dto = new ReviewRequestDto();
    dto.setEmail(email);
    dto.setLectureId(lectureId);
    dto.setStarRating(starRating);
    dto.setContents(contents);
    dto.setCertificateFile(certificateFile);

    userReviewService.createReview(dto);
    return ResponseEntity.ok("리뷰 등록 완료 (검증 중)");
  }

  // ✅ [REV002] 강의별 리뷰 조회
  @GetMapping("/lecture/{lectureId}")
  public ResponseEntity<List<ReviewResponseDto>> getReviewsByLecture(
      @PathVariable String lectureId
  ) {
    List<ReviewResponseDto> reviews = userReviewService.getReviewsByLecture(lectureId);
    return ResponseEntity.ok(reviews);
  }

  // ✅ [REV003] 리뷰 삭제 (본인 리뷰만 삭제 가능)
  @DeleteMapping("/delete/{reviewId}")
  public ResponseEntity<String> deleteReview(
      @PathVariable Long reviewId,
      @RequestParam("email") String email
  ) {
    userReviewService.deleteReview(reviewId, email);
    return ResponseEntity.ok("리뷰가 삭제되었습니다.");
  }
  // ✅ [REV004] 마이페이지 - 내가 쓴 리뷰 목록 조회
  @GetMapping("/user/{email}")
  public ResponseEntity<List<ReviewResponseDto>> getMyReviews(
      @PathVariable String email
  ) {
    List<ReviewResponseDto> reviews = userReviewService.getMyReviews(email);
    return ResponseEntity.ok(reviews);
  }

}
