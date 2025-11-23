package org.example.socam_be.service.lecture;

import lombok.RequiredArgsConstructor;
import org.example.socam_be.domain.lecture.Lecture;
import org.example.socam_be.domain.lecture.LectureStatus;
import org.example.socam_be.dto.lecture.LectureDetailDto;
import org.example.socam_be.dto.lecture.LectureResponseDto;
import org.example.socam_be.repository.LectureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;

    /**
     * -----------------------------------------------
     *  승인된 강의 리스트 조회 (간략 정보)
     * -----------------------------------------------
     */
    public List<LectureResponseDto> getLectures(String target, String method, String category) {

        List<Lecture> lectures =
                lectureRepository.findByTargetAndMethodAndCategoryAndStatus(
                        target, method, category, LectureStatus.APPROVED
                );

        return lectures.stream()
                .map(lecture -> LectureResponseDto.builder()
                        .id(lecture.getId())
                        .title(lecture.getTitle())
                        .instructor(lecture.getInstructor())
                        .organization(lecture.getOrganization())
                        .category(lecture.getCategory())
                        .method(lecture.getMethod())
                        .target(lecture.getTarget())
                        .startDate(lecture.getStartDate())
                        .endDate(lecture.getEndDate())
                        .status(lecture.getStatus())
                        .build())
                .toList();
    }

    /**
     * -----------------------------------------------
     *  강의 상세 조회 (전체 정보)
     * -----------------------------------------------
     */
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
                .status(lecture.getStatus())

                // ⭐ 신규 필드 매핑
                .region(lecture.getRegion())
                .needCard(lecture.getNeedCard())
//                .ncs(lecture.getNcs())
                .tuition(lecture.getTuition())
                .supportAvailable(lecture.getSupportAvailable())
                .applicationProcess(lecture.getApplicationProcess())
                .eligibility(lecture.getEligibility())
                .employmentSupport(lecture.getEmploymentSupport())
                .curriculum(lecture.getCurriculum())

                .build();
    }
}
