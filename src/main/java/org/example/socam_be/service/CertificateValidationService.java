package org.example.socam_be.service;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.lecture.Lecture;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.dto.CertificateExtractDto;
import org.example.socam_be.repository.LectureRepository;
import org.example.socam_be.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CertificateValidationService {

  private final LectureRepository lectureRepository;
  private final UserRepository userRepository;

  public boolean validateCertificate(CertificateExtractDto ocrResult, String email, String lectureId) {

    // ✅ 1️⃣ 사용자 조회
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

    // ✅ 2️⃣ 강의 조회
    Lecture lecture = lectureRepository.findById(Long.parseLong(lectureId))
        .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

    // ✅ 3️⃣ OCR 결과와 실제 DB 데이터 비교
    boolean nameMatch = ocrResult.getTraineeName().contains(user.getName());
    boolean courseMatch = ocrResult.getCourseTitle().contains(lecture.getTitle());
    boolean orgMatch = ocrResult.getOrganization().contains(lecture.getOrganization());

    return nameMatch && courseMatch && orgMatch;
  }
}
