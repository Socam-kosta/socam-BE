package org.example.socam_be.dto.org;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrgUpdateInfoReqDto {
    private String orgName;
    private String contact;
    private String certificatePath;
    private String password; // 변경 시에만 전달
}