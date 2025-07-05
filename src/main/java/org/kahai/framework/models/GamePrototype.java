package org.kahai.framework.models;

public interface GamePrototype {
    public Game clone(User newOwner);
};
