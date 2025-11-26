package org.example.socam_be.dto.org;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class OrgRegisterRequestDto {

    @Schema(description = "운영기관 이메일", example = "org001@test.com")
    private String email;

    @Schema(description = "비밀번호", example = "password123")
    private String password;

    @Schema(description = "기관명", example = "서울직업교육원")
    private String orgName;

    @Schema(description = "기관 연락처", example = "010-1234-5678")
    private String contact;

    @Schema(description = "재직증명서 파일", example = "/uploads/cert/org001.pdf")
    private MultipartFile certificateFile;
}
