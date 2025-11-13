package org.example.socam_be.controller.org;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.org.*;
import org.example.socam_be.service.org.OrgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/org")
@Tag(name = "운영기관 API", description = "운영기관 회원가입, 로그인, 정보조회 기능 제공")
public class OrgController {

    private final OrgService orgService;

    /** 운영기관 회원가입 */
    @Operation(
            summary = "운영기관 회원가입",
            description = "운영기관이 이메일/비밀번호/기관명/연락처/증명서 경로를 입력하여 회원가입합니다."
    )
    @PostMapping("/register")
    public ResponseEntity<OrgResponseDto> register(@RequestBody OrgRegisterRequestDto dto) {
        return ResponseEntity.ok(orgService.registerOrg(dto));
    }

    /** 운영기관 로그인 */
    @Operation(
            summary = "운영기관 로그인",
            description = "승인된 운영기관만 로그인이 가능합니다. 승인 대기(PENDING), 거절(REJECTED) 상태는 로그인 불가입니다."
    )
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody OrgLoginRequestDto dto) {
        return ResponseEntity.ok(orgService.login(dto));
    }

    /** 운영기관 내 정보 조회 */
    @Operation(
            summary = "운영기관 내 정보 조회",
            description = "JWT 기반 이메일로 운영기관 자신의 정보를 조회합니다."
    )
    @GetMapping("/me")
    public ResponseEntity<OrgResponseDto> getMyInfo(@RequestAttribute("email") String email) {
        return ResponseEntity.ok(new OrgResponseDto(orgService.findByEmail(email)));
    }

    /** 운영기관 정보 수정 */
    @Operation(
            summary = "운영기관 정보 수정",
            description = "운영기관 이름, 연락처, 비밀번호, 증명서 경로 등을 수정합니다."
    )
    @PutMapping("/{email}")
    public ResponseEntity<OrgResponseDto> updateOrg(
            @PathVariable String email,
            @RequestBody OrgUpdateInfoReqDto dto
    ) {
        return ResponseEntity.ok(orgService.updateOrgInfo(email, dto));
    }

    /** 운영기관 탈퇴 */
    @Operation(
            summary = "운영기관 탈퇴",
            description = "운영기관 계정을 삭제합니다."
    )
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteOrg(@PathVariable String email) {
        orgService.deleteOrg(email);
        return ResponseEntity.noContent().build();
    }

    /** 비밀번호 재설정 요청 */
    @Operation(
            summary = "비밀번호 재설정 요청",
            description = "운영기관 이메일로 비밀번호 재설정 링크를 발송합니다."
    )
    @PostMapping("/password-reset-request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        orgService.requestPasswordReset(email);
        return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 메일이 발송되었습니다."));
    }

    /** 실제 비밀번호 변경 */
    @Operation(
            summary = "비밀번호 재설정",
            description = "토큰을 이용해 새로운 비밀번호로 재설정합니다."
    )
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword");

        orgService.resetPassword(token, newPassword, confirmPassword);

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }
}
