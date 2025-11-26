package org.example.socam_be.dto.admin;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureAdminDetailResponseDto {
    private Long lectureId;
    private String title;
    private String instructor;
    private String organization;
    private String email;
    private String orgName;
    private String category;
    private String method;
    private String target;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String status;
}
