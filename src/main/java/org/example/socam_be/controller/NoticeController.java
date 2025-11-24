package org.example.socam_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.admin.NoticeResponseDto;
import org.example.socam_be.service.admin.AdminNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
@RequiredArgsConstructor
public class NoticeController {

    private final AdminNoticeService adminNoticeService;

    /**
     * 공개된 공지사항 목록 조회 (인증 불필요)
     * status가 VISIBLE인 공지사항만 반환
     */
    @GetMapping
    public ResponseEntity<List<NoticeResponseDto>> getPublicNotices() {
        List<NoticeResponseDto> allNotices = adminNoticeService.getAllNotices();
        // VISIBLE 상태인 공지사항만 필터링
        List<NoticeResponseDto> visibleNotices = allNotices.stream()
                .filter(notice -> "VISIBLE".equals(notice.getStatus()))
                .toList();
        return ResponseEntity.ok(visibleNotices);
    }

    /**
     * 공개된 공지사항 상세 조회 (인증 불필요)
     * VISIBLE 상태인 공지사항만 반환 (조회수 증가 없음)
     */
    @GetMapping("/{noticeId}")
    public ResponseEntity<NoticeResponseDto> getPublicNoticeDetail(@PathVariable Long noticeId) {
        NoticeResponseDto notice = adminNoticeService.getNoticeDetail(noticeId);
        
        // VISIBLE 상태가 아니면 404 반환
        if (!"VISIBLE".equals(notice.getStatus())) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(notice);
    }

    /**
     * 공지사항 조회수 증가 (인증 불필요)
     * 프론트엔드에서 한 번만 호출하도록 별도 엔드포인트로 분리
     */
    @PostMapping("/{noticeId}/view")
    public ResponseEntity<Void> incrementViewCount(@PathVariable Long noticeId) {
        NoticeResponseDto notice = adminNoticeService.getNoticeDetail(noticeId);
        
        // VISIBLE 상태가 아니면 404 반환
        if (!"VISIBLE".equals(notice.getStatus())) {
            return ResponseEntity.notFound().build();
        }
        
        // 조회수 증가
        adminNoticeService.incrementViewCount(noticeId);
        
        return ResponseEntity.ok().build();
    }
}

