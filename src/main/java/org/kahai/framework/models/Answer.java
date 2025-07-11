package org.kahai.framework.models;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.questions.ConcreteQuestion;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "answers")
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question")
    @JsonBackReference
    private ConcreteQuestion question;

    public Answer(String answer) {
        this.answer = answer;
    };

    public Answer(String answer, ConcreteQuestion question) {
        this.answer = answer;
        this.question = question;
    };

    public static List<Answer> fromList(
        List<String> answers
    ) {
        return answers
            .stream()
            .map(Answer::new)
            .collect(Collectors.toList());
    };
};