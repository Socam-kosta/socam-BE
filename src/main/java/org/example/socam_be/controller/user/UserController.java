package org.example.socam_be.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.user.*;
import org.example.socam_be.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 🧩 회원가입
     */
    @PostMapping("/register")
    public ResponseEntity<UserResDto> register(@RequestBody RegisterReqDto dto) {
        UserResDto registeredUser = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /**
     * 🔐 로그인
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginReqDto dto) {
        Map<String, String> tokens = userService.login(dto);
        return ResponseEntity.ok(tokens);
    }

    // ✅ 내 정보 조회 (토큰 기반)
    @GetMapping("/me")
    public ResponseEntity<UserResDto> getMyInfo() {
        // JWT Filter에서 저장한 email 꺼내기
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResDto userInfo = new UserResDto(userService.findByEmail(email));
        return ResponseEntity.ok(userInfo);
    }

    /**
     * ✏️ 회원 정보 수정
     */
    @PutMapping("/{email}")
    public ResponseEntity<UserResDto> updateUser(
            @PathVariable String email,
            @RequestBody UpdateUserReqDto dto
    ) {
        UserResDto updated = userService.updateUserInfo(email, dto);
        return ResponseEntity.ok(updated);
    }

    /**
     * ❌ 회원 탈퇴
     */
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }
}
