package org.kahai.framework.services.strategies;

import java.util.List;
import java.util.Optional;

import org.kahai.framework.transients.Participant;
import org.kahai.framework.transients.QuestionVariant;

public interface VariantsDistributionStrategy {
    public Optional<QuestionVariant> selectVariant(
        Participant participant, 
        List<QuestionVariant> variants
    );

    public List<QuestionVariant> selectVariants(
        Participant participant, 
        List<QuestionVariant> variants
    );
};