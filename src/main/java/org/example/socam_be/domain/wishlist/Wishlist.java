package org.example.socam_be.domain.wishlist;

import jakarta.persistence.*;
import lombok.*;
import org.example.socam_be.domain.lecture.Lecture;
import org.example.socam_be.domain.user.User;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wishlist {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 🔹 email이 PK라면 user_email로 외래키를 연결해야 함
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
  private User user;

  // ✅ Lecture 엔티티로 참조 변경
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "lecture_id", nullable = false)
  private Lecture lecture;
}
