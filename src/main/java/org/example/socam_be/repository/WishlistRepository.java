package org.example.socam_be.repository;

import org.example.socam_be.domain.wishlist.Wishlist;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.domain.lecture.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
  // user_email FK -> user.email 참조
  List<Wishlist> findByUser(User user); // 사용자의 찜 목록 조회

  Optional<Wishlist> findByUserAndLecture(User user, Lecture lecture); // ✅ 중복 여부 확인용

  void deleteByUserAndLecture(User user, Lecture lecture); // ✅ 특정 강의 찜 해제
}

