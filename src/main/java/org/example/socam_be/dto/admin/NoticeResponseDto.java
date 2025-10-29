package org.example.socam_be.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeResponseDto {
    private Long noticeId;
    private String adminEmail;
    private String title;
    private String contents;
    private LocalDateTime regDate;
    private String status;
}
