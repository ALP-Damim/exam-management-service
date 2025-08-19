package com.kt.damim.exammanagement.repository;

import com.kt.damim.exammanagement.entity.StudentAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface StudentAttemptRepository extends JpaRepository<StudentAttempt, Long> {
    Optional<StudentAttempt> findFirstByExamIdAndStudentIdOrderByStartedAtDesc(Long examId, String studentId);
    List<StudentAttempt> findByExamId(Long examId);
}
