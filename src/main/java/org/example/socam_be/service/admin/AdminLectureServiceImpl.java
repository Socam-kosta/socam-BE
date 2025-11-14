package org.example.socam_be.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.lecture.*;
import org.example.socam_be.domain.org.Org;
import org.example.socam_be.dto.admin.*;
import org.example.socam_be.exception.CustomException;
import org.example.socam_be.exception.ErrorCode;
import org.example.socam_be.repository.LectureRepository;
import org.example.socam_be.repository.OrgRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminLectureServiceImpl implements AdminLectureService {

    private final LectureRepository lectureRepository;
    private final OrgRepository orgRepository;

    @Override
    public List<LectureAdminListResponseDto> getPendingLectures() {
        List<Lecture> lectures = lectureRepository.findByStatus(LectureStatus.PENDING);

        return lectures.stream().map(lec -> {
            Org org = orgRepository.findById(lec.getEmail())
                    .orElse(null);

            return LectureAdminListResponseDto.builder()
                    .lectureId(lec.getId())
                    .title(lec.getTitle())
                    .email(lec.getEmail())
                    .orgName(org != null ? org.getOrgName() : null)
                    .startDate(lec.getStartDate())
                    .endDate(lec.getEndDate())
                    .status(lec.getStatus().name())
                    .build();
        }).collect(Collectors.toList());
    }

    @Override
    public LectureAdminDetailResponseDto getLectureDetail(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        Org org = orgRepository.findById(lecture.getEmail())
                .orElse(null);

        return LectureAdminDetailResponseDto.builder()
                .lectureId(lecture.getId())
                .title(lecture.getTitle())
                .instructor(lecture.getInstructor())
                .organization(lecture.getOrganization())
                .email(lecture.getEmail())
                .orgName(org != null ? org.getOrgName() : null)
                .category(lecture.getCategory())
                .method(lecture.getMethod())
                .target(lecture.getTarget())
                .startDate(lecture.getStartDate())
                .endDate(lecture.getEndDate())
                .description(lecture.getDescription())
                .status(lecture.getStatus().name())
                .build();
    }

    @Override
    public LectureAdminDetailResponseDto updateLectureStatus(Long lectureId, LectureStatusUpdateRequestDto dto) {

        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        if (lecture.getStatus() != LectureStatus.PENDING) {
            throw new CustomException(ErrorCode.INVALID_LECTURE_STATUS);
        }

        LectureStatus newStatus = LectureStatus.valueOf(dto.getStatus());

        lecture.setStatus(newStatus);
        lectureRepository.save(lecture);

        return getLectureDetail(lectureId);
    }

    @Override
    public void deleteLecture(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new CustomException(ErrorCode.LECTURE_NOT_FOUND));

        lectureRepository.delete(lecture);
    }
}
