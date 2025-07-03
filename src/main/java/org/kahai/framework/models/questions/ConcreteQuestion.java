package org.kahai.framework.models.questions;

import java.util.List;
import java.util.UUID;

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

// TODO - SUGESTÃO TOP, ao invés de passar o name dentro do @QuestionType
// Que tal passar... nada? Dá para pegar o nome do Class assim oh
// ConcreteQuestion.class.getName()
// Então acho que dá para fazer @QuestionType sem name
// ou, no máximo, @QuestionType(ConcreteQuestion.class)

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "questions")
@QuestionType("concrete")
public class ConcreteQuestion implements Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "game_id")
    @JsonBackReference
    private Game game;

    @Column(name = "question")
    private String question;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
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
        return question;
    };

    @Override
    public List<Boolean> validate(List<String> answers) {
        // TODO - Implementar esse método de verdade
        return List.of(true); 
    };

    @Override
    @JsonIgnore
    public ConcreteQuestion getRoot() {
        return this;
    };
};