package org.example.socam_be.controller.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User API", description = "회원 관련 기능")
public class UserController {

    private final UserService userService;

    // 🧩 회원가입
    @Operation(summary = "회원가입", description = "회원 정보를 입력하여 신규 회원을 등록합니다.")
    @PostMapping("/register")
    public ResponseEntity<UserResDto> register(@RequestBody RegisterReqDto dto) {
        UserResDto registeredUser = userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
    }

    // 🔐 로그인
    @Operation(summary = "로그인", description = "이메일과 비밀번호를 사용하여 로그인을 수행하고 JWT를 반환합니다.")
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginReqDto dto) {
        Map<String, String> tokens = userService.login(dto);
        return ResponseEntity.ok(tokens);
    }

    // ✔ 내 정보 조회
    @Operation(summary = "내 정보 조회", description = "JWT 토큰 기반으로 로그인한 사용자의 정보를 조회합니다.")
    @GetMapping("/me")
    public ResponseEntity<UserResDto> getMyInfo() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResDto userInfo = new UserResDto(userService.findByEmail(email));
        return ResponseEntity.ok(userInfo);
    }

    // ✏ 회원정보 수정
    @Operation(summary = "회원정보 수정", description = "이메일 기준으로 회원 정보를 수정합니다.")
    @PutMapping("/{email}")
    public ResponseEntity<UserResDto> updateUser(
        @Parameter(description = "회원 이메일") @PathVariable String email,
        @RequestBody UpdateUserReqDto dto
    ) {
        UserResDto updated = userService.updateUserInfo(email, dto);
        return ResponseEntity.ok(updated);
    }

    // ❌ 회원 탈퇴
    @Operation(summary = "회원 탈퇴", description = "이메일 기준으로 회원 정보를 삭제합니다.")
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUser(
        @Parameter(description = "회원 이메일") @PathVariable String email
    ) {
        userService.deleteUser(email);
        return ResponseEntity.noContent().build();
    }

    // 비밀번호 재설정 메일 요청
    @Operation(summary = "비밀번호 재설정 요청",
        description = "사용자 이메일로 비밀번호 재설정 링크를 발송합니다.")
    @PostMapping("/password-reset-request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        userService.requestPasswordReset(email);
        return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 메일이 발송되었습니다."));
    }

    // 실제 비밀번호 변경
    @Operation(summary = "비밀번호 재설정",
        description = "발급된 토큰을 사용하여 실제 비밀번호를 변경합니다.")
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword");

        userService.resetPassword(token, newPassword, confirmPassword);

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }
}
