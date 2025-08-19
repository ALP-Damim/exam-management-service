package com.kt.damim.exammanagement.service.impl;

import com.kt.damim.exammanagement.dto.QuestionDto;
import com.kt.damim.exammanagement.entity.Question;
import com.kt.damim.exammanagement.repository.QuestionRepository;
import com.kt.damim.exammanagement.service.ExamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExamServiceImpl implements ExamService {
    
    private final QuestionRepository questionRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public QuestionDto getQuestion(Long examId, int idx) {
        Question question = questionRepository.findByExamIdAndIdx(examId, idx)
            .orElseThrow(() -> new IllegalArgumentException("시험 문제를 찾을 수 없습니다: examId=" + examId + ", idx=" + idx));
        
        List<String> choices = null;
        if ("mcq".equals(question.getType())) {
            try {
                choices = objectMapper.readValue(question.getChoicesJson(), new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                log.error("선택지 JSON 파싱 오류", e);
                choices = List.of();
            }
        }
        
        return QuestionDto.of(question, choices);
    }
}

