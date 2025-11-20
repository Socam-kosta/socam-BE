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

import java.util.List;

@RestController
@RequestMapping("/api/org/lecture")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ORG')")
public class OrgLectureController {

    private final OrgLectureService orgLectureService;

    /**
     * 강의 등록
     */
    @PostMapping("/add")
    public ResponseEntity<String> addLecture(
            @RequestAttribute("email") String email,
            @RequestBody OrgLectureRequestDto dto
    ) {
        dto.setEmail(email);
        orgLectureService.createLecture(dto);

        return ResponseEntity.ok("강의 등록 완료 (승인 대기)");
    }

    /**
     * 운영기관 본인이 등록한 강의 목록 조회
     * ex) /api/org/lecture/list?status=PENDING
     */
    @GetMapping("/list")
    public ResponseEntity<List<LectureResponseDto>> getMyLectures(
            @RequestAttribute("email") String email,
            @RequestParam LectureStatus status
    ) {
        return ResponseEntity.ok(
                orgLectureService.getMyLectures(email, status)
        );
    }

    /**
     * 운영기관 본인의 강의 상세 조회
     */
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailDto> getLectureDetail(
            @PathVariable Long lectureId,
            @RequestAttribute("email") String email
    ) {
        return ResponseEntity.ok(
                orgLectureService.getLectureDetailForOrg(lectureId, email)
        );
    }

    /**
     * 강의 수정 (승인 완료 → 수정 시 다시 PENDING)
     */
    @PutMapping("/{lectureId}")
    public ResponseEntity<String> updateLecture(
            @PathVariable Long lectureId,
            @RequestAttribute("email") String email,
            @RequestBody OrgLectureRequestDto dto
    ) {
        dto.setEmail(email);
        orgLectureService.updateLecture(lectureId, dto);

        return ResponseEntity.ok("강의 수정 완료 (승인 대기)");
    }

    /**
     * 강의 삭제
     * 운영기관 본인만 삭제 가능
     */
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<String> deleteLecture(
            @PathVariable Long lectureId,
            @RequestAttribute("email") String email
    ) {
        orgLectureService.deleteLecture(lectureId, email);
        return ResponseEntity.ok("강의가 삭제되었습니다.");
    }
}
