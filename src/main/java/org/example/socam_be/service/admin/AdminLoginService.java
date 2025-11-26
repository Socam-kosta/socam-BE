package org.example.socam_be.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.admin.Admin;
import org.example.socam_be.dto.admin.AdminLoginReqDto;
import org.example.socam_be.exception.CustomAuthException;
import org.example.socam_be.exception.ErrorCode;
import org.example.socam_be.repository.AdminRepository;
import org.example.socam_be.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminLoginService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    public Map<String, String> login(AdminLoginReqDto dto) {
        Admin admin = adminRepository.findById(dto.getAdminEmail())
                .orElseThrow(() -> new CustomAuthException("존재하지 않는 관리자입니다.", ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
            throw new CustomAuthException("비밀번호가 올바르지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = JwtUtils.generateAccessToken(admin.getAdminEmail(), admin.getRole(), admin.getName());
        String refreshToken = JwtUtils.generateRefreshToken(admin.getAdminEmail(), admin.getRole(), admin.getName());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "role", admin.getRole(),
                "name", admin.getName(),
                "email", admin.getAdminEmail(),
                "nickname", admin.getNickname()
        );
    }
}
