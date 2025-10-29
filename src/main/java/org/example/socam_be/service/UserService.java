package org.example.socam_be.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.review.Review;
import org.example.socam_be.domain.user.ApprovalStatus;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;
import org.example.socam_be.dto.user.LoginReqDto;
import org.example.socam_be.dto.user.RegisterReqDto;
import org.example.socam_be.dto.user.UpdateUserReqDto;
import org.example.socam_be.exception.CustomAuthException;
import org.example.socam_be.exception.ErrorCode;
import org.example.socam_be.repository.ReviewRepository;
import org.example.socam_be.repository.UserRepository;
import org.example.socam_be.util.JwtUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReviewRepository reviewRepository;

    // 이메일로 사용자 찾기
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // 사용자 저장 (신규 또는 수정)
    public User save(User user) {
        return userRepository.save(user);
    }

    // 이메일 중복 체크
    public boolean isEmailDuplicate(String email) {
        return userRepository.existsByEmail(email);
    }

    // ✅ 회원가입
    public RegisterReqDto registerUser(RegisterReqDto dto) {
        // 이메일 중복 검사
        if (isEmailDuplicate(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        // Role 변환
        Role role = Role.valueOf(dto.getRole().toUpperCase());

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        // 사용자 객체 생성
        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        user.setPassword(encodedPassword);
        user.setRole(role);
        user.setApprovalStatus(role == Role.ORG ? ApprovalStatus.PENDING : ApprovalStatus.APPROVED);
        user.setApproved(role != Role.ORG);
        user.setLocked(role == Role.ORG); // 운영기관은 승인 전까지 잠금 상태

        // 기관 정보 (선택)
        user.setOrgName(dto.getOrgName());
        user.setContact(dto.getContact());
        user.setCertificatePath(dto.getCertificatePath());

        User savedUser = userRepository.save(user);
        return new RegisterReqDto();
    }

    // ✅ OAuth2 기반 회원가입 (또는 자동 로그인)
    public User registerUserFromOAuth(String email, String name, String roleStr) {
        Role role;
        try {
            role = Role.valueOf(roleStr.toUpperCase());
        } catch (Exception e) {
            role = Role.USER;
        }

        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // 운영기관인데 승인 안 됨
            if (user.getRole() == Role.ORG && !user.isApproved()) {
                throw new CustomAuthException("관리자의 승인을 기다려 주세요.", ErrorCode.ORG_NOT_APPROVED);
            }
            return user;
        }

        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setNickname(name); // 기본 닉네임은 이름으로
        user.setRole(role);
        user.setApprovalStatus(role == Role.ORG ? ApprovalStatus.PENDING : ApprovalStatus.APPROVED);
        user.setApproved(role != Role.ORG);
        user.setLocked(role == Role.ORG);

        return userRepository.save(user);
    }

    // ✅ 로그인
    public Map<String, String> login(LoginReqDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new CustomAuthException("존재하지 않는 사용자입니다.", ErrorCode.USER_NOT_FOUND));

        // 운영기관 승인 확인
        if (user.getRole() == Role.ORG && !user.isApproved()) {
            throw new CustomAuthException("관리자의 승인을 기다려 주세요.", ErrorCode.ORG_NOT_APPROVED);
        }

        // 비밀번호 확인
        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new CustomAuthException("비밀번호가 올바르지 않습니다.", ErrorCode.INVALID_PASSWORD);
        }

        // JWT 생성
        String accessToken = JwtUtils.generateAccessToken(
                user.getEmail(), String.valueOf(user.getId()), user.getRole().name(), user.getName()
        );
        String refreshToken = JwtUtils.generateRefreshToken(
                user.getEmail(), String.valueOf(user.getId()), user.getRole().name(), user.getName()
        );

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "role", user.getRole().name(),
                "name", user.getName(),
                "email", user.getEmail(),
                "nickname", user.getNickname()
        );
    }

    // ✅ 회원 정보 수정
    @Transactional
    public RegisterReqDto updateUserInfo(Long userId, UpdateUserReqDto dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // 이메일 중복 체크
        if (!user.getEmail().equals(dto.getEmail()) && userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 등록된 이메일입니다.");
        }

        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setNickname(dto.getNickname());
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.save(user);
        return new RegisterReqDto();
    }

    // ✅ 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }

    // ✅ 리뷰 조회
    public List<Review> getReviews(String email) {
        return reviewRepository.findByEmail(email);
    }

}
