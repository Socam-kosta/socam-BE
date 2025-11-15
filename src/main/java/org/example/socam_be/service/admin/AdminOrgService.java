package org.example.socam_be.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.org.Org;
import org.example.socam_be.domain.org.OrgStatus;
import org.example.socam_be.dto.org.OrgResponseDto;
import org.example.socam_be.dto.org.OrgUpdateRequestDto;
import org.example.socam_be.repository.OrgRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrgService {

    private final OrgRepository orgRepository;

    // [ADM002] 상태가 PENDING인 운영기관 목록 조회
    public List<OrgResponseDto> getPendingOrgs() {
        List<Org> orgs = orgRepository.findByStatus(OrgStatus.PENDING);

        return orgs.stream()
            .map(OrgResponseDto::new)   // ← 생성자 방식으로 DTO 변환
            .toList();
    }

    // [ADM003] 운영기관 승인/거부 처리 (PATCH /api/orgs/{email}/status)
    @Transactional
    public OrgResponseDto updateOrgStatus(String email, OrgUpdateRequestDto requestDto) {

        Org org = orgRepository.findById(email)
            .orElseThrow(() -> new IllegalArgumentException("운영기관을 찾을 수 없습니다. email: " + email));

        org.setStatus(OrgStatus.valueOf(requestDto.getStatus().toUpperCase()));
        orgRepository.save(org);

        return new OrgResponseDto(org);   // ← builder 제거, 생성자 방식으로 반환
    }
}
