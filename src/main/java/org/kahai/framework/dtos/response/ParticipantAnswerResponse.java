package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;

import org.kahai.framework.models.ParticipantAnswer;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ParticipantAnswerResponse {
    private UUID uuid;
    private UUID question;
    private String nickname;
    private String answer;

    public ParticipantAnswerResponse(ParticipantAnswer answer) {
        this.uuid = answer.getUuid();
        this.question = answer.getQuestion().getUuid();
        this.nickname = answer.getNickname();
        this.answer = answer.getAnswer();
    };

    public static List<ParticipantAnswerResponse> mapFromList(List<ParticipantAnswer> answers) {
        return answers.stream()
            .map(ParticipantAnswerResponse::new)
            .toList();
    };
};
