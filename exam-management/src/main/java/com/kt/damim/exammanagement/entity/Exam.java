package com.kt.damim.exammanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Exam {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    // "하/중/상" 등 난이도 레이블
    @Column(nullable = false)
    private String level;

    private Instant scheduledAt;
}
