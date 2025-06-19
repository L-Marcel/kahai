package org.kahai.framework.dtos.request;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.transients.QuestionVariant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendQuestionVariantsRequestBody {
    private String code;
    private UUID original;
    private List<QuestionVariant> variants;
};