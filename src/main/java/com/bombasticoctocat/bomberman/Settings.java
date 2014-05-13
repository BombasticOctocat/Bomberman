package com.bombasticoctocat.bomberman;

import com.bombasticoctocat.bomberman.game.Directions;
import javafx.scene.input.KeyCode;

public class Settings {
    public interface EnumField<T extends Enum<T>> {
        public String stringValue();
        public Class<? extends Enum<T>> getEnumClass();
    }

    public enum Key implements EnumField<KeyCode> {
        PAUSE("controls.pause"),
        BOMB("controls.bomb");

        private String name;
        private Key(String name) {
            this.name = name;
        }

        @Override
        public String stringValue() {
            return name;
        }

        @Override
        public Class<? extends Enum<KeyCode>> getEnumClass() {
            return KeyCode.class;
        }
    }

    public enum DirectionKey implements EnumField<KeyCode> {
        UP("controls.up", Directions.Direction.UP),
        DOWN("controls.down", Directions.Direction.DOWN),
        LEFT("controls.left", Directions.Direction.LEFT),
        RIGHT("controls.right", Directions.Direction.RIGHT);

        private String name;
        private Directions.Direction direction;
        private DirectionKey(String name, Directions.Direction dir) {
            this.direction = dir;
            this.name = name;
        }

        @Override
        public String stringValue() {
            return name;
        }

        @Override
        public Class<? extends Enum<KeyCode>> getEnumClass() {
            return KeyCode.class;
        }

        public Directions.Direction getDirection() {
            return direction;
        }
    }
}
