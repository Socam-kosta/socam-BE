package org.example.socam_be.domain.notices;

import jakarta.persistence.*;
import lombok.*;
import org.example.socam_be.domain.admin.Admin;

import java.time.LocalDateTime;

@Entity
@Table(name = "notice")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long noticeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_email", nullable = false)
    private Admin admin; // FK 관계

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NoticeStatus status;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 2000)
    private String contents;

    private LocalDateTime regDate;
    private LocalDateTime editDate;
    private int viewCount;

    @PrePersist
    public void prePersist() {
        this.regDate = LocalDateTime.now();
        this.status = this.status == null ? NoticeStatus.VISIBLE : this.status;
        this.viewCount = 0;
    }
}
