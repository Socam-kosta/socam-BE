package org.example.socam_be.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.dto.admin.AdminLoginReqDto;
import org.example.socam_be.service.admin.AdminLoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminLoginController {

    private final AdminLoginService adminLoginService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AdminLoginReqDto dto) {
        Map<String, String> tokens = adminLoginService.login(dto);
        return ResponseEntity.ok(tokens);
    }
}
