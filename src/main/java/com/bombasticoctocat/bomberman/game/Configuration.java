package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;

public class Configuration {
    private static ArrayList<LevelConfiguration> levels = new ArrayList<>();

    static {
        levels.add(new LevelConfiguration(1, 0.25, goombaTypes(6), Powerup.RANGE, 5, 1));
        levels.add(new LevelConfiguration(2, 0.25, goombaTypes(3, 3), Powerup.BOMB, 5, 2));
        levels.add(new LevelConfiguration(3, 0.25, goombaTypes(2, 2, 2), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(4, 0.25, goombaTypes(1, 1, 2, 2), Powerup.SPEED, 5, 3));
        levels.add(new LevelConfiguration(5, 0.25, goombaTypes(0, 4, 3), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(6, 0.25, goombaTypes(0, 2, 3, 2), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(7, 0.25, goombaTypes(0, 2, 3, 0, 2), Powerup.RANGE, 5, 3));
        levels.add(new LevelConfiguration(8, 0.25, goombaTypes(0, 1, 2, 4), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(9, 0.25, goombaTypes(0, 1, 1, 4, 1), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(10, 0.25, goombaTypes(0, 1, 1, 1, 3, 1), Powerup.WALLPASS, 5, 3));
        levels.add(new LevelConfiguration(11, 0.25, goombaTypes(0, 1, 2, 3, 1, 1), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(12, 0.25, goombaTypes(0, 1, 1, 1, 4, 1), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(13, 0.25, goombaTypes(0, 0, 3, 3, 3), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(14, 0.25, goombaTypes(0, 0, 0, 0, 0, 7, 1), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(15, 0.25, goombaTypes(0, 0, 1, 3, 3, 0, 1), Powerup.RANGE, 5, 3));
        levels.add(new LevelConfiguration(16, 0.25, goombaTypes(0, 0, 0, 3, 4, 0, 1), Powerup.WALLPASS, 5, 3));
        levels.add(new LevelConfiguration(17, 0.25, goombaTypes(0, 0, 5, 0, 2, 0, 1), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(18, 0.25, goombaTypes(3, 3, 0, 0, 0, 0, 2), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(19, 0.25, goombaTypes(1, 1, 3, 0, 0, 1, 2), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(20, 0.25, goombaTypes(0, 1, 1, 1, 2, 1, 2), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(21, 0.25, goombaTypes(0, 0, 0, 0, 4, 3, 2), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(22, 0.25, goombaTypes(0, 0, 4, 3, 1, 0, 1), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(23, 0.25, goombaTypes(0, 0, 2, 2, 2, 2, 1), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(24, 0.25, goombaTypes(0, 0, 1, 1, 4, 2, 1), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(25, 0.25, goombaTypes(0, 2, 1, 1, 2, 2, 1), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(26, 0.25, goombaTypes(1, 1, 1, 1, 2, 1, 1), Powerup.MYSTERY, 5, 3));
        levels.add(new LevelConfiguration(27, 0.25, goombaTypes(1, 1, 0, 0, 5, 1, 1), Powerup.RANGE, 5, 3));
        levels.add(new LevelConfiguration(28, 0.25, goombaTypes(0, 1, 3, 3, 1, 0, 1), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(29, 0.25, goombaTypes(0, 0, 0, 0, 2, 5, 2), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(30, 0.25, goombaTypes(0, 0, 3, 2, 1, 2, 1), Powerup.FLAMEPASS, 5, 3));
        levels.add(new LevelConfiguration(31, 0.25, goombaTypes(0, 2, 2, 2, 2, 2), Powerup.WALLPASS, 5, 3));
        levels.add(new LevelConfiguration(32, 0.25, goombaTypes(0, 1, 1, 3, 4, 0, 1), Powerup.BOMB, 5, 3));
        levels.add(new LevelConfiguration(33, 0.25, goombaTypes(0, 0, 2, 2, 3, 1, 2), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(34, 0.25, goombaTypes(0, 0, 2, 3, 3, 0, 2), Powerup.MYSTERY, 5, 3));
        levels.add(new LevelConfiguration(35, 0.25, goombaTypes(0, 0, 2, 1, 3, 1, 2), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(36, 0.25, goombaTypes(0, 0, 2, 2, 3, 0, 3), Powerup.FLAMEPASS, 5, 3));
        levels.add(new LevelConfiguration(37, 0.25, goombaTypes(0, 0, 2, 1, 3, 1, 3), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(38, 0.25, goombaTypes(0, 0, 2, 2, 3, 0, 3), Powerup.RANGE, 5, 3));
        levels.add(new LevelConfiguration(39, 0.25, goombaTypes(0, 0, 1, 1, 2, 2, 4), Powerup.WALLPASS, 5, 3));
        levels.add(new LevelConfiguration(40, 0.25, goombaTypes(0, 0, 1, 2, 3, 0, 4), Powerup.MYSTERY, 5, 3));
        levels.add(new LevelConfiguration(41, 0.25, goombaTypes(0, 0, 1, 1, 3, 1, 4), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(42, 0.25, goombaTypes(0, 0, 0, 1, 3, 1, 5), Powerup.WALLPASS, 5, 3));
        levels.add(new LevelConfiguration(43, 0.25, goombaTypes(0, 0, 0, 1, 2, 1, 6), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(44, 0.25, goombaTypes(0, 0, 0, 1, 2, 1, 6), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(45, 0.25, goombaTypes(0, 0, 0, 0, 2, 2, 6), Powerup.MYSTERY, 5, 3));
        levels.add(new LevelConfiguration(46, 0.25, goombaTypes(0, 0, 0, 0, 2, 2, 6), Powerup.WALLPASS, 5, 3));
        levels.add(new LevelConfiguration(47, 0.25, goombaTypes(0, 0, 0, 0, 2, 2, 6), Powerup.BOMBPASS, 5, 3));
        levels.add(new LevelConfiguration(48, 0.25, goombaTypes(0, 0, 0, 0, 2, 1, 6, 1), Powerup.DETONATOR, 5, 3));
        levels.add(new LevelConfiguration(49, 0.25, goombaTypes(0, 0, 0, 0, 1, 2, 6, 1), Powerup.FLAMEPASS, 5, 3));
        levels.add(new LevelConfiguration(50, 0.25, goombaTypes(0, 0, 0, 0, 1, 2, 5, 2), Powerup.MYSTERY, 5, 3));

    }

    public static LevelConfiguration forLevel(int level) {
        return levels.get(level - 1);
    }

    private static ArrayList<Goomba.Type> goombaTypes(Integer... l) {
        ArrayList<Goomba.Type> result = new ArrayList<>();
        for (int level = 0; level < l.length; level++) {
            for (int i = 0; i < l[level]; i++) {
                result.add(Goomba.Type.valueOf("LEVEL" + level));
            }
        }
        return result;
    }
}
