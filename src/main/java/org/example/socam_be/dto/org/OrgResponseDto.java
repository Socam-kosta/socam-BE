package org.example.socam_be.dto.org;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.example.socam_be.domain.org.Org;

@Getter
public class OrgResponseDto {

    @Schema(description = "이메일", example = "org001@test.com")
    private final String email;

    @Schema(description = "기관명", example = "서울직업교육원")
    private final String orgName;

    @Schema(description = "기관 연락처", example = "010-1234-5678")
    private final String contact;

    @Schema(description = "증명서 파일 경로", example = "/uploads/cert/org001.pdf")
    private final String certificatePath;

    @Schema(description = "승인 상태", example = "PENDING")
    private final String status;

    public OrgResponseDto(Org org) {
        this.email = org.getEmail();
        this.orgName = org.getOrgName();
        this.contact = org.getContact();
        this.certificatePath = org.getCertificatePath();
        this.status = org.getStatus().name();
    }
}
