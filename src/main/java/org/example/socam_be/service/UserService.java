package org.example.socam_be.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.review.Review;
import org.example.socam_be.domain.user.ApprovalStatus;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.dto.user.*;
import org.example.socam_be.exception.CustomAuthException;
import org.example.socam_be.exception.ErrorCode;
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

    public User findByEmail(String email) {
        return userRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    // ✅ 회원가입
    public UserResDto registerUser(RegisterReqDto dto) {
        if (isEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        Role role = Role.valueOf(dto.getRole().toUpperCase());
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

        return new UserResDto(userRepository.save(user));
    }

    // ✅ 로그인
    public Map<String, String> login(LoginReqDto dto) {
        User user = userRepository.findById(dto.getEmail())
                .orElseThrow(() -> new CustomAuthException("존재하지 않는 사용자입니다.", ErrorCode.USER_NOT_FOUND));

        if (user.getRole() == Role.ORG && !user.isApproved()) {
            throw new CustomAuthException("관리자의 승인을 기다려 주세요.", ErrorCode.ORG_NOT_APPROVED);
        }

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomAuthException("비밀번호가 올바르지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        // ✅ 이메일 기반 토큰 발급
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


    // ✅ 회원정보 수정
    @Transactional
    public UserResDto updateUserInfo(String email, UpdateUserReqDto dto) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        user.setName(dto.getName());
        user.setNickname(dto.getNickname());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        return new UserResDto(userRepository.save(user));
    }

    // ✅ 회원 탈퇴
    @Transactional
    public void deleteUser(String email) {
        User user = userRepository.findById(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    // ✅ 리뷰 조회
    public List<Review> getReviews(String email) {
        return reviewRepository.findByEmail(email);
    }
}
