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
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

private String question;
@OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @JsonManagedReference
    private List<Answer> answers;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
@JsonManagedReference
private List<Context> contexts;

    private int correctValue;
    private int wrongValue;

    @Override
    public String getPromptFormat() {
        return question;
    }

    @Override
    public List<Boolean> validate(List<String> answers) {
        return List.of(true); 
    }

    @Override
    @JsonIgnore
    public ConcreteQuestion getRoot() {
        return this;
    }
}