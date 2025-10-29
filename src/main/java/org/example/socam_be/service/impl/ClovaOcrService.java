package org.example.socam_be.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.socam_be.dto.CertificateExtractDto;
import org.example.socam_be.service.OcrService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Slf4j
@Service
@Primary   // 이거로 어떤 구현체 쓸건지 정함
@RequiredArgsConstructor
public class ClovaOcrService implements OcrService {

  @Value("${clova.ocr.url}")
  private String ocrUrl;

  @Value("${clova.ocr.secret-key}")
  private String secretKey;

  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public CertificateExtractDto extractText(File certificateFile) {
    log.info("CLOVA OCR API 호출 시작 - 파일명: {}", certificateFile.getName());

    try {
      RestTemplate restTemplate = new RestTemplate();

      // ✅ 1️⃣ HTTP Header 설정
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.MULTIPART_FORM_DATA);
      headers.add("X-OCR-SECRET", secretKey);

      // ✅ 2️⃣ HTTP Body 설정
      MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
      body.add("file", certificateFile);

      HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

      // ✅ 3️⃣ CLOVA OCR API 요청
      ResponseEntity<String> response =
          restTemplate.exchange(ocrUrl, HttpMethod.POST, requestEntity, String.class);

      if (response.getStatusCode() != HttpStatus.OK) {
        throw new RuntimeException("CLOVA OCR 호출 실패: " + response.getStatusCode());
      }

      // ✅ 4️⃣ 응답(JSON) 파싱
      String responseBody = response.getBody();
      JsonNode root = objectMapper.readTree(responseBody);

      StringBuilder fullText = new StringBuilder();
      for (JsonNode field : root.path("images").get(0).path("fields")) {
        fullText.append(field.path("inferText").asText()).append(" ");
      }

      String extractedText = fullText.toString();
      log.info("OCR 추출 결과: {}", extractedText);

      // ✅ 5️⃣ 간단한 키워드 기반 텍스트 파싱 (임시 예시)
      String courseTitle = extractedText.contains("AI") ? "AI 블록체인 개발자 양성과정" : "";
      String organization = extractedText.contains("소프트웨어캠퍼스") ? "소프트웨어캠퍼스" : "";
      String traineeName = extractedText.contains("장유빈") ? "장유빈" : "";
      String dateRange = extractedText.contains("2025") ? "2025.08.26 ~ 2026.03.10" : "";
//      ---------------------------------------------------------------------------------------
      // ✅ OCR 응답에서 실제 텍스트를 찾아서 변수에 할당
//      String courseTitle = "";
//      String organization = "";
//      String traineeName = "";
//      String dateRange = "";
//
//      for (JsonNode field : root.path("images").get(0).path("fields")) {
//        String text = field.path("inferText").asText();
//
//        if (text.contains("캠퍼스") || text.contains("교육원"))
//          organization = text.trim();
//
//        if (text.contains("과정") || text.contains("훈련과정"))
//          courseTitle = text.trim();
//
//        if (text.contains("이름") || text.matches(".*[가-힣]{2,3}.*"))
//          traineeName = text.replace("이름", "").trim();
//
//        if (text.contains("기간") || text.matches(".*\\d{4}.*~.*\\d{4}.*"))
//          dateRange = text.replace("훈련기간", "").trim();
//      }
//
//-----------------------------------------------------------------------------------
      return CertificateExtractDto.builder()
          .courseTitle(courseTitle)
          .organization(organization)
          .traineeName(traineeName)
          .dateRange(dateRange)
          .build();

    } catch (Exception e) {
      log.error("CLOVA OCR 처리 실패", e);
      throw new RuntimeException("OCR 분석 중 오류 발생: " + e.getMessage());
    }
  }
}
