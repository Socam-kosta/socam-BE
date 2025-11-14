package org.example.socam_be.dto.org;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgLoginRequestDto {
    private String email;
    private String password;
}