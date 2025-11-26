package org.example.socam_be.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReviewRequestDto {

  private String email;          // 작성자 이메일 (로그인 유저)
  private String lectureId;      // 강의 ID (나중에 OCR로 검증)
  private int starRating;        // 별점 (1~5 정수만 허용)
  private String contents;       // 리뷰 내용 (50~500자 제한)
  private MultipartFile certificateFile; // 수료증 파일 (PDF, JPG, PNG)

}
