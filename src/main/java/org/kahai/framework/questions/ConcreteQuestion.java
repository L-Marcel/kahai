package org.kahai.framework.questions;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Difficulty;
import org.kahai.framework.models.Game;
import org.kahai.framework.questions.response.ConcreteQuestionResponse;
import org.kahai.framework.questions.response.QuestionResponse;
import org.kahai.framework.questions.variants.ConcreteQuestionVariant;
import org.kahai.framework.questions.variants.QuestionVariant;
import org.kahai.framework.utils.Examples;

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
    private List<Answer> answers = new LinkedList<>();

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    private List<Context> contexts = new LinkedList<>();

    @Column(name = "correct_value")
    private Integer correctValue;

    @Column(name = "wrong_value")
    private Integer wrongValue;

    @Override
    public List<Boolean> validate(List<String> candidates) {
        Set<String> answers = this.answers.stream()
            .map(Answer::getAnswer)
            .collect(Collectors.toSet());
        
        return candidates.stream()
            .map(answers::contains)
            .collect(Collectors.toList());
    };

    @Override
    public ConcreteQuestion getRoot() {
        return this;
    };

    @Override
    public QuestionResponse toResponse() {
        return new ConcreteQuestionResponse(
            this.uuid,
            this.question,
            this.contexts
                .stream()
                .map(Context::getName)
                .collect(Collectors.toList()),
            this.answers
                .stream()
                .map(Answer::getAnswer)
                .collect(Collectors.toList())
        );
    };

    @Override
    public Examples<? extends QuestionVariant> getPromptExamples() {
        return new Examples<>(
            new ConcreteQuestionVariant("Quanto é 20 + 15?", Difficulty.EASY, List.of("35", "40", "50")), 
            new ConcreteQuestionVariant("Quanto é a raíz de 4?", Difficulty.NORMAL, List.of("35", "2", "50")),
            new ConcreteQuestionVariant("Quanto é a raíz de 25?", Difficulty.HARD, List.of("2", "25", "5"))
        );
    };
};