package org.example.socam_be.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.org.Org;
import org.example.socam_be.domain.org.OrgStatus;
import org.example.socam_be.dto.admin.OrgResponseDto;
import org.example.socam_be.repository.OrgRepository;
import org.springframework.stereotype.Service;

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
                        .isApproved(o.getIsApproved())
                        .certificatePath(o.getCertificatePath())
                        .build())
                .toList();
    }
}