package org.example.socam_be.repository;

import org.example.socam_be.domain.org.Org;
import org.example.socam_be.domain.org.OrgStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrgRepository extends JpaRepository<Org, Long> {

    // 상태(status) 기준으로 운영기관 조회 (예: PENDING, APPROVED, REJECTED)
    List<Org> findByStatus(OrgStatus status);
}