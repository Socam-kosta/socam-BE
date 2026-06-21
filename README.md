```
socam-BE/

│

├── 📂 src/main/java/org/example/socam_be/

│   │

│   ├── ⚙️ config/                     # Security(JWT 필터, CORS), S3, Swagger 설정

│   │

│   ├── 🎯 controller/                 # REST API 요청 처리 (JSON 응답)

│   │   ├── admin/                    # 관리자 전용 API (강의 승인, 운영기관 승인 등)

│   │   ├── lecture/                  # 강의 조회 API

│   │   ├── org/                      # 운영기관 API

│   │   └── user/                     # 사용자 API (인증, 리뷰, 찜)

│   │

│   ├── 💼 service/                    # 비즈니스 로직

│   │   ├── admin/                    # 관리자 도메인 서비스

│   │   ├── impl/                     # 외부 API 연동 구현체 (Clova/Local OCR)

│   │   ├── lecture/, org/, user/     # 도메인별 서비스

│   │

│   ├── 🗄️ repository/                 # DB 접근 (Spring Data JPA - JpaRepository 인터페이스)

│   │

│   ├── 📦 domain/                     # JPA Entity (@Entity)

│   │

│   ├── 📋 dto/                        # 요청/응답용 DTO

│   │

│   ├── 🔐 security/                   # JWT 토큰 발급·검증 서비스

│   │

│   ├── 🛠️ util/                       # JWT 유틸리티

│   │

│   └── ⚠️ exception/                  # 전역 예외 처리 (CustomException, ErrorCode)

│

├── 📂 src/main/resources/

│   └── 📄 application.yml            # 애플리케이션 설정 (DB, JWT, S3, Clova OCR 키 등)

│

└── 📄 build.gradle                    # 빌드 설정 (Spring Data JPA, Security, JWT, AWS S3 SDK)

```
