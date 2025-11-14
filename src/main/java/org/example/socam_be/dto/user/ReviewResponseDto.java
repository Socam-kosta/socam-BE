package org.example.socam_be.dto.user;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReviewResponseDto {

  private String email;        // 작성자 이메일
  private int starRating;      // 별점
  private String contents;     // 리뷰 내용
  private String filepath;     // 수료증 파일 경로 (옵션)
  private LocalDateTime createdAt; // 작성일
}
