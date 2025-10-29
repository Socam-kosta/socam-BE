package org.example.socam_be.service.admin;


import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.admin.Admin;
import org.example.socam_be.domain.notices.Notice;
import org.example.socam_be.domain.notices.NoticeStatus;
import org.example.socam_be.dto.admin.NoticeRequestDto;
import org.example.socam_be.dto.admin.NoticeResponseDto;
import org.example.socam_be.repository.AdminRepository;
import org.example.socam_be.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {
    private  final NoticeRepository noticeRepository;
    private final AdminRepository adminRepository;

    @Transactional
    public NoticeResponseDto createNotice(NoticeRequestDto requestDto) {

        // 1️⃣ 관리자 확인
        Admin admin = adminRepository.findById(requestDto.getAdminEmail())
                .orElseThrow(() -> new IllegalArgumentException("해당 관리자를 찾을 수 없습니다."));

        // 2️⃣ 금지어 자동 거절 처리
        if (containsForbiddenWords(requestDto.getTitle()) || containsForbiddenWords(requestDto.getContents())) {
            throw new IllegalArgumentException("공지 내용에 금지어가 포함되어 등록이 거절되었습니다.");
        }

        // 3️⃣ 공지 등록
        Notice notice = Notice.builder()
                .admin(admin)
                .title(requestDto.getTitle())
                .contents(requestDto.getContents())
                .status(NoticeStatus.VISIBLE)
                .build();

        Notice saved = noticeRepository.save(notice);

        // 4️⃣ 응답 변환
        return NoticeResponseDto.builder()
                .noticeId(saved.getNoticeId())
                .title(saved.getTitle())
                .contents(saved.getContents())
                .adminEmail(admin.getAdminEmail())
                .regDate(saved.getRegDate())
                .status(saved.getStatus().name())
                .build();
    }

    // 🔸 금지어 필터링
    private boolean containsForbiddenWords(String text) {
        List<String> forbidden = List.of("광고", "음란", "불법");
        return forbidden.stream().anyMatch(text::contains);
    }

}
