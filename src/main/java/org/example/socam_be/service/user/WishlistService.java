package org.example.socam_be.service.user;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.domain.lecture.Lecture;
import org.example.socam_be.domain.wishlist.Wishlist;
import org.example.socam_be.dto.user.WishlistResponseDto;
import org.example.socam_be.repository.UserRepository;
import org.example.socam_be.repository.LectureRepository;
import org.example.socam_be.repository.WishlistRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

  private final WishlistRepository wishlistRepository;
  private final UserRepository userRepository;
  private final LectureRepository lectureRepository; // ✅ 추가

  @Transactional
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

  @Transactional
  public void removeWishlist(String email, Long lectureId) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    Lecture lecture = lectureRepository.findById(lectureId)
        .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

    wishlistRepository.deleteByUserAndLecture(user, lecture);
  }

  @Transactional(readOnly = true)
  public List<WishlistResponseDto> getWishlist(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));
    List<Wishlist> wishlists = wishlistRepository.findByUser(user);
    
    // DTO로 변환 (트랜잭션 내에서 Lecture 정보를 가져옴)
    return wishlists.stream()
        .map(wishlist -> {
          Lecture lecture = wishlist.getLecture();
          return WishlistResponseDto.builder()
              .id(wishlist.getId())
              .lecture(WishlistResponseDto.LectureInfo.builder()
                  .id(lecture.getId())
                  .title(lecture.getTitle())
                  .instructor(lecture.getInstructor())
                  .organization(lecture.getOrganization())
                  .imageUrl(lecture.getImageUrl())
                  .startDate(lecture.getStartDate())
                  .endDate(lecture.getEndDate())
                  .build())
              .build();
        })
        .collect(Collectors.toList());
  }
}
