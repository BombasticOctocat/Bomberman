package com.bombasticoctocat.bomberman;

public enum ParticleImage {
    BOMB("bomb"),
    BRICKS("bricks"),
    CHARACTER("character"),
    CONCRETE("concrete"),
    EMPTY("empty"),
    FLAMES("flames"),
    GOOMBA("goomba"),
    KILLED("killed"),
    DOOR_OPEN("door_open"),
    DOOR_CLOSED("door_closed"),
    POWERUP("powerup");

    private String name;

    private ParticleImage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
