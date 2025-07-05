package org.kahai.framework.questions.request;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.kahai.framework.models.Answer;
import org.kahai.framework.models.Context;
import org.kahai.framework.questions.ConcreteQuestion;
import org.kahai.framework.questions.Question;
import org.kahai.framework.validation.ValidatorChain;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteQuestionRequest implements QuestionRequest {
    private String question;
    private List<String> answers;
    private List<String> contexts;
    private Integer correctValue = 1;
    private Integer wrongValue = 0;

    @Override
    public Question toQuestion() {
        ConcreteQuestion concrete = new ConcreteQuestion();
        concrete.setQuestion(question);
        
        List<Answer> answers = this.answers.stream().map((answer) -> {
            return new Answer(answer, concrete);
        }).collect(Collectors.toList());
        concrete.setAnswers(answers);

        List<Context> contexts = this.contexts.stream().map((context) -> {
            return new Context(context, concrete);
        }).collect(Collectors.toList());
        concrete.setContexts(contexts);

        concrete.setCorrectValue(this.correctValue);
        concrete.setWrongValue(this.wrongValue);
        return concrete;
    };
    
    @Override
    public void validate(ValidatorChain validator, String prefix) {
        validator.validate(prefix + ".question", this.question)
            .nonempty("A pergunta é obrigatória!")
            .max(300, "A pergunta só pode ter até 300 caracteres!");
        
        validator.validate(prefix + ".answers", this.answers)
            .min(1, "Deve haver pelo menos uma opção de resposta!")
            .max(10, "Deve haver no máximo 10 opções de resposta!");
        
        for(int i = 0; i < this.answers.size(); i++) {
            String answer = this.answers.get(i);
            validator.validate(prefix + ".answers." + i, answer)
                .nonempty("Nenhuma resposta pode ser vazia!");
        };

        validator.validate(prefix + ".contexts", this.contexts)
            .min(1, "Deve haver pelo menos uma palavra-chave de contexto!")
            .max(10, "Deve haver no máximo 10 palavras-chave de contexto!");
        
        for(int i = 0; i < this.contexts.size(); i++) {
            String context = this.contexts.get(i);
            validator.validate(prefix + ".contexts." + i, context)
                .nonempty("Nenhuma palavra-chave de contexto pode ser vazia!");
        };

        validator.validate(prefix + ".correctValue", this.correctValue)
            .min(0, "O incremento mínimo por acerto é 0!");
        
        validator.validate(prefix + ".wrongValue", this.wrongValue)
            .min(0, "O decremento mínimo por erro é 0!");
    };
};
