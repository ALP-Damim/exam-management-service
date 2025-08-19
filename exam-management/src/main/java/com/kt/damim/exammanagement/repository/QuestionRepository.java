package com.kt.damim.exammanagement.repository;

import com.kt.damim.exammanagement.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    Optional<Question> findByExamIdAndIdx(Long examId, int idx);
    List<Question> findByExamIdOrderByIdx(Long examId);
}
