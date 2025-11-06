package com.project.codebasespringjpa.dto.survey.request;

import com.project.codebasespringjpa.dto.question.request.QuestionRequest;
import com.project.codebasespringjpa.dto.question.response.QuestionResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SurveyRequest {
    Long id;

    String name;
    String type;
    List<QuestionRequest> questions;
}
