package org.kahai.framework.models.questions;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)public interface Question {

    String getPromptFormat();

    List<Boolean> validate(List<String> answers);

    ConcreteQuestion getRoot();
}
