package org.example.socam_be.repository;

import org.example.socam_be.domain.user.ApprovalStatus;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    List<User> findByRole(Role role);
    List<User> findByRoleAndApprovalStatus(Role role, ApprovalStatus approvalStatus);

    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u WHERE u.role = :role ORDER BY u.email ASC")
    List<User> findByRoleOrderByEmail(@Param("role") Role role);

    Optional<User> findByEmail(String email);
}
