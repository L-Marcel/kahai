package org.kahai.framework.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.questions.ConcreteQuestion;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "games")
public class Game implements GamePrototype{
    @Id
    @GeneratedValue
    private UUID uuid;

    @Column
    private String title;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false)
    @JsonIgnore
    private User owner;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference 
    private List<ConcreteQuestion> questions = new ArrayList<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParticipantAnswer> answers;

    @Override
    public void clone(User newOwner) {
        new Game(this.uuid, this.title, newOwner, this.questions, this.answers);
    }
};
