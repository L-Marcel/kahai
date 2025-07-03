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
@Table(name = "answer")
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid; 

    @ManyToOne
    @JoinColumn(name = "participant_answer_uuid", nullable = false)
    private ParticipantAnswer participantAnswer;

    @Column(columnDefinition = "TEXT")
    private String answer;
}
