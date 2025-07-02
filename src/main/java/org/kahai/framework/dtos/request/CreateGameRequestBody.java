package org.kahai.framework.dtos.request;
import java.util.List;

import org.kahai.framework.models.User;

import org.kahai.framework.models.Game;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.kahai.framework.dtos.request.CreateConcreteQuestionRequestBody;
import app.hakai.backend.dto.CreateMultipleChoiceQuestionRequestBody;
import lombok.Setter;
import lombok.Getter;
@Getter
@Setter
public class CreateGameRequestBody {
    private String title;
    private List<CreateQuestionRequestBody> questions;
}
