package org.example.socam_be.controller.admin;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.org.OrgStatus;
import org.example.socam_be.dto.org.OrgResponseDto;
import org.example.socam_be.dto.org.OrgUpdateRequestDto;
import org.example.socam_be.service.admin.AdminOrgService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orgs")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrgController {

    private final AdminOrgService adminOrgService;

    // [ADM002] 전체 운영기관 목록 조회 (모든 상태 포함)
    @GetMapping
    public ResponseEntity<List<OrgResponseDto>> getAllOrgs() {
        List<OrgResponseDto> orgs = adminOrgService.getAllOrgs();
        return ResponseEntity.ok(orgs);
    }

    // 상태별 운영기관 목록 조회
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrgResponseDto>> getOrgsByStatus(
            @PathVariable String status
    ) {
        OrgStatus orgStatus = OrgStatus.valueOf(status.toUpperCase());
        List<OrgResponseDto> orgs = adminOrgService.getOrgsByStatus(orgStatus);
        return ResponseEntity.ok(orgs);
    }

    // [ADM003] 운영기관 승인/거절 처리 (PATCH /api/orgs/{email}/status)
    @PatchMapping("/{email}/status")
    public ResponseEntity<OrgResponseDto> updateOrgStatus(
            @PathVariable String email,
            @RequestBody OrgUpdateRequestDto requestDto
    ) {
        OrgResponseDto response = adminOrgService.updateOrgStatus(email, requestDto);
        return ResponseEntity.ok(response);
    }
}