package org.example.socam_be.controller.org;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.lecture.LectureStatus;
import org.example.socam_be.dto.lecture.LectureDetailDto;
import org.example.socam_be.dto.lecture.LectureResponseDto;
import org.example.socam_be.dto.org.OrgLectureRequestDto;
import org.example.socam_be.dto.org.OrgLectureMultipartSchema;
import org.example.socam_be.service.org.OrgLectureService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/org/lecture")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ORG')")
public class OrgLectureController {

    private final OrgLectureService orgLectureService;

    /**
     * -------------------------
     *       강의 등록
     * -------------------------
     */
    @Operation(
            summary = "강의 등록",
            description = """
                    운영기관이 강의를 등록합니다.<br><br>
                    ✔ data = JSON 문자열<br>
                    ✔ imageFile = 이미지 파일(optional)<br><br>
                    파일을 업로드하지 않으면 DTO의 imageUrl 값이 저장됩니다.
                    """
    )
    @RequestBody(
            description = "multipart 요청 형식",
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = OrgLectureMultipartSchema.class)
            )
    )
    @PostMapping(value = "/add", consumes = {"multipart/form-data"})
    public ResponseEntity<String> addLecture(
            @RequestAttribute("email") String email,
            @RequestPart("data") OrgLectureRequestDto dto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        dto.setEmail(email);
        dto.setImageFile(imageFile);
        orgLectureService.createLecture(dto, imageFile);
        return ResponseEntity.ok("강의 등록 완료 (승인 대기)");
    }

    /**
     * -------------------------
     *   내 강의 목록 조회
     * -------------------------
     */
    @Operation(
            summary = "운영기관 본인의 강의 목록 조회",
            description = """
                    운영기관이 등록한 강의들을 상태별(PENDING/APPROVED/REJECTED)로 조회합니다.
                    """
    )
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
     * -------------------------
     *   강의 상세 조회
     * -------------------------
     */
    @Operation(
            summary = "강의 상세 조회",
            description = "운영기관 본인이 등록한 강의만 조회할 수 있습니다."
    )
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
     * -------------------------
     *      강의 수정
     * -------------------------
     */
    @Operation(
            summary = "강의 수정",
            description = """
                    운영기관이 기존에 등록한 강의를 수정합니다.<br><br>
                    ✔ imageFile 업로드 시 → 기존 이미지 교체<br>
                    ✔ imageFile 없이 imageUrl만 수정하면 → URL만 변경<br>
                    ✔ 둘 다 없으면 → 기존 이미지 유지
                    """
    )
    @RequestBody(
            description = "multipart 요청 형식",
            required = true,
            content = @Content(
                    mediaType = MediaType.MULTIPART_FORM_DATA_VALUE,
                    schema = @Schema(implementation = OrgLectureMultipartSchema.class)
            )
    )
    @PutMapping(value = "/{lectureId}", consumes = {"multipart/form-data"})
    public ResponseEntity<String> updateLecture(
            @PathVariable Long lectureId,
            @RequestAttribute("email") String email,
            @RequestPart("data") OrgLectureRequestDto dto,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        dto.setEmail(email);
        dto.setImageFile(imageFile);
        orgLectureService.updateLecture(lectureId, dto);
        return ResponseEntity.ok("강의 수정 완료 (승인 대기)");
    }

    /**
     * -------------------------
     *      강의 삭제
     * -------------------------
     */
    @Operation(
            summary = "강의 삭제",
            description = "운영기관 본인이 등록한 강의만 삭제할 수 있습니다."
    )
    @DeleteMapping("/{lectureId}")
    public ResponseEntity<String> deleteLecture(
            @PathVariable Long lectureId,
            @RequestAttribute("email") String email
    ) {
        orgLectureService.deleteLecture(lectureId, email);
        return ResponseEntity.ok("강의가 삭제되었습니다.");
    }
}
