package org.example.socam_be.controller.user;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.user.*;
import org.example.socam_be.service.user.UserService;
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

    /** 🧩 회원가입 */
    @PostMapping("/register")
    public ResponseEntity<UserResDto> register(@RequestBody RegisterReqDto dto) {
        UserResDto registeredUser = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    /** 🔐 로그인 */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginReqDto dto) {
        Map<String, String> tokens = userService.login(dto);
        return ResponseEntity.ok(tokens);
    }

    /** ✅ 내 정보 조회 (JWT 기반) */
    @GetMapping("/me")
    public ResponseEntity<UserResDto> getMyInfo() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResDto userInfo = new UserResDto(userService.findByEmail(email));
        return ResponseEntity.ok(userInfo);
    }

    /** ✏️ 회원정보 수정 */
    @PutMapping("/{email}")
    public ResponseEntity<UserResDto> updateUser(@PathVariable String email, @RequestBody UpdateUserReqDto dto) {
        UserResDto updated = userService.updateUserInfo(email, dto);
        return ResponseEntity.ok(updated);
    }

    /** ❌ 회원 탈퇴 */
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    // ✅ 비밀번호 재설정 메일 요청
    @PostMapping("/password-reset-request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userService.requestPasswordReset(email);
        return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 메일이 발송되었습니다."));
    }

    // ✅ 실제 비밀번호 변경 (새 비밀번호 제출)
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword"); // 🔹 추가된 부분

        userService.resetPassword(token, newPassword, confirmPassword); // 🔹 3개 인자 전달

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }

}
