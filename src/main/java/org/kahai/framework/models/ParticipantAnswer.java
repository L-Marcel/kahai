package org.kahai.framework.models;

import java.util.UUID;

import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "participant_answers")
@NoArgsConstructor
public class ParticipantAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private UUID session;

    @ManyToOne
    @JoinColumn(name = "game", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "question", nullable = false)
    private ConcreteQuestion question;

    @Column(nullable = false)
    private String nickname; 

    @ManyToOne
    @JoinColumn(name = "owner", nullable = true)  
    private User owner;

    @Column(nullable = false, length = 200)
    private String answer;

    public ParticipantAnswer(
        UUID session, 
        Game game, 
        User owner,
        Question question, 
        String nickname,
        Answer answer
    ) {
        this.session = session;
        this.game = game;
        this.owner = owner;
        this.question = question.getRoot();
        this.nickname = nickname;
        this.answer = answer.getAnswer();
    };
};