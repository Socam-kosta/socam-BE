project-root/
├─ src/main/java/com/project/
│  ├─ config/            # DB 설정, MyBatis 설정, ViewResolver 설정
│  ├─ controller/        # 웹 요청 처리 (JSP 연결)
│  ├─ service/           # 비즈니스 로직
│  │  └─ impl/           # Service 구현체
│  ├─ repository/        # DB 접근 (MyBatis Mapper Interface)
│  ├─ domain/            # Entity / 테이블 매핑 객체
│  ├─ dto/               # 요청/응답용 DTO
│  ├─ mapper/            # MyBatis XML Mapper (SQL)
│  ├─ util/              # 파일 업로드, 유틸 함수
│  └─ exception/         # 예외 처리
├─ src/main/resources/
│  ├─ application.properties  # 애플리케이션 설정
│  └─ mapper/                 # Mapper XML 위치
├─ src/main/webapp/
│  ├─ WEB-INF/
│  │  ├─ views/           # JSP 화면
│  │  └─ web.xml          # 웹 애플리케이션 설정
│  ├─ resources/
│  │  └─ uploads/         # 업로드 파일 저장소
│  └─ index.jsp           # 메인 페이지
└─ pom.xml / build.gradle # 빌드 설정 파일
