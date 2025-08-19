package com.kt.damim.exammanagement.dto;

import java.util.List;

/**
 * 특정 학생 상세 화면 (틀린 문항 목록 포함)
 */
public record TeacherViewDto(
    String studentId,
    Integer currentIdx,
    Integer answered,
    Integer totalScore,
    List<MistakeItem> mistakes
) {
    public record MistakeItem(
        Integer questionIdx,
        Long questionId,
        String correctAnswer,
        String studentAnswer
    ) {}
}
