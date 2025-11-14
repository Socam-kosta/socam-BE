package org.example.socam_be.dto.org;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.socam_be.domain.org.Org;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrgResponseDto {
    private String email;
    private String orgName;
    private String contact;
    private LocalDateTime createdAt;
    private String status;
    private String certificatePath;

    public OrgResponseDto(Org org) {
        this.email = org.getEmail();
        this.orgName = org.getOrgName();
        this.contact = org.getContact();
        this.certificatePath = org.getCertificatePath();
        this.status = org.getStatus().name();
    }
}
