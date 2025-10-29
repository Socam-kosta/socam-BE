package org.example.socam_be.service;

import org.example.socam_be.dto.CertificateExtractDto;
import java.io.File;

//(인터페이스)
public interface OcrService {
  CertificateExtractDto extractText(File certificateFile);
}
//org.example.socam_be/
//    ├── service/
//    │   ├── OcrService.java               ← 인터페이스
//    │   ├── impl/
//    │   │   ├── ClovaOcrService.java      ← 현재 사용 (외부 API)
//    │   │   └── LocalOcrService.java      ← 나중에 교체 (Tesseract 등)
//    └── dto/
//        └── CertificateExtractDto.java    ← OCR 결과 데이터 DTO
