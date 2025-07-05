package org.kahai.framework.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Tuple<F, S> {
    private final F first;
    private final S second;
};
