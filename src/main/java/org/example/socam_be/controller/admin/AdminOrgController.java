package org.example.socam_be.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.admin.OrgResponseDto;
import org.example.socam_be.dto.admin.OrgUpdateRequestDto;
import org.example.socam_be.service.admin.AdminOrgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orgs")
@RequiredArgsConstructor
public class AdminOrgController {

    private final AdminOrgService adminOrgService;

    // [ADM002] 상태가 PENDING인 운영기관 목록 조회
    @GetMapping
    public ResponseEntity<List<OrgResponseDto>> getPendingOrgs() {
        List<OrgResponseDto> orgs = adminOrgService.getPendingOrgs();
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