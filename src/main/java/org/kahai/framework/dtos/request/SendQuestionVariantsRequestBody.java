package org.kahai.framework.dtos.request;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.transients.QuestionVariant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendQuestionVariantsRequestBody {
    private String code;
    private UUID original;
    private List<QuestionVariant> variants;
};