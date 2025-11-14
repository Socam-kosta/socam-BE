package org.example.socam_be.dto.org;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgRegisterRequestDto {
    private String email;
    private String password;
    private String orgName;
    private String contact;
    private String certificatePath;
}