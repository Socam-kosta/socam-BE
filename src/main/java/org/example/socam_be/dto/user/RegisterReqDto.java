package org.example.socam_be.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterReqDto {
    private String email;
    private String password;
    private String name;
    private String nickname;
    private String role;
}
