package com.kt.damim.exammanagement.service.impl;

import com.kt.damim.exammanagement.dto.StudentProgressDto;
import com.kt.damim.exammanagement.dto.TeacherViewDto;
import com.kt.damim.exammanagement.entity.Answer;
import com.kt.damim.exammanagement.entity.Question;
import com.kt.damim.exammanagement.entity.StudentAttempt;
import com.kt.damim.exammanagement.repository.AnswerRepository;
import com.kt.damim.exammanagement.repository.QuestionRepository;
import com.kt.damim.exammanagement.repository.StudentAttemptRepository;
import com.kt.damim.exammanagement.service.ProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProgressServiceImpl implements ProgressService {
    
    private final StudentAttemptRepository studentAttemptRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionRepository;
    
    @Override
    public List<StudentProgressDto> getProgress(Long examId, Long since) {
        List<StudentAttempt> attempts = studentAttemptRepository.findByExamId(examId);
        
        return attempts.stream()
            .filter(attempt -> since == null || attempt.getUpdatedAt().toEpochMilli() > since)
            .map(StudentProgressDto::of)
            .collect(Collectors.toList());
    }
    
    @Override
    public TeacherViewDto getStudentDetail(Long examId, String studentId) {
        StudentAttempt attempt = studentAttemptRepository.findFirstByExamIdAndStudentIdOrderByStartedAtDesc(examId, studentId)
            .orElseThrow(() -> new IllegalArgumentException("학생 시도 기록을 찾을 수 없습니다: " + studentId));
        
        List<Answer> answers = answerRepository.findByAttemptId(attempt.getId());
        List<TeacherViewDto.MistakeItem> mistakes = answers.stream()
            .filter(answer -> !answer.getCorrect())
            .map(this::convertToMistakeItem)
            .collect(Collectors.toList());
        
        return new TeacherViewDto(
            attempt.getStudentId(),
            attempt.getCurrentIdx(),
            attempt.getAnswered(),
            attempt.getRealtimeScore(),
            mistakes
        );
    }
    
    private TeacherViewDto.MistakeItem convertToMistakeItem(Answer answer) {
        Question question = answer.getQuestion();
        return new TeacherViewDto.MistakeItem(
            question.getIdx(),
            question.getId(),
            question.getCorrectAnswer(),
            answer.getAnswer()
        );
    }
}

