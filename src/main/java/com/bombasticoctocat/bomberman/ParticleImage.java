package com.bombasticoctocat.bomberman;

public enum ParticleImage {
    BOMB("bomb"),
    BRICKS("bricks"),
    CHARACTER("character"),
    CONCRETE("concrete"),
    EMPTY("empty"),
    FLAMES("flames"),
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
    POWERUP_MYSTERY("powerup_mystery"),
    GOOMBA_LEVEL0("goomba_level0"),
    GOOMBA_LEVEL1("goomba_level1"),
    GOOMBA_LEVEL2("goomba_level2"),
    GOOMBA_LEVEL3("goomba_level3"),
    GOOMBA_LEVEL4("goomba_level4"),
    GOOMBA_LEVEL5("goomba_level5"),
    GOOMBA_LEVEL6("goomba_level6"),
    GOOMBA_LEVEL7("goomba_level7");

    private String name;

    private ParticleImage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
