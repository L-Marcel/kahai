package org.kahai.framework.services.strategies;

import java.util.List;

import org.kahai.framework.questions.Question;
import org.kahai.framework.transients.Participant;

public interface VariantsScoreStrategy {
    Integer calculate(
        Participant participant, 
        Question question, 
        List<Boolean> corrects
    );
};