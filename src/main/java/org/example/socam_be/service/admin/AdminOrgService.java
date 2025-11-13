package org.example.socam_be.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.org.Org;
import org.example.socam_be.domain.org.OrgStatus;
import org.example.socam_be.dto.admin.OrgResponseDto;
import org.example.socam_be.dto.admin.OrgUpdateRequestDto;
import org.example.socam_be.repository.OrgRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrgService {
    private final OrgRepository orgRepository;

    // [ADM002] 상태가 PENDING인 운영기관 목록 조회
    public List<OrgResponseDto> getPendingOrgs() {
        List<Org> orgs = orgRepository.findByStatus(OrgStatus.PENDING);

        return orgs.stream()
                .map(o -> OrgResponseDto.builder()
                        .email(o.getEmail())
                        .password(o.getPassword())
                        .orgName(o.getOrgName())
                        .contact(o.getContact())
                        .createdAt(o.getCreatedAt())
                        .status(o.getStatus().name())
                        .certificatePath(o.getCertificatePath())
                        .build())
                .toList();
    }

    // ✅ [ADM003] 운영기관 승인/거부 처리 로직 (PATCH /api/orgs/{email}/status)
    @Transactional
    public OrgResponseDto updateOrgStatus(String email, OrgUpdateRequestDto requestDto) {
        // 운영기관 조회
        Org org = orgRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("운영기관을 찾을 수 없습니다. email: " + email));

        org.setStatus(OrgStatus.valueOf(requestDto.getStatus().toUpperCase()));
        orgRepository.save(org);

        // 5️⃣ 응답 DTO 생성 후 반환
        return OrgResponseDto.builder()
                .email(org.getEmail())
                .password(org.getPassword())
                .orgName(org.getOrgName())
                .contact(org.getContact())
                .createdAt(org.getCreatedAt())
                .status(org.getStatus().name())
                .certificatePath(org.getCertificatePath())
                .build();
    }
}