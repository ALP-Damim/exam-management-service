package com.kt.damim.exammanagement.dto;

import com.kt.damim.exammanagement.entity.Question;
import java.util.List;

/**
 * 한 페이지 = 한 문항 조회 DTO
 */
public record QuestionDto(
    Long id,
    int idx,
    String type,          // "mcq" | "text"
    String prompt,
    List<String> choices  // text형이면 null
) {
    public static QuestionDto of(Question q, List<String> choices) {
        return new QuestionDto(q.getId(), q.getIdx(), q.getType(), q.getPrompt(), choices);
    }
}
