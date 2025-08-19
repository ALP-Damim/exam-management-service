package com.kt.damim.exammanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.Instant;

@Entity
@Getter @Setter @NoArgsConstructor
public class Answer {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private StudentAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Question question;

    @Column(length = 1000)
    private String answer;

    private Boolean correct;

    private Instant submittedAt = Instant.now();
}
