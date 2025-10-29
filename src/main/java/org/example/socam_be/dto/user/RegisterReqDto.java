package org.example.socam_be.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.example.socam_be.domain.user.User;

@Getter
@Setter
public class RegisterReqDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String role; // USER, ADMIN, ORG
    private String orgName; // 기관명 (ORG 전용)
    private String contact; // 기관 연락처 (ORG 전용)
    private String certificatePath; // 재직자 증명서 경로 (선택)

}
