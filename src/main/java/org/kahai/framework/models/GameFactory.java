package org.kahai.framework.models;

import org.kahai.framework.dtos.request.CreateGameRequestBody;

public interface GameFactory {

    Game createGame(CreateGameRequestBody body, User user);
}
