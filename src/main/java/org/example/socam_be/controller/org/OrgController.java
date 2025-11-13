package org.example.socam_be.controller.org;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.org.Org;
import org.example.socam_be.dto.org.*;
import org.example.socam_be.service.org.OrgService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/org")
public class OrgController {

    private final OrgService orgService;

    @PostMapping("/register")
    public ResponseEntity<OrgResponseDto> register(@RequestBody OrgRegisterRequestDto dto) {
        return ResponseEntity.ok(orgService.registerOrg(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody OrgLoginRequestDto dto) {
        return ResponseEntity.ok(orgService.login(dto));
    }

    @GetMapping("/me")
    public ResponseEntity<Org> getMyInfo(@RequestAttribute("email") String email) {
        return ResponseEntity.ok(orgService.findByEmail(email));
    }

    @PutMapping("/{email}")
    public ResponseEntity<OrgResponseDto> updateOrg(
            @PathVariable String email,
            @RequestBody OrgUpdateInfoReqDto dto
    ) {
        return ResponseEntity.ok(orgService.updateOrgInfo(email, dto));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteOrg(@PathVariable String email) {
        orgService.deleteOrg(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password-reset-request")
    public ResponseEntity<Map<String, String>> requestPasswordReset(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        orgService.requestPasswordReset(email);
        return ResponseEntity.ok(Map.of("message", "비밀번호 재설정 메일이 발송되었습니다."));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> body) {
        String token = body.get("token");
        String newPassword = body.get("newPassword");
        String confirmPassword = body.get("confirmPassword");

        orgService.resetPassword(token, newPassword, confirmPassword);

        return ResponseEntity.ok(Map.of("message", "비밀번호가 성공적으로 변경되었습니다."));
    }
}
