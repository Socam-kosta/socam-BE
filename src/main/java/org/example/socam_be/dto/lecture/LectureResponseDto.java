package org.example.socam_be.dto.lecture;

import lombok.Builder;
import lombok.Getter;
import org.example.socam_be.domain.lecture.LectureStatus;

import java.time.LocalDate;

@Getter
@Builder
public class LectureResponseDto {
    private Long id; // 강의 식별자
    private String title; // 강의명
    private String instructor; // 강사명
    private String organization; // 기관명
    private String category; // 프론트엔드 / 백엔드 / AI 등
    private String method; // 온라인 / 오프라인
    private String target; // 재직자 / 취준생 등
    private LocalDate startDate; // 개강일
    private LocalDate endDate; // 종강일
    private LectureStatus status; // 상태(PENDING, APPROVED, REJECTED)
}
