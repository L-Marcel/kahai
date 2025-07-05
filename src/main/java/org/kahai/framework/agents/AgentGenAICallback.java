package org.kahai.framework.agents;

import java.util.List;
import java.util.function.Consumer;

import org.kahai.framework.questions.variants.QuestionVariant;

public @FunctionalInterface
interface AgentGenAICallback extends Consumer<List<QuestionVariant>> {};