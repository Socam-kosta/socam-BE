package org.example.socam_be.controller.org;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.lecture.LectureStatus;
import org.example.socam_be.dto.lecture.LectureDetailDto;
import org.example.socam_be.dto.org.OrgLectureRequestDto;
import org.example.socam_be.dto.lecture.LectureResponseDto;
import org.example.socam_be.service.org.OrgLectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/org/lecture")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ORG')")

public class OrgLectureController {

    private final OrgLectureService orgLectureService;

    // [ORG002] 강의 등록 요청
    @PostMapping("/add")
    public ResponseEntity<String> addLecture(
            @RequestPart("email") String email,
            @RequestPart("title") String title,
            @RequestPart("instructor") String instructor,
            @RequestPart("category") String category,
            @RequestPart("method") String method,
            @RequestPart("target") String target,
            @RequestPart("startDate") LocalDate startDate,
            @RequestPart("endDate") LocalDate endDate,
            @RequestPart("description") String description
    ) {
        OrgLectureRequestDto dto = new OrgLectureRequestDto();
        dto.setEmail(email);
        dto.setTitle(title);
        dto.setInstructor(instructor);
        dto.setCategory(category);
        dto.setMethod(method);
        dto.setTarget(target);
        dto.setStartDate(startDate);
        dto.setEndDate(endDate);
        dto.setDescription(description);

        orgLectureService.createLecture(dto);
        return ResponseEntity.ok("강의 등록 완료 (승인 대기)");
    }

    // [ORG002] 상태별 등록한 강의 조회
    @GetMapping("/{email}")
    public ResponseEntity<List<LectureResponseDto>> getMyLectures(
            @PathVariable String email,
            @RequestParam LectureStatus status
    ) {
        List<LectureResponseDto> lectures = orgLectureService.getMyLectures(email, status);
        return ResponseEntity.ok(lectures);
    }

    // [ORG002] 강의 상세 조회
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailDto> getLectureDetail(
            @PathVariable Long lectureId
    ) {
        LectureDetailDto lecture = orgLectureService.getLectureDetail(lectureId);
        return ResponseEntity.ok(lecture);
    }

    // [ORG002] 강의 수정 요청
    @PutMapping("/{lectureId}")
    public ResponseEntity<String> updateLecture(@PathVariable Long lectureId, @RequestBody OrgLectureRequestDto dto) {
        orgLectureService.updateLecture(lectureId, dto);
        return ResponseEntity.ok("강의 수정 완료 (승인 대기)");
    }

    // [ORG002] 강의 삭제
    @DeleteMapping("/delete/{lectureId}")
    public ResponseEntity<String> deleteLecture(
            @PathVariable Long lectureId,
            @RequestParam("email") String email
    ) {
        orgLectureService.deleteLecture(lectureId, email);
        return ResponseEntity.ok("강의가 삭제되었습니다.");
    }
}
