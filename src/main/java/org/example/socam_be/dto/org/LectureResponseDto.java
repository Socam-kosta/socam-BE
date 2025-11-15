package org.example.socam_be.dto.org;

import lombok.Builder;
import lombok.Getter;
import org.example.socam_be.domain.lecture.LectureStatus;

@Getter
@Builder
public class LectureResponseDto {
    private Long id; // 강의 식별자
    private String title; // 강의명
    private LectureStatus status; // 상태(PENDING, APPROVED, REJECTED)
}
