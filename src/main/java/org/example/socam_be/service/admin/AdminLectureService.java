package org.example.socam_be.service.admin;

import org.example.socam_be.domain.lecture.LectureStatus;
import org.example.socam_be.dto.admin.*;
import java.util.List;

public interface AdminLectureService {

    // PENDING 목록 조회
    List<LectureAdminListResponseDto> getPendingLectures();

    // 상태별 목록 조회
    List<LectureAdminListResponseDto> getLecturesByStatus(LectureStatus status);

    // 상세 조회
    LectureAdminDetailResponseDto getLectureDetail(Long lectureId);

    // 승인/거절 처리
    LectureAdminDetailResponseDto updateLectureStatus(Long lectureId, LectureStatusUpdateRequestDto dto);

    // 관리자 삭제
    void deleteLecture(Long lectureId);
}
