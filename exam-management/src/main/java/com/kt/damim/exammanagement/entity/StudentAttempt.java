package com.kt.damim.exammanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class StudentAttempt {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Exam exam;

    @Column(nullable = false, length = 128)
    private String studentId;

    private Instant startedAt = Instant.now();
    private Instant updatedAt = Instant.now();

    // 교사 모니터링용 중간 점수/진행지표
    private Integer realtimeScore;
    private Integer finalScore;

    private Integer currentIdx; // 현재 문항 번호
    private Integer answered;   // 제출 수
}
