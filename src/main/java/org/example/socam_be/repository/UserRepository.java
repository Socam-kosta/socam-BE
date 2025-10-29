package org.example.socam_be.repository;

import org.example.socam_be.domain.user.ApprovalStatus;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 🎯 역할별 사용자 조회
     */
    List<User> findByRole(Role role);

    /**
     * 🎯 역할과 승인 상태별 사용자 조회
     */
    List<User> findByRoleAndApprovalStatus(Role role, ApprovalStatus approvalStatus);

    /**
     * 🎯 강사 승인 통계를 위한 카운트 메서드들
     */
    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role")
    long countByRole(@Param("role") Role role);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.approvalStatus = :status")
    long countByRoleAndApprovalStatus(@Param("role") Role role, @Param("status") ApprovalStatus status);

    /**
     * 🎯 강사 목록을 생성일 기준으로 정렬하여 조회
     */
    @Query("SELECT u FROM User u WHERE u.role = :role ORDER BY u.createdAt DESC")
    List<User> findByRoleOrderByCreatedAtDesc(@Param("role") Role role);

    @Query("SELECT u FROM User u WHERE u.role = :role AND u.approvalStatus = :status ORDER BY u.createdAt DESC")
    List<User> findByRoleAndApprovalStatusOrderByCreatedAtDesc(@Param("role") Role role, @Param("status") ApprovalStatus status);

    /**
     * 🎯 이메일 기반 사용자 조회 및 중복 체크
     */
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    /**
     * 🎯 사용자 ID로 조회 (findById는 JpaRepository 기본 제공이지만 커스텀 이름이면 이렇게)
     */
    List<User> findUserById(Long id);
}
