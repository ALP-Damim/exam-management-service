package com.kt.damim.exammanagement.service;

import com.kt.damim.exammanagement.dto.StudentProgressDto;
import com.kt.damim.exammanagement.dto.TeacherViewDto;
import java.util.List;
import java.util.UUID;

public interface ProgressService {
    List<StudentProgressDto> getProgress(Long examId, Long since);
    TeacherViewDto getStudentDetail(Long examId, Long userId);
}
