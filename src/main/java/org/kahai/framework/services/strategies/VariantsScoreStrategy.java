package org.kahai.framework.services.strategies;

import java.util.List;

import org.kahai.framework.models.questions.Question;
import org.kahai.framework.transients.Participant;

public interface VariantsScoreStrategy {

    int calculate(Participant participant, Question question, List<Boolean> corrects); //
}