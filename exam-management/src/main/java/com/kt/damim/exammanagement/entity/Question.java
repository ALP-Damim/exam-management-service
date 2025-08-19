package com.kt.damim.exammanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Question {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Exam exam;

    // 1-based index (한 페이지 = 한 문항 진행용)
    @Column(nullable = false)
    private int idx;

    // "mcq" | "text"
    @Column(nullable = false, length = 16)
    private String type;

    @Column(nullable = false, length = 1000)
    private String prompt;

    // MCQ일 때 선택지 JSON 문자열 (예: ["A","B","C","D"])
    @Column(length = 2000)
    private String choicesJson;

    // 정답 (MCQ: 보기 텍스트/키, TEXT: 키워드 등)
    @Column(length = 500)
    private String correctAnswer;
}
