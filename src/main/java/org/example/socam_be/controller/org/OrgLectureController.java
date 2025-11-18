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
     * JWT에서 자동으로 email을 가져와 사용함
     */
    @PostMapping("/add")
    public ResponseEntity<String> addLecture(
            @RequestAttribute("email") String email,
            @RequestBody OrgLectureRequestDto dto
    ) {
        dto.setEmail(email); // 본인 email 적용
        orgLectureService.createLecture(dto);

        return ResponseEntity.ok("강의 등록 완료 (승인 대기)");
    }

    /**
     * 운영기관 본인이 등록한 강의 목록 조회
     * PathVariable email 제거 → JWT email 사용
     */
    @GetMapping("/list")
    public ResponseEntity<List<LectureResponseDto>> getMyLectures(
            @RequestAttribute("email") String email,
            @RequestParam LectureStatus status
    ) {
        List<LectureResponseDto> lectures = orgLectureService.getMyLectures(email, status);
        return ResponseEntity.ok(lectures);
    }

    /**
     * 강의 상세 조회
     * (단, 본인 강의인지 검증은 Service에서 수행)
     */
    @GetMapping("/{lectureId}")
    public ResponseEntity<LectureDetailDto> getLectureDetail(
            @PathVariable Long lectureId,
            @RequestAttribute("email") String email
    ) {
        LectureDetailDto lecture = orgLectureService.getLectureDetailForOrg(lectureId, email);
        return ResponseEntity.ok(lecture);
    }

    /**
     * 강의 수정 요청
     * 본인 강의 아닌 경우 수정 불가
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
     * 본인 강의인지 반드시 확인
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
