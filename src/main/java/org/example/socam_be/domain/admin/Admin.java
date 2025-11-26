package org.example.socam_be.domain.admin;

import jakarta.persistence.*;
import lombok.*;
import org.example.socam_be.domain.notices.Notice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Admin {

    @Id
    @Column(name = "admin_email", nullable = false)
    private String adminEmail; //PK

    @Column(nullable = false)
    private String password;

    private String name;
    private String nickname;
    private LocalDateTime createdAt;
    private String approvalStatus;
    private String role;

    @OneToMany(mappedBy = "admin", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notice> notices = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
