package org.example.socam_be.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //회원 식별용

    // ----------------------------
    // 🧩 기본 회원정보
    // ----------------------------
    @Column(nullable = false, length = 30, unique = true)
    private String email; //아이디용

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 7)
    private String name; //수료증 인증 확인용

    @Column(nullable = false, length = 10, unique = true)
    private String nickname; //활동명

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // USER, ADMIN, ORG

    private boolean locked = false; // 운영기관 회원가입 -> 관리자 승인 전까지 대기

    @Column(nullable = false)
    private LocalDateTime createdAt;

    // ----------------------------
    // 🏢 운영기관 회원가입 시만 사용되는 필드
    // ----------------------------
    @Column(nullable = true, length = 50)
    private String orgName;       // 기관명

//    @Column(nullable = true, length = 10, unique = true)
//    private String businessNumber;         // 사업자등록번호
//
//    @Column(nullable = true, length = 20)
//    private String managerName;            // 담당자명

    @Column(nullable = true, length = 20)
    private String contact;                // 기관 연락처

    private String certificatePath;        // 재직자 증명서 파일경로

    // ----------------------------
    // ✅ 승인 관련 (운영기관 전용)
    // ----------------------------
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    @Column(nullable = false)
    private boolean isApproved = false;

    // ----------------------------
    // 🧠 생성자
    // ----------------------------
    public User(String email, String name, String password, LocalDateTime createdAt) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = Role.USER; // 기본값 설정
        this.createdAt = createdAt;
    }

    public void approveOrganization() {
        this.isApproved = true;
        this.approvalStatus = ApprovalStatus.PENDING;
    }

    public boolean isOrganizationApproved() {
        return this.role == Role.ORG && this.isApproved; //운영기관 여부 확인 메서드
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }
}
