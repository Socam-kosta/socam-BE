package org.example.socam_be.controller.admin;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.notices.NoticeStatus;
import org.example.socam_be.dto.admin.NoticeRequestDto;
import org.example.socam_be.dto.admin.NoticeResponseDto;
import org.example.socam_be.service.admin.AdminNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    // 전체 공지사항 목록 조회
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getAllNotices() {
        List<NoticeResponseDto> notices = adminNoticeService.getAllNotices();
        return ResponseEntity.ok(notices);
    }

    //✅ [ADM006] 공지사항 등록 API
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice (@RequestBody NoticeRequestDto requestDto){
        NoticeResponseDto response = adminNoticeService.createNotice(requestDto);
        return ResponseEntity.ok(response);
    }

    // 공지사항 상세 조회
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getNoticeDetail(@PathVariable Long noticeId) {
        NoticeResponseDto response = adminNoticeService.getNoticeDetail(noticeId);
        return ResponseEntity.ok(response);
    }

    // 공지사항 수정
    @PutMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> updateNotice(
            @PathVariable Long noticeId,
            @RequestBody NoticeRequestDto requestDto
    ) {
        NoticeResponseDto response = adminNoticeService.updateNotice(noticeId, requestDto);
        return ResponseEntity.ok(response);
    }

    // 공지사항 상태 변경 (VISIBLE ↔ REJECTED)
    @PatchMapping("/{noticeId}/status")
    public ResponseEntity<NoticeResponseDto> updateNoticeStatus(
            @PathVariable Long noticeId,
            @RequestParam String status
    ) {
        NoticeStatus noticeStatus = NoticeStatus.valueOf(status.toUpperCase());
        NoticeResponseDto response = adminNoticeService.updateNoticeStatus(noticeId, noticeStatus);
        return ResponseEntity.ok(response);
    }

    // 공지사항 삭제
    @DeleteMapping("/{noticeId}")
    public ResponseEntity<Void> deleteNotice(@PathVariable Long noticeId) {
        adminNoticeService.deleteNotice(noticeId);
        return ResponseEntity.noContent().build();
    }
}
