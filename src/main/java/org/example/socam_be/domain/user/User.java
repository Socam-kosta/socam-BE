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
    @Id
    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 7)
    private String name;

    @Column(nullable = false, length = 10, unique = true)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER; // USER, ADMIN, ORG

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "is_approved", nullable = false)
    private Boolean isApproved = true; // 학생 회원가입은 자동 승인

    @Column(name = "locked", nullable = false)
    private Boolean locked = false; // 계정 잠금 여부


    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = Role.USER;
        this.isApproved = true; // 학생 회원가입은 자동 승인
        this.locked = false; // 계정 잠금 여부
    }

    public boolean isAdmin() {
        return this.role == Role.ADMIN;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
