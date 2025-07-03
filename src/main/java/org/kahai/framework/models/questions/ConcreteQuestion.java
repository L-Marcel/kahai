package org.kahai.framework.models.questions;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.annotations.QuestionType;
import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Game;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
@QuestionType
public class ConcreteQuestion implements Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game")
    @JsonBackReference
    private Game game;

    @Column(nullable = false, length = 600)
    private String question;

    @JsonManagedReference
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Context> contexts;

    @Column(name = "correct_value")
    private Integer correctValue;

    @Column(name = "wrong_value")
    private Integer wrongValue;

    @Override
    public String getPromptFormat() {

        return """
                [
                    {
                        "difficulty": "EASY",
                        "question": "...",
                        "options": [
                            "...",
                            "...",
                            "..."
                        ]
                    },
                    {
                        "difficulty": "NORMAL",
                        "question": "...",
                        "options": [
                            "...",
                            "...",
                            "...",
                            "..."
                        ]
                    },
                    {
                        "difficulty": "HARD",
                        "question": "...",
                        "options": [
                            "...",
                            "...",
                            "...",
                            "...",
                            "...",
                            "..."
                        ]
                    }
                ]
                """;
    }

    @Override
    public List<Boolean> validate(List<String> userAnswers) {
        Set<String> correctAnswersSet = this.answers.stream()
                .map(Answer::getAnswer)
                .collect(Collectors.toSet());
        return userAnswers.stream()
                .map(correctAnswersSet::contains)
                .collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public ConcreteQuestion getRoot() {
        return this;
    };
};