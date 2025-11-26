package org.example.socam_be.dto.org;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgUpdateInfoReqDto {

    @Schema(description = "기관명", example = "서울직업교육원")
    private String orgName;

    @Schema(description = "기관 연락처", example = "010-1234-5678")
    private String contact;

    @Schema(description = "증명서 파일 경로", example = "/uploads/cert/org001.pdf")
    private String certificatePath;

    @Schema(description = "새 비밀번호 (선택)", example = "newPassword123")
    private String password;
}
