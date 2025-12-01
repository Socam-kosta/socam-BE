package org.example.socam_be.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserReqDto {
    // ✅ 닉네임만 수정 가능하도록 필드 정리
    private String nickname;
}
