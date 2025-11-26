package org.example.socam_be.domain.org;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "org")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Org {
    @Id
    @Column(nullable = false, length = 30, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String orgName;

    @Column(nullable = false)
    private String contact;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrgStatus status;

    @Column(nullable = false)
    private String certificatePath;

    @PrePersist
    public void prePersist() {
        if (this.status == null) {
            this.status = OrgStatus.PENDING; //상태 기본값
        }
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
    }
}