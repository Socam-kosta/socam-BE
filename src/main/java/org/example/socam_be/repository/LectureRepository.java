package org.example.socam_be.repository;

import org.example.socam_be.domain.lecture.Lecture;
import org.example.socam_be.domain.lecture.LectureStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    // 운영 기관 이메일 기준으로 강의조회
    List<Lecture> findByEmail(String email);

    // 관리자: 상태 기반 조회 (PENDING, APPROVED, REJECTED)
    List<Lecture> findByStatus(LectureStatus status);

    // 관리자: 강의 상세 조회/승인/거절 처리 시 필요
    Optional<Lecture> findById(Long id);
}
