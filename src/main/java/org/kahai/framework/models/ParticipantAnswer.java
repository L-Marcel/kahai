package org.kahai.framework.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "participant_answer")
@NoArgsConstructor
public class ParticipantAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column
    private UUID session;

    @ManyToOne
    @JoinColumn(name = "game", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "question", nullable = false)
    private Question question;

    @Column(name = "nickname")
    private String nickname; 

    @OneToMany(mappedBy = "participantAnswer")
    private List<Answer> answers;

    public ParticipantAnswer(UUID session, Game game, Question question, String nickname, List<Answer> answers) {
        this.session = session;
        this.game = game;
        this.question = question;
        this.nickname = nickname;
        this.answers = answers;
    }

    public static List<Answer> convertStringsToAnswers(
        List<String> stringList
    ) {
        List<Answer> answerList = new ArrayList<>();

        for (String answerText : stringList) {
            Answer answer = new Answer();
            answer.setAnswer(answerText);
            answerList.add(answer);
        }
        return answerList;
    }
}