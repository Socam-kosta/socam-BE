package org.example.socam_be.dto.admin;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeRequestDto {
    private String adminEmail;  // 관리자 식별자 (FK)
    private String title;       // 제목
    private String contents;    // 내용
}
