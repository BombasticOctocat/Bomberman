package com.bombasticoctocat.bomberman;

public enum ParticleImage {
    BOMB("bomb"),
    BRICKS("bricks"),
    CHARACTER("character"),
    CONCRETE("concrete"),
    EMPTY("empty"),
    FLAMES("flames"),
    GOOMBA("goomba"),
    KILLED("killed");

    private String name;

    private ParticleImage(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
