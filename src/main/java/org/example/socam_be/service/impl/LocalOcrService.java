package org.example.socam_be.service.impl;

import org.example.socam_be.dto.CertificateExtractDto;
import org.example.socam_be.service.OcrService;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class LocalOcrService implements OcrService {

  @Override
  public CertificateExtractDto extractText(File certificateFile) {
    // TODO: 나중에 Tesseract OCR 연동 시 구현
    return null;
  }
}
//“ClovaOcrService”의 @Primary를 지우고,
//“LocalOcrService”에 @Primary를 붙이면
//자동으로 로컬 OCR로 교체됩니다.