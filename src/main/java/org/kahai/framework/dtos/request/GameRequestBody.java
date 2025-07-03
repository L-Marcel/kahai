package org.kahai.framework.dtos.request;
import java.util.List;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GameRequestBody {
    private String title;
    private List<QuestionRequestBody> questions;
};
