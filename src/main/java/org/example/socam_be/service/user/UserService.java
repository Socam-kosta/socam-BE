package org.example.socam_be.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.review.Review;
import org.example.socam_be.domain.user.ApprovalStatus;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.dto.user.*;
import org.example.socam_be.exception.CustomAuthException;
import org.example.socam_be.exception.ErrorCode;
import org.example.socam_be.repository.RefreshTokenRepository;
import org.example.socam_be.repository.ReviewRepository;
import org.example.socam_be.repository.UserRepository;
import org.example.socam_be.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;
    private final MailService mailService;
    private final RefreshTokenRepository refreshTokenRepository;

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    // ✅ 이메일 & 닉네임 중복 검사
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isNicknameDuplicate(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    // ✅ 회원가입
    public UserResDto registerUser(RegisterReqDto dto) {
        if (isEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }
        if (isNicknameDuplicate(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        // ✅ role 값이 null이면 기본 USER 적용
        Role role = Role.valueOf(dto.getRole() != null ? dto.getRole().toUpperCase() : "USER");
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setName(dto.getName());
        user.setNickname(dto.getNickname());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setApprovalStatus(role == Role.ORG ? ApprovalStatus.PENDING : ApprovalStatus.APPROVED);
        user.setApproved(role != Role.ORG);
        user.setLocked(role == Role.ORG);
        user.setOrgName(dto.getOrgName());
        user.setContact(dto.getContact());
        user.setCertificatePath(dto.getCertificatePath());

        return new UserResDto(userRepository.saveAndFlush(user));
    }

    // ✅ 로그인
    public Map<String, String> login(LoginReqDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomAuthException("존재하지 않는 사용자입니다.", ErrorCode.USER_NOT_FOUND));

        if (user.getRole() == Role.ORG && !user.isApproved()) {
            throw new CustomAuthException("관리자의 승인을 기다려 주세요.", ErrorCode.ORG_NOT_APPROVED);
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomAuthException("비밀번호가 올바르지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = JwtUtils.generateAccessToken(user.getEmail(), user.getRole().name(), user.getName());
        String refreshToken = JwtUtils.generateRefreshToken(user.getEmail(), user.getRole().name(), user.getName());

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "role", user.getRole().name(),
                "name", user.getName(),
                "email", user.getEmail(),
                "nickname", user.getNickname()
        );
    }

    // ✅ 로그아웃 시 리프레시 토큰 삭제
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }

    public boolean existsByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken) != null;
    }

    // ✅ 회원정보 수정
    @Transactional
    public UserResDto updateUserInfo(String email, UpdateUserReqDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getNickname().equals(dto.getNickname()) && isNicknameDuplicate(dto.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        user.setName(dto.getName());
        user.setNickname(dto.getNickname());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return new UserResDto(user); // @Transactional dirty checking 적용됨
    }

    // ✅ 회원 탈퇴
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    // ✅ 리뷰 조회
    public List<Review> getReviews(String email) {
        return reviewRepository.findByEmail(email);
    }

    // ✅ 비밀번호 재설정 메일 발송
    public void requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 이메일입니다."));

        // 10분 유효한 토큰 생성
        String token = JwtUtils.generatePasswordResetToken(email);

        // 메일 발송 (링크 포함)
        mailService.sendPasswordResetMail(email, token);
    }

    // ✅ 비밀번호 변경
    @Transactional
    public void resetPassword(String token, String newPassword, String confirmPassword) {
        if (!JwtUtils.validateToken(token)) {
            throw new IllegalArgumentException("토큰이 유효하지 않거나 만료되었습니다.");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        String email = JwtUtils.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        user.setPassword(passwordEncoder.encode(newPassword));
    }
}
