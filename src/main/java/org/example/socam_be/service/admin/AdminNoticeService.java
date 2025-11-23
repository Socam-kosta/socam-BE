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
                .viewCount(saved.getViewCount())
                .build();
    }

    // 전체 공지사항 목록 조회
    public List<NoticeResponseDto> getAllNotices() {
        List<Notice> notices = noticeRepository.findAll();

        return notices.stream()
            .map(notice -> NoticeResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .adminEmail(notice.getAdmin().getAdminEmail())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .regDate(notice.getRegDate())
                .status(notice.getStatus().name())
                .viewCount(notice.getViewCount())
                .build())
            .toList();
    }

    // 공지사항 상세 조회
    public NoticeResponseDto getNoticeDetail(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + noticeId));

        return NoticeResponseDto.builder()
                .noticeId(notice.getNoticeId())
                .adminEmail(notice.getAdmin().getAdminEmail())
                .title(notice.getTitle())
                .contents(notice.getContents())
                .regDate(notice.getRegDate())
                .status(notice.getStatus().name())
                .viewCount(notice.getViewCount())
                .build();
    }

    // 공지사항 수정
    @Transactional
    public NoticeResponseDto updateNotice(Long noticeId, NoticeRequestDto requestDto) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + noticeId));

        // 금지어 체크
        if (containsForbiddenWords(requestDto.getTitle()) || containsForbiddenWords(requestDto.getContents())) {
            throw new IllegalArgumentException("공지 내용에 금지어가 포함되어 수정이 거절되었습니다.");
        }

        // 공지 수정
        notice.setTitle(requestDto.getTitle());
        notice.setContents(requestDto.getContents());
        notice.setEditDate(java.time.LocalDateTime.now());

        Notice saved = noticeRepository.save(notice);

        return NoticeResponseDto.builder()
                .noticeId(saved.getNoticeId())
                .adminEmail(saved.getAdmin().getAdminEmail())
                .title(saved.getTitle())
                .contents(saved.getContents())
                .regDate(saved.getRegDate())
                .status(saved.getStatus().name())
                .viewCount(saved.getViewCount())
                .build();
    }

    // 공지사항 상태 변경 (VISIBLE ↔ REJECTED)
    @Transactional
    public NoticeResponseDto updateNoticeStatus(Long noticeId, NoticeStatus status) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + noticeId));

        notice.setStatus(status);
        notice.setEditDate(java.time.LocalDateTime.now());

        Notice saved = noticeRepository.save(notice);

        return NoticeResponseDto.builder()
                .noticeId(saved.getNoticeId())
                .adminEmail(saved.getAdmin().getAdminEmail())
                .title(saved.getTitle())
                .contents(saved.getContents())
                .regDate(saved.getRegDate())
                .status(saved.getStatus().name())
                .viewCount(saved.getViewCount())
                .build();
    }

    // 공지사항 삭제
    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항을 찾을 수 없습니다. ID: " + noticeId));

        noticeRepository.delete(notice);
    }

    // 🔸 금지어 필터링
    private boolean containsForbiddenWords(String text) {
        List<String> forbidden = List.of("광고", "음란", "불법");
        return forbidden.stream().anyMatch(text::contains);
    }

}
