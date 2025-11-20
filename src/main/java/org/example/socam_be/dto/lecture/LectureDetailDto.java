package org.example.socam_be.dto.lecture;

import lombok.Builder;
import lombok.Getter;
import org.example.socam_be.domain.lecture.LectureStatus;

import java.time.LocalDate;

@Getter
@Builder
public class LectureDetailDto {
    private String title;
    private String instructor;
    private String organization;
    private String category;
    private String method;
    private String target;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private LectureStatus status;

    private String region;
    private Boolean needCard;
//    private String ncs;
    private Integer tuition;
    private Boolean supportAvailable;
    private String applicationProcess;
    private String eligibility;
    private String employmentSupport;
    private String curriculum;
}

