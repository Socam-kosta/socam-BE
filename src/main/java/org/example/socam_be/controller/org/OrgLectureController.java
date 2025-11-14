package org.example.socam_be.controller.org;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.org.LectureDetailDto;
import org.example.socam_be.dto.org.LectureRequestDto;
import org.example.socam_be.dto.org.LectureResponseDto;
import org.example.socam_be.service.org.OrgLectureService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orgs/lecture")
@RequiredArgsConstructor
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
        LectureRequestDto dto = new LectureRequestDto();
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

    // [ORG002] 등록한 강의 조회
    @GetMapping("/{email}")
    public ResponseEntity<List<LectureResponseDto>> getMyLectures(
            @PathVariable String email
    ) {
        List<LectureResponseDto> lectures = orgLectureService.getMyLectures(email);
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
    public ResponseEntity<String> updateLecture(@PathVariable Long lectureId, @RequestBody LectureRequestDto dto) {
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
