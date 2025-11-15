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

    // 분류별 강의 조회
    @GetMapping("/{target}/{method}/{category}")
    public ResponseEntity<List<LectureResponseDto>> getLectures(
            @PathVariable String target,
            @PathVariable String method,
            @PathVariable String category
    ) {
        List<LectureResponseDto> lectures = lectureService.getLectures(target, method, category);
        return ResponseEntity.ok(lectures);
    }

    // 강의 상세 조회
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailDto> getLectureDetail(
            @PathVariable Long lectureId
    ) {
        LectureDetailDto lecture = lectureService.getLectureDetail(lectureId);
        return ResponseEntity.ok(lecture);
    }
}
