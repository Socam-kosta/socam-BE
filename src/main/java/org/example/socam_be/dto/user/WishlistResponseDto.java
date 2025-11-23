package org.example.socam_be.dto.user;

import lombok.Builder;
import lombok.Getter;
import org.example.socam_be.dto.lecture.LectureResponseDto;

import java.time.LocalDate;

@Getter
@Builder
public class WishlistResponseDto {
    private Long id; // wishlist id
    private LectureInfo lecture; // 강의 정보

    @Getter
    @Builder
    public static class LectureInfo {
        private Long id;
        private String title;
        private String instructor;
        private String organization;
        private String imageUrl;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}

