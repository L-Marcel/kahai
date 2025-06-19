package org.kahai.framework.dtos.response;

import java.util.UUID;

import org.kahai.framework.models.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private UUID uuid;
    private String name;

    public UserResponse(User user) {
        this.uuid = user.getUuid();
        this.name = user.getName();
    };
};
