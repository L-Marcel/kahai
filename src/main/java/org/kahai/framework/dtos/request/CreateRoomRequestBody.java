package org.kahai.framework.dtos.request;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateRoomRequestBody {
    private UUID game;
};
