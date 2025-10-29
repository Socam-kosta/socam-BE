package org.example.socam_be.domain.lecture;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lecture {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id; // 강의 식별자

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

  @Column(length = 255)
  private String description; // 간단 설명
}
