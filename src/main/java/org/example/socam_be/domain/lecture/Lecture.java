package org.example.socam_be.domain.lecture;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "lecture")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 강의 식별자

    @Column(nullable = false)
    private String email; // 기관 이메일

    @Column(nullable = false, length = 100)
    private String title; // 강의명

    @Column(length = 50)
    private String instructor; // 강사명

    @Column(length = 100)
    private String organization; // 기관명

    @Column(length = 20)
    private String category; // 프론트엔드 / 백엔드 / AI 등

    @Column(length = 20)
    private String method; // 온라인 / 오프라인

    @Column(length = 20)
    private String target; // 재직자 / 취준생 등

    private LocalDate startDate; // 개강일

    private LocalDate endDate; // 종강일

    @Column(length = 255)
    private String description; // 간단 설명

    @Enumerated(EnumType.STRING)
    private LectureStatus status; //상태(PENDING, APPROVED, REJECTED)
}
