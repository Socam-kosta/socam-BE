package org.example.socam_be.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LectureStatusUpdateRequestDto {
    private String status;  // APPROVED / REJECTED
    private String reason;  // optional
}
