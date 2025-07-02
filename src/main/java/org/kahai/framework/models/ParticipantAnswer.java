package org.kahai.framework.models;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "participant_answer")
public class ParticipantAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "game", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "question", nullable = false)
    private Question question;

    @Column(name = "nickname")
    private String nickname; 

    @Column(columnDefinition = "TEXT")
    private String answer;

    // @Column
    // private LocalDateTime submissionTime;
}