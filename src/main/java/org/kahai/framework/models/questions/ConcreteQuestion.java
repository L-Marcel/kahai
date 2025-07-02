package org.kahai.framework.models.questions;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.questions.Question;
import org.kahai.framework.models.Context;
import org.kahai.framework.models.Game;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;

import jakarta.persistence.Lob;
import jakarta.persistence.Transient;

@Getter
@Setter
@Entity
@Table(name = "questions")
@JsonTypeName("concrete")
public class ConcreteQuestion implements Question {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid; //

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)@JsonBackReference
    private Game game; //

    @Column(nullable = false, length = 600)
    private String question; //

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)  @JsonManagedReference
    private List<Answer> answers = new ArrayList<>(); 

    @Column(nullable = false)
    private int correctValue; 

    @Column(nullable = false)
    private int wrongValue; 
@Column(name = "decorators_json", columnDefinition = "TEXT")
private String decoratorsJson;

@OneToMany(
    mappedBy = "question", 
    cascade = CascadeType.ALL, 
    orphanRemoval = true 
)
@JsonManagedReference 
private List<Context> contexts = new ArrayList<>();

    @Override
    public String getPromptFormat() {
        return this.question;
    }

    @Override
    public List<Boolean> validate(List<String> userAnswers) {
        return new ArrayList<>();
    }

    @Override  @JsonIgnore 
    public ConcreteQuestion getRoot() {
        return this;
    }
}