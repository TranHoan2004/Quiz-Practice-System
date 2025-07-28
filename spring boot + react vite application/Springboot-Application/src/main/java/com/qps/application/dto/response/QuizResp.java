package com.qps.application.dto.response;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record QuizResp(String id, Integer duration, Boolean status, Float passRate,
                       LocalDate updatedDate, Integer numberOfQuestions,
                       String description, String title,
                       String subjectName, String type) {
}
