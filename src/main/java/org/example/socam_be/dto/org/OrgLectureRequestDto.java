package org.example.socam_be.dto.org;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter @Setter
public class OrgLectureRequestDto {

    private String email;
    private String title;
    private String instructor;
    private String category;
    private String method;
    private String target;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String imageUrl;

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
