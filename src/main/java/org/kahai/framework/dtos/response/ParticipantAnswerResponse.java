package org.kahai.framework.dtos.response;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
    private List<AnswerResponse> answers;

    public ParticipantAnswerResponse(ParticipantAnswer answer) {
        this.uuid = answer.getUuid();
        this.question = answer.getQuestion().getUuid();
        this.nickname = answer.getNickname();
        this.answers = answer.getAnswers() != null ?
                       answer.getAnswers().stream()
                           .map(AnswerResponse::new)
                           .collect(Collectors.toList()) :
                       List.of();
    }

    public static List<ParticipantAnswerResponse> mapFromList(List<ParticipantAnswer> answers) {
        return answers.stream()
            .map(ParticipantAnswerResponse::new)
            .toList();
    }    
}
