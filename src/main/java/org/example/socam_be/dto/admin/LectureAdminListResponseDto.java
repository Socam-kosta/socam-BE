package org.example.socam_be.dto.admin;

import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LectureAdminListResponseDto {
    private Long lectureId;
    private String title;
    private String orgName;
    private String email;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
}
