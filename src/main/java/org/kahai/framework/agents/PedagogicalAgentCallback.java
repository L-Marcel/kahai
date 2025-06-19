package org.kahai.framework.agents;

import java.util.List;
import java.util.function.Consumer;

import org.kahai.framework.transients.QuestionVariant;

public @FunctionalInterface
interface PedagogicalAgentCallback extends Consumer<List<QuestionVariant>> {};