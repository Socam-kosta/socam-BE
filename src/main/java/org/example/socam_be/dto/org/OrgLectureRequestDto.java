package org.example.socam_be.dto.org;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

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

    // 기존 URL 등록용
    private String imageUrl;

    // 파일 업로드용
    private MultipartFile imageFile;

    private String region;
    private Boolean needCard;
    private Integer tuition;
    private Boolean supportAvailable;
    private String applicationProcess;
    private String eligibility;
    private String employmentSupport;
    private String curriculum;
}
