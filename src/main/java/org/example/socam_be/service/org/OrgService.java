package org.example.socam_be.service.org;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.org.Org;
import org.example.socam_be.domain.org.OrgStatus;
import org.example.socam_be.dto.org.OrgLoginRequestDto;
import org.example.socam_be.dto.org.OrgRegisterRequestDto;
import org.example.socam_be.dto.org.OrgUpdateInfoReqDto;
import org.example.socam_be.dto.org.OrgResponseDto;
import org.example.socam_be.exception.CustomAuthException;
import org.example.socam_be.exception.ErrorCode;
import org.example.socam_be.repository.OrgRepository;
import org.example.socam_be.service.FileService;
import org.example.socam_be.service.user.MailService;
import org.example.socam_be.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrgService {

    private final OrgRepository orgRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final FileService fileService;

    /** 운영기관 정보 조회 */
    public Org findByEmail(String email) {
        return orgRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("운영기관을 찾을 수 없습니다."));
    }

    /** 이메일 중복 검사 */
    public boolean isEmailDuplicate(String email) {
        return orgRepository.existsById(email);
    }

    /** 운영기관 회원가입 */
    public OrgResponseDto registerOrg(OrgRegisterRequestDto dto) {

        if (isEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 운영기관 이메일입니다.");
        }

        MultipartFile certFile = dto.getCertificateFile();
        if (certFile == null || certFile.isEmpty()) {
            throw new IllegalArgumentException("재직증명서 파일을 업로드해야 합니다.");
        }

        // 파일 크기 제한 (5MB)
        if (certFile.getSize() > 5 * 1024 * 1024) {
            throw new IllegalArgumentException("파일 크기는 최대 5MB까지 가능합니다.");
        }

        // 확장자 검사
        String originalName = certFile.getOriginalFilename();
        if (originalName == null ||
                !originalName.toLowerCase().matches(".*\\.(pdf|jpg|jpeg|png)$")) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다. (pdf, jpg, jpeg, png)");
        }

        // MIME 타입 검사
        String contentType = certFile.getContentType();
        if (contentType == null ||
                !(contentType.equals("application/pdf") ||
                        contentType.equals("image/jpeg") ||
                        contentType.equals("image/png"))) {
            throw new IllegalArgumentException("허용되지 않는 MIME 형식입니다.");
        }

        // S3 업로드
        String certificateUrl;
        try {
            certificateUrl = fileService.uploadOrgCertificate(certFile);
        } catch (Exception e) {
            throw new RuntimeException("S3 업로드 실패: " + e.getMessage());
        }

        Org org = Org.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .orgName(dto.getOrgName())
                .contact(dto.getContact())
                .certificatePath(certificateUrl)
                .status(OrgStatus.PENDING)
                .build();

        orgRepository.save(org);

        return new OrgResponseDto(org);
    }

    /** 운영기관 로그인 */
    public Map<String, String> login(OrgLoginRequestDto dto) {

        Org org = orgRepository.findById(dto.getEmail())
                .orElseThrow(() -> new CustomAuthException("존재하지 않는 운영기관입니다.", ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), org.getPassword())) {
            throw new CustomAuthException("비밀번호가 올바르지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        if (org.getStatus() == OrgStatus.PENDING) {
            throw new CustomAuthException("관리자 승인 대기 중입니다.", ErrorCode.ORG_NOT_APPROVED);
        }

        if (org.getStatus() == OrgStatus.REJECTED) {
            throw new CustomAuthException("승인이 거절되었습니다.", ErrorCode.ORG_REJECTED);
        }

        String accessToken = JwtUtils.generateAccessToken(
                org.getEmail(),
                "ORG",
                org.getOrgName()
        );

        String refreshToken = JwtUtils.generateRefreshToken(
                org.getEmail(),
                "ORG",
                org.getOrgName()
        );

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "email", org.getEmail(),
                "orgName", org.getOrgName(),
                "status", org.getStatus().name()
        );
    }

    /** 운영기관 정보 수정 */
    @Transactional
    public OrgResponseDto updateOrgInfo(String email, OrgUpdateInfoReqDto dto) {

        Org org = orgRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("운영기관을 찾을 수 없습니다."));

        org.setOrgName(dto.getOrgName());
        org.setContact(dto.getContact());
        org.setCertificatePath(dto.getCertificatePath());  // 🔥 여기도 향후 업로드로 변경 가능

        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            org.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return new OrgResponseDto(org);
    }

    /** 운영기관 탈퇴 */
    @Transactional
    public void deleteOrg(String email) {
        Org org = orgRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("운영기관을 찾을 수 없습니다."));
        orgRepository.delete(org);
    }

    /** 비밀번호 재설정 요청 */
    public void requestPasswordReset(String email) {
        Org org = orgRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 운영기관 이메일입니다."));

        String token = JwtUtils.generatePasswordResetToken(email);

        mailService.sendPasswordResetMail(email, token);
    }

    /** 비밀번호 변경 */
    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {

        if (!JwtUtils.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않거나 만료되었습니다.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String email = JwtUtils.getEmailFromToken(token);

        Org org = orgRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("운영기관을 찾을 수 없습니다."));

        org.setPassword(passwordEncoder.encode(newPassword));
    }
}
