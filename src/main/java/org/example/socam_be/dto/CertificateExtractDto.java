package org.example.socam_be.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CertificateExtractDto { //(OCR 결과 데이터)
  private String courseTitle;     // 과정명
  private String organization;    // 기관명
  private String dateRange;       // 교육기간
  private String traineeName;     // 수강생 이름
}
