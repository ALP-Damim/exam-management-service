# 시험 관리 시스템 (Exam Management System)

Spring Boot를 기반으로 한 온라인 시험 관리 시스템입니다.

## 주요 기능

- **시험 문제 조회**: 객관식/주관식 문제 제공
- **답안 제출**: 학생 답안 제출 및 자동 채점
- **진행 상황 추적**: 실시간 학생 진행 상황 모니터링
- **교사 대시보드**: 학생별 상세 성적 및 오답 분석

## 기술 스택

- **Backend**: Spring Boot 3.x, Spring Data JPA
- **Database**: H2 (개발), PostgreSQL (배포)
- **Build Tool**: Maven
- **Language**: Java 17+

## API 엔드포인트

### 시험 문제
- `GET /api/exams/{examId}/questions/{idx}` - 특정 문제 조회

### 답안 제출
- `POST /api/exams/{examId}/answers` - 답안 제출

### 진행 상황
- `GET /api/exams/{examId}/progress` - 전체 학생 진행 상황
- `GET /api/exams/{examId}/students/{studentId}` - 특정 학생 상세 정보

## 실행 방법

### 1. 개발 환경 실행
```bash
./mvnw spring-boot:run
```

### 2. 프로덕션 환경 실행
```bash
export DB_HOST=your-db-host
export DB_PORT=5432
export DB_NAME=exam
export DB_USER=your-username
export DB_PASS=your-password

./mvnw spring-boot:run -Dspring.profiles.active=prod
```

## 데이터베이스 스키마

### 주요 엔티티
- **Exam**: 시험 정보 (제목, 난이도, 일정)
- **Question**: 문제 정보 (유형, 지문, 선택지, 정답)
- **StudentAttempt**: 학생 시도 기록
- **Answer**: 학생 답안 및 채점 결과

## 개발 가이드

### 새로운 문제 유형 추가
1. `Question` 엔티티의 `type` 필드에 새 유형 추가
2. `QuestionDto`에서 해당 유형 처리 로직 구현
3. `SubmissionServiceImpl`에서 채점 로직 추가

### 새로운 API 추가
1. Controller에 엔드포인트 정의
2. Service 인터페이스 및 구현체 작성
3. Repository 메서드 추가 (필요시)

## 테스트

```bash
# 전체 테스트 실행
./mvnw test

# 특정 테스트 클래스 실행
./mvnw test -Dtest=ExamControllerTest
```

## 배포

### Docker (권장)
```bash
docker build -t exam-management .
docker run -p 8080:8080 exam-management
```

### JAR 파일 직접 실행
```bash
./mvnw clean package
java -jar target/exam-management-0.0.1-SNAPSHOT.jar
```

## 라이센스

MIT License

