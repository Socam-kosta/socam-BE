package org.example.socam_be.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.wishlist.Wishlist;
import org.example.socam_be.service.WishlistService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
@Tag(name = "Wishlist API", description = "찜(위시리스트) 기능")
public class WishlistController {

  private final WishlistService wishlistService;

  @Operation(summary = "찜 추가", description = "특정 강의를 찜 목록에 추가합니다.")
  @PostMapping("/add")
  public String add(
      @Parameter(description = "사용자 이메일") @RequestParam String email,
      @Parameter(description = "강의 ID") @RequestParam Long lectureId
  ) {
    wishlistService.addWishlist(email, lectureId);
    return "찜 추가 완료";
  }

  @Operation(summary = "찜 삭제", description = "특정 강의를 찜 목록에서 삭제합니다.")
  @DeleteMapping("/remove")
  public String remove(
      @Parameter(description = "사용자 이메일") @RequestParam String email,
      @Parameter(description = "강의 ID") @RequestParam Long lectureId
  ) {
    wishlistService.removeWishlist(email, lectureId);
    return "찜 삭제 완료";
  }

  @Operation(summary = "찜 목록 조회", description = "사용자가 찜한 강의 목록을 조회합니다.")
  @GetMapping("/list")
  public List<Wishlist> list(
      @Parameter(description = "사용자 이메일") @RequestParam String email
  ) {
    return wishlistService.getWishlist(email);
  }
}
