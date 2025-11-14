package org.example.socam_be.service.org;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.lecture.Lecture;
import org.example.socam_be.domain.lecture.LectureStatus;

import org.example.socam_be.dto.org.LectureDetailDto;
import org.example.socam_be.dto.org.LectureRequestDto;
import org.example.socam_be.dto.org.LectureResponseDto;
import org.example.socam_be.repository.LectureRepository;
import org.example.socam_be.repository.OrgRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrgLectureService {

    private final LectureRepository lectureRepository;
    private final OrgRepository orgRepository;

    // 강의 등록
    public void createLecture(LectureRequestDto dto) {

        // 내용 유효성 검사
        if (dto.getTitle() == null || dto.getTitle().length() < 1 || dto.getTitle().length() > 100) {
            throw new IllegalArgumentException("강의명은 최소 1자 이상, 최대 100자 이하로 입력해야 합니다.");
        }
        if (dto.getInstructor().length() > 50) {
            throw new IllegalArgumentException("강사명은 최대 50자 까지 입력할 수 있습니다.");
        }
        if (dto.getCategory() == null) {
            throw new IllegalArgumentException("강의 분야를 선택해주세요.");
        }
        if (dto.getMethod() == null) {
            throw new IllegalArgumentException("강의 방식을 선택해주세요.");
        }
        if (dto.getTarget() == null) {
            throw new IllegalArgumentException("강의 대상을 선택해주세요.");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("개강일은 종강일 이후일 수 없습니다.");
        }
        if (dto.getDescription().length() > 255) {
            throw new IllegalArgumentException("강의 설명은 최대 255자 까지 입력할 수 있습니다.");
        }

        // Lecture 객체 생성 및 저장
        Lecture lecture = Lecture.builder()
                .email(dto.getEmail())
                .title(dto.getTitle())
                .instructor(dto.getInstructor())
                .organization(orgRepository.findOrgNameByEmail(dto.getEmail()))
                .category(dto.getCategory())
                .method(dto.getMethod())
                .target(dto.getTarget())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .description(dto.getDescription())
                .status(LectureStatus.PENDING)
                .build();

        lectureRepository.save(lecture);
    }

    // 등록한 강의 조회
    public List<LectureResponseDto> getMyLectures(String email) {
        List<Lecture> lectures = lectureRepository.findByEmail(email);

        return lectures.stream()
                .map(lecture -> LectureResponseDto.builder()
                        .id(lecture.getId())
                        .title(lecture.getTitle())
                        .build())
                .toList();
    }

    // 강의 상세 조회
    public LectureDetailDto getLectureDetail(Long lectureId) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        return LectureDetailDto.builder()
                        .title(lecture.getTitle())
                        .instructor(lecture.getInstructor())
                        .organization(lecture.getOrganization())
                        .category(lecture.getCategory())
                        .method(lecture.getMethod())
                        .target(lecture.getTarget())
                        .startDate(lecture.getStartDate())
                        .endDate(lecture.getEndDate())
                        .description(lecture.getDescription())
                        .build();
    }

    // 강의 수정
    @Transactional
    public void updateLecture(Long lectureId, LectureRequestDto dto) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("강의를 찾을 수 없습니다."));

        if (!lecture.getEmail().equals(dto.getEmail())) {
            throw new IllegalArgumentException("본인이 등록한 강의만 수정할 수 있습니다.");
        }

        // 내용 유효성 검사
        if (dto.getTitle() == null || dto.getTitle().length() < 1 || dto.getTitle().length() > 100) {
            throw new IllegalArgumentException("강의명은 최소 1자 이상, 최대 100자 이하로 입력해야 합니다.");
        }
        if (dto.getInstructor().length() > 50) {
            throw new IllegalArgumentException("강사명은 최대 50자 까지 입력할 수 있습니다.");
        }
        if (dto.getCategory() == null) {
            throw new IllegalArgumentException("강의 분야를 선택해주세요.");
        }
        if (dto.getMethod() == null) {
            throw new IllegalArgumentException("강의 방식을 선택해주세요.");
        }
        if (dto.getTarget() == null) {
            throw new IllegalArgumentException("강의 대상을 선택해주세요.");
        }
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new IllegalArgumentException("개강일은 종강일 이후일 수 없습니다.");
        }
        if (dto.getDescription().length() > 255) {
            throw new IllegalArgumentException("강의 설명은 최대 255자 까지 입력할 수 있습니다.");
        }

        lecture.setTitle(dto.getTitle());
        lecture.setInstructor(dto.getInstructor());
        lecture.setCategory(dto.getCategory());
        lecture.setMethod(dto.getMethod());
        lecture.setTarget(dto.getTarget());
        lecture.setStartDate(dto.getStartDate());
        lecture.setEndDate(dto.getEndDate());
        lecture.setDescription(dto.getDescription());
        lecture.setStatus(LectureStatus.PENDING);

    }

    // 강의 삭제
    public void deleteLecture(Long lectureId, String email) {
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new IllegalArgumentException("해당 강의를 찾을 수 없습니다."));

        if (!lecture.getEmail().equals(email)) {
            throw new IllegalArgumentException("본인이 등록한 강의만 삭제할 수 있습니다.");
        }

        lectureRepository.delete(lecture);
    }
}
