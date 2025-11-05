package org.example.socam_be.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.socam_be.domain.org.OrgStatus;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgResponseDto {
    private String email;
    private String password;
    private String orgName;
    private String contact;
    private LocalDateTime createdAt;
    private OrgStatus isApproved;
    private String certificatePath;
}
