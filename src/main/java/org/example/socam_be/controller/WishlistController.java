package org.example.socam_be.controller;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.domain.wishlist.Wishlist;
import org.example.socam_be.service.WishlistService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

  private final WishlistService wishlistService;

  @PostMapping("/add")
  public String add(@RequestParam String email, @RequestParam Long lectureId) {
    wishlistService.addWishlist(email, lectureId);
    return "찜 추가 완료";
  }

  @DeleteMapping("/remove")
  public String remove(@RequestParam String email, @RequestParam Long lectureId) {
    wishlistService.removeWishlist(email, lectureId);
    return "찜 삭제 완료";
  }

  @GetMapping("/list")
  public List<Wishlist> list(@RequestParam String email) {
    return wishlistService.getWishlist(email);
  }
}
