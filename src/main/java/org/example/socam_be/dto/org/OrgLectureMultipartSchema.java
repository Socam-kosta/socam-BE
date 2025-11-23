package org.example.socam_be.dto.org;

import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.web.multipart.MultipartFile;

@Schema(name = "OrgLectureMultipartSchema", description = "강의 등록/수정 multipart 요청 형식")
public class OrgLectureMultipartSchema {

    @Schema(description = "강의 정보(JSON 문자열)", type = "string", example = "{ \"title\": \"Spring 강의\", \"category\": \"Backend\" }")
    public String data;

    @Schema(description = "강의 이미지 파일(선택)", type = "string", format = "binary")
    public MultipartFile imageFile;
}