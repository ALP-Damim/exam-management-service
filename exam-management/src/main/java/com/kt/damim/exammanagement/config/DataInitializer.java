package com.kt.damim.exammanagement.config;

import com.kt.damim.exammanagement.entity.Exam;
import com.kt.damim.exammanagement.entity.Question;
import com.kt.damim.exammanagement.repository.ExamRepository;
import com.kt.damim.exammanagement.repository.QuestionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;

@Slf4j
@Component
@Profile("!prod")
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {
    
    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public void run(String... args) throws Exception {
        if (examRepository.count() == 0) {
            initializeSampleData();
        }
    }
    
    private void initializeSampleData() throws JsonProcessingException {
        log.info("샘플 시험 데이터를 초기화합니다...");
        
        // 시험 생성
        Exam exam = new Exam();
        exam.setTitle("2024년 중간고사");
        exam.setLevel("중");
        exam.setScheduledAt(Instant.now());
        exam = examRepository.save(exam);
        
        // 객관식 문제
        Question mcq1 = new Question();
        mcq1.setExam(exam);
        mcq1.setIdx(1);
        mcq1.setType("mcq");
        mcq1.setPrompt("다음 중 Java의 특징이 아닌 것은?");
        mcq1.setChoicesJson(objectMapper.writeValueAsString(Arrays.asList("A. 객체지향", "B. 플랫폼 독립적", "C. 컴파일 언어", "D. 인터프리터 언어")));
        mcq1.setCorrectAnswer("D");
        questionRepository.save(mcq1);
        
        Question mcq2 = new Question();
        mcq2.setExam(exam);
        mcq2.setIdx(2);
        mcq2.setType("mcq");
        mcq2.setPrompt("Spring Boot에서 @RestController 어노테이션의 역할은?");
        mcq2.setChoicesJson(objectMapper.writeValueAsString(Arrays.asList("A. 데이터베이스 연결", "B. REST API 엔드포인트 제공", "C. 보안 설정", "D. 로깅 설정")));
        mcq2.setCorrectAnswer("B");
        questionRepository.save(mcq2);
        
        // 주관식 문제
        Question text1 = new Question();
        text1.setExam(exam);
        text1.setIdx(3);
        text1.setType("text");
        text1.setPrompt("JPA에서 엔티티 간의 관계를 표현하는 어노테이션 3가지를 작성하세요.");
        text1.setChoicesJson(null);
        text1.setCorrectAnswer("@OneToOne, @OneToMany, @ManyToOne, @ManyToMany");
        questionRepository.save(text1);
        
        log.info("샘플 시험 데이터 초기화 완료: 시험 ID={}, 문제 수={}", exam.getId(), 3);
    }
}

