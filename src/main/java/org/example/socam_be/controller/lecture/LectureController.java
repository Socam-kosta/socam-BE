package org.example.socam_be.controller.lecture;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.lecture.LectureDetailDto;
import org.example.socam_be.dto.lecture.LectureResponseDto;
import org.example.socam_be.service.lecture.LectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lecture")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService lectureService;

    /**
     * 분류별 강의 조회
     * 예: /api/lecture/jobseeker/온라인/백엔드
     */
    @GetMapping("/{target}/{method}/{category}")
    public ResponseEntity<List<LectureResponseDto>> getLectures(
            @PathVariable String target,
            @PathVariable String method,
            @PathVariable String category
    ) {
        return ResponseEntity.ok(
                lectureService.getLectures(target, method, category)
        );
    }

    /**
     * 강의 상세 조회
     * (일반 유저와 운영기관 모두 접근 가능)
     */
    @GetMapping("/detail/{lectureId}")
    public ResponseEntity<LectureDetailDto> getLectureDetail(
            @PathVariable Long lectureId
    ) {
        return ResponseEntity.ok(lectureService.getLectureDetail(lectureId));
    }
}
