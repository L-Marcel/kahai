package org.kahai.framework.questions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public abstract class BaseQuestion implements Question {
    protected Question wrappee;
};
