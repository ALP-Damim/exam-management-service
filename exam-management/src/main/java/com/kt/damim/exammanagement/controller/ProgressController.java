package com.kt.damim.exammanagement.controller;

import com.kt.damim.exammanagement.dto.*;
import com.kt.damim.exammanagement.service.ProgressService;
import com.kt.damim.exammanagement.service.SubmissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ProgressController {
    
    private final SubmissionService submissionService;
    private final ProgressService progressService;
    
    @PostMapping("/{examId}/answers")
    public SubmitAnswerResponse submitAnswer(
        @PathVariable Long examId,
        @Valid @RequestBody SubmitAnswerRequest req
    ) {
        return submissionService.submitAnswer(examId, req);
    }
    
    @GetMapping("/{examId}/progress")
    public List<StudentProgressDto> getProgress(
        @PathVariable Long examId,
        @RequestParam(required = false) Long since
    ) {
        return progressService.getProgress(examId, since);
    }
    
    @GetMapping("/{examId}/students/{studentId}")
    public TeacherViewDto getStudentDetail(
        @PathVariable Long examId,
        @PathVariable Long studentId
    ) {
        return progressService.getStudentDetail(examId, studentId);
    }
}
