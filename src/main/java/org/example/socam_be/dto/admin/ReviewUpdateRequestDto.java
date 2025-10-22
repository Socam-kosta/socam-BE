package org.example.socam_be.dto.admin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewUpdateRequestDto {
    private String status;  // ex) "APPROVED" or "REJECTED"
    private String reason;  // 선택: 거절 사유
}
