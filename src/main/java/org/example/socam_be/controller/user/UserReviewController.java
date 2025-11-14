package org.example.socam_be.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Review API", description = "강의 리뷰 관련 기능")
public class UserReviewController {

  private final UserReviewService userReviewService;

  @Operation(summary = "리뷰 등록", description = "별점, 내용, 수료증 파일을 포함하여 리뷰를 등록합니다.")
  @PostMapping("/add")
  public ResponseEntity<String> addReview(
      @Parameter(description = "사용자 이메일") @RequestPart("email") String email,
      @Parameter(description = "강의 ID") @RequestPart("lectureId") String lectureId,
      @Parameter(description = "별점(1~5)") @RequestPart("starRating") int starRating,
      @Parameter(description = "리뷰 내용") @RequestPart("contents") String contents,
      @Parameter(description = "수료증 파일(PDF/JPG/PNG)") @RequestPart("certificateFile") MultipartFile certificateFile
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

  @Operation(summary = "강의별 리뷰 조회", description = "특정 강의의 리뷰 목록을 조회합니다.")
  @GetMapping("/lecture/{lectureId}")
  public ResponseEntity<List<ReviewResponseDto>> getReviewsByLecture(
      @Parameter(description = "강의 ID") @PathVariable String lectureId
  ) {
    List<ReviewResponseDto> reviews = userReviewService.getReviewsByLecture(lectureId);
    return ResponseEntity.ok(reviews);
  }

  @Operation(summary = "리뷰 삭제", description = "본인이 작성한 리뷰만 삭제할 수 있습니다.")
  @DeleteMapping("/delete/{reviewId}")
  public ResponseEntity<String> deleteReview(
      @Parameter(description = "리뷰 ID") @PathVariable Long reviewId,
      @Parameter(description = "사용자 이메일") @RequestParam("email") String email
  ) {
    userReviewService.deleteReview(reviewId, email);
    return ResponseEntity.ok("리뷰가 삭제되었습니다.");
  }

  @Operation(summary = "내 리뷰 목록 조회", description = "마이페이지에서 내가 작성한 리뷰 목록을 조회합니다.")
  @GetMapping("/user/{email}")
  public ResponseEntity<List<ReviewResponseDto>> getMyReviews(
      @Parameter(description = "사용자 이메일") @PathVariable String email
  ) {
    List<ReviewResponseDto> reviews = userReviewService.getMyReviews(email);
    return ResponseEntity.ok(reviews);
  }
}
