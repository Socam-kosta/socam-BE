package org.example.socam_be.repository;

import org.example.socam_be.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    // 운영 기관 이메일 기준으로 강의조회
    List<Lecture> findByEmail(String email);
}
