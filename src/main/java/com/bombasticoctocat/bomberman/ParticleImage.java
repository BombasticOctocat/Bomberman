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
    POWERUP_RANGE("powerup_range"),
    POWERUP_BOMB("powerup_bomb"),
    POWERUP_SPEED("powerup_speed"),
    POWERUP_DETONATOR("powerup_detonator"),
    POWERUP_WALLPASS("powerup_wallpass"),
    POWERUP_BOMBPASS("powerup_bombpass"),
    POWERUP_FLAMEPASS("powerup_flamepass"),
    POWERUP_MYSTERY("powerup_mystery");

    private String name;

    private ParticleImage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
