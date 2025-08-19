package com.kt.damim.exammanagement.service.impl;

import com.kt.damim.exammanagement.dto.SubmitAnswerRequest;
import com.kt.damim.exammanagement.dto.SubmitAnswerResponse;
import com.kt.damim.exammanagement.entity.Answer;
import com.kt.damim.exammanagement.entity.Exam;
import com.kt.damim.exammanagement.entity.Question;
import com.kt.damim.exammanagement.entity.StudentAttempt;
import com.kt.damim.exammanagement.repository.AnswerRepository;
import com.kt.damim.exammanagement.repository.QuestionRepository;
import com.kt.damim.exammanagement.repository.StudentAttemptRepository;
import com.kt.damim.exammanagement.service.SubmissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class SubmissionServiceImpl implements SubmissionService {
    
    private final QuestionRepository questionRepository;
    private final StudentAttemptRepository studentAttemptRepository;
    private final AnswerRepository answerRepository;
    
    @Override
    public SubmitAnswerResponse submitAnswer(Long examId, SubmitAnswerRequest req) {
        // 문제 조회
        Question question = questionRepository.findById(req.questionId())
            .orElseThrow(() -> new IllegalArgumentException("문제를 찾을 수 없습니다: " + req.questionId()));
        
        // 시험 ID 검증
        if (!question.getExam().getId().equals(examId)) {
            throw new IllegalArgumentException("시험 ID가 일치하지 않습니다");
        }
        
        // 학생 시도 기록 조회 (임시로 studentId를 "anonymous"로 설정)
        StudentAttempt attempt = getOrCreateAttempt(examId, "anonymous");
        
        // 답안 저장
        Answer answer = new Answer();
        answer.setAttempt(attempt);
        answer.setQuestion(question);
        answer.setAnswer(req.answer());
        answer.setCorrect(isCorrectAnswer(question, req.answer()));
        answer.setSubmittedAt(Instant.now());
        answerRepository.save(answer);
        
        // 시도 기록 업데이트
        updateAttemptProgress(attempt, question.getIdx());
        
        // 다음 문제 인덱스 계산
        Integer nextIdx = calculateNextIndex(examId, question.getIdx());
        if (nextIdx == null) {
            return SubmitAnswerResponse.finished();
        }
        return SubmitAnswerResponse.next(nextIdx);
    }
    
    private StudentAttempt getOrCreateAttempt(Long examId, String studentId) {
        return studentAttemptRepository.findFirstByExamIdAndStudentIdOrderByStartedAtDesc(examId, studentId)
            .orElseGet(() -> {
                StudentAttempt newAttempt = new StudentAttempt();
                newAttempt.setExam(new Exam());
                newAttempt.getExam().setId(examId);
                newAttempt.setStudentId(studentId);
                newAttempt.setCurrentIdx(1);
                newAttempt.setAnswered(0);
                newAttempt.setRealtimeScore(0);
                return studentAttemptRepository.save(newAttempt);
            });
    }
    
    private boolean isCorrectAnswer(Question question, String studentAnswer) {
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) {
            return false;
        }
        
        String correctAnswer = question.getCorrectAnswer();
        if (correctAnswer == null) {
            return false;
        }
        
        return correctAnswer.trim().equalsIgnoreCase(studentAnswer.trim());
    }
    
    private void updateAttemptProgress(StudentAttempt attempt, int currentIdx) {
        attempt.setCurrentIdx(currentIdx + 1);
        attempt.setAnswered(attempt.getAnswered() + 1);
        attempt.setUpdatedAt(Instant.now());
        
        // 실시간 점수 계산 (간단한 구현)
        List<Answer> answers = answerRepository.findByAttemptId(attempt.getId());
        long correctCount = answers.stream().mapToLong(a -> a.getCorrect() ? 1 : 0).sum();
        attempt.setRealtimeScore((int) (correctCount * 100 / answers.size()));
        
        studentAttemptRepository.save(attempt);
    }
    
    private Integer calculateNextIndex(Long examId, int currentIdx) {
        List<Question> questions = questionRepository.findByExamIdOrderByIdx(examId);
        if (currentIdx >= questions.size()) {
            return null; // 모든 문제 완료
        }
        return currentIdx + 1;
    }
}
