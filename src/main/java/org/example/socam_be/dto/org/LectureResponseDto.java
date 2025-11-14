package org.example.socam_be.dto.org;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LectureResponseDto {
    private Long id; // 강의 식별자
    private String title; // 강의명
}
