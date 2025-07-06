package org.kahai.framework.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Examples<T> {
    private final T first;
    private final T second;
    private final T third;
};
