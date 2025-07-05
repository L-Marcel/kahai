package org.kahai.framework.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.kahai.framework.questions.view.QuestionVariantView;

import com.fasterxml.jackson.annotation.JsonView;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@JsonView(QuestionVariantView.Payload.class)
public @interface QuestionVariantPayload {};