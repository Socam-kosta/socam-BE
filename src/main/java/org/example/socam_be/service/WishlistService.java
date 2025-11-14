package org.example.socam_be.service;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.domain.lecture.Lecture;
import org.example.socam_be.domain.wishlist.Wishlist;
import org.example.socam_be.repository.UserRepository;
import org.example.socam_be.repository.LectureRepository;
import org.example.socam_be.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final UserRepository userRepository;
  private final LectureRepository lectureRepository; // ✅ 추가

  public void addWishlist(String email, Long lectureId) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

    boolean exists = wishlistRepository.findByUserAndLecture(user, lecture).isPresent();
    if (!exists) {
      wishlistRepository.save(Wishlist.builder()
          .user(user)
          .lecture(lecture)
          .build());
    }
  }

  public void removeWishlist(String email, Long lectureId) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

    wishlistRepository.deleteByUserAndLecture(user, lecture);
  }

  public List<Wishlist> getWishlist(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    return wishlistRepository.findByUser(user);
  }
}
