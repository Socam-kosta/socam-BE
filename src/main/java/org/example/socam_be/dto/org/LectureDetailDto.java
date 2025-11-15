package org.example.socam_be.dto.org;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class LectureDetailDto {
    private String title; // 강의명
    private String instructor; // 강사명
    private String organization; // 기관명
    private String category; // 프론트엔드 / 백엔드 / AI 등
    private String method; // 온라인 / 오프라인
    private String target; // 재직자 / 취준생 등
    private LocalDate startDate; // 개강일
    private LocalDate endDate; // 종강일
    private String description; // 간단 설명
}
