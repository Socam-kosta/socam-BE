package org.example.socam_be.controller.admin;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.admin.NoticeRequestDto;
import org.example.socam_be.dto.admin.NoticeResponseDto;
import org.example.socam_be.service.admin.AdminNoticeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminNoticeController {

    private final AdminNoticeService adminNoticeService;

    //✅ [ADM006] 공지사항 등록 API
    @PostMapping
    public ResponseEntity<NoticeResponseDto> createNotice (@RequestBody NoticeRequestDto requestDto){
        NoticeResponseDto response = adminNoticeService.createNotice(requestDto);
        return ResponseEntity.ok(response);
    }
}
