package org.kahai.framework.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.questions.ConcreteQuestion;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue
    private UUID uuid;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)  
    private User owner;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)@JsonManagedReference 
   private List<ConcreteQuestion> questions = new ArrayList<>();

};
