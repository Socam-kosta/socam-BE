package org.example.socam_be.dto.org;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgLoginRequestDto {

    @Schema(description = "운영기관 이메일", example = "org001@test.com")
    private String email;

    @Schema(description = "비밀번호", example = "password123")
    private String password;
}
