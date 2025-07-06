package org.kahai.framework.dtos.request;
import java.util.List;

import org.kahai.framework.questions.request.QuestionRequest;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest {
    private String title;
    private List<QuestionRequest> questions;
};
