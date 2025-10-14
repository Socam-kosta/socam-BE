# socam_be

project-root/
│
├── src/main/java/com/project/
│   ├── config/                     # DB 설정, MyBatis 설정, ViewResolver 설정
│   ├── controller/                 # 웹 요청 처리 (JSP 연결)
│   ├── service/                    # 비즈니스 로직
│   │   ├── impl/                   # Service 구현
│   ├── repository/                 # DB 접근(MyBatis Mapper Interface)
│   ├── domain/                     # Entity / Table과 매핑되는 객체
│   ├── dto/                        # 요청/응답용 DTO
│   ├── mapper/                     # MyBatis XML Mapper (SQL)
│   ├── util/                       # 파일 업로드, 유틸 등
│   └── exception/                  # 예외 처리
│
├── src/main/resources/
│   ├── application.properties
│   └── mapper/                     # Mapper XML 위치
│
├── src/main/webapp/
│   ├── WEB-INF/
│   │   ├── views/                  # JSP 화면
│   │   └── web.xml
│   ├── resources/uploads/          # 업로드 파일 저장
│   └── index.jsp
│
└── pom.xml / build.gradle
