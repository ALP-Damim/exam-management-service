package com.kt.damim.exammanagement.dto;

import com.kt.damim.exammanagement.entity.StudentAttempt;

/**
 * 교사 모니터 테이블 행
 */
public record StudentProgressDto(
    String studentId,
    Integer currentIdx,
    Integer answered,
    Integer totalScore,
    Long updatedAt
) {
    public static StudentProgressDto of(StudentAttempt a) {
        return new StudentProgressDto(
            a.getStudentId(),
            a.getCurrentIdx(),
            a.getAnswered(),
            a.getRealtimeScore(),
            a.getUpdatedAt() == null ? null : a.getUpdatedAt().toEpochMilli()
        );
    }
}
