package org.example.socam_be.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.admin.LectureAdminDetailResponseDto;
import org.example.socam_be.dto.admin.LectureAdminListResponseDto;
import org.example.socam_be.dto.admin.LectureStatusUpdateRequestDto;
import org.example.socam_be.service.admin.AdminLectureService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@Tag(
        name = "관리자 강의 API",
        description = "관리자가 강의를 조회·승인·거절·삭제하는 기능"
)
@RestController
@RequestMapping("/api/admin/lectures")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ADMIN')")

public class AdminLectureController {

    private final AdminLectureService adminLectureService;

    // 1) 승인 대기중(PENDING) 강의 목록 조회
    @Operation(
            summary = "미승인 강의 목록 조회",
            description = "상태가 PENDING인 강의를 관리자 권한으로 전체 조회합니다."
    )
    @GetMapping("/pending")
    public List<LectureAdminListResponseDto> getPendingLectures() {
        return adminLectureService.getPendingLectures();
    }

    // 2) 강의 상세 조회
    @Operation(
            summary = "강의 상세 조회",
            description = "lectureId 값을 받아 해당 강의의 상세 정보를 조회합니다."
    )
    @GetMapping("/{lectureId}")
    public LectureAdminDetailResponseDto getLectureDetail(@PathVariable Long lectureId) {
        return adminLectureService.getLectureDetail(lectureId);
    }

    // 3) 강의 승인/거절 처리
    @Operation(
            summary = "강의 승인·거절 처리",
            description = "강의 상태를 APPROVED 또는 REJECTED로 변경합니다."
    )
    @PutMapping("/{lectureId}/status")
    public LectureAdminDetailResponseDto updateLectureStatus(
            @PathVariable Long lectureId,
            @RequestBody LectureStatusUpdateRequestDto dto
    ) {
        return adminLectureService.updateLectureStatus(lectureId, dto);
    }

    // 4) 강의 삭제
    @Operation(
            summary = "강의 삭제",
            description = "특정 lectureId에 해당하는 강의를 삭제합니다."
    )
    @DeleteMapping("/{lectureId}")
    public void deleteLecture(@PathVariable Long lectureId) {
        adminLectureService.deleteLecture(lectureId);
    }
}
