package org.example.socam_be.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserReqDto {
    private String email;
    private String name;
    private String nickname;
    private String password; // 선택적으로 수정 가능
}
