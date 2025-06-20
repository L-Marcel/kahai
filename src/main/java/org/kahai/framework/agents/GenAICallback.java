package org.kahai.framework.agents;

import java.util.Optional;
import java.util.function.Consumer;

public @FunctionalInterface
interface GenAICallback extends Consumer<Optional<String>> {};
