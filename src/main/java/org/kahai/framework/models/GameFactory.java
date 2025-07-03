package org.kahai.framework.models;

import org.kahai.framework.dtos.request.GameRequestBody;

public interface GameFactory {
    Game createGame(GameRequestBody body, User user);
};

// TODO - Isso aqui não está sendo usado em canto algum...