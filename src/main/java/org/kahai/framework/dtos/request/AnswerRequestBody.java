package org.kahai.framework.dtos.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerRequestBody {
    private String text;
    private boolean isCorrect;
}