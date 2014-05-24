package com.bombasticoctocat.bomberman;

import com.bombasticoctocat.bomberman.game.Directions;
import com.google.inject.Inject;
import javafx.scene.input.KeyCode;

public class Settings {
    @Inject private static SettingsManager settingsManager;

    public interface  Field<T> {
        public void setSetting(T value);
        public T getSetting();
        public T getDefaultSetting();
    }

    public interface EnumField<T extends Enum<T>> extends Field<T> {
        public String stringValue();
        public Class<? extends Enum<T>> getEnumClass();
    }

    public static enum Key implements EnumField<KeyCode> {
        PAUSE("controls.pause"),
        BOMB("controls.bomb"),
        DETONATE("controls.detonate"),
        UP("controls.up"),
        DOWN("controls.down"),
        LEFT("controls.left"),
        RIGHT("controls.right");

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

        @Override
        public void setSetting(KeyCode value) {
            settingsManager.setSetting(this, value);
        }

        @Override
        public KeyCode getSetting() {
            return settingsManager.getSetting(this, SettingsManager.SettingType.CURRENT);
        }

        @Override
        public KeyCode getDefaultSetting() {
            return settingsManager.getSetting(this, SettingsManager.SettingType.DEFAULT);
        }
    }

    public static enum DirectionKey implements EnumField<KeyCode> {
        UP(Key.UP, Directions.Direction.UP),
        DOWN(Key.DOWN, Directions.Direction.DOWN),
        LEFT(Key.LEFT, Directions.Direction.LEFT),
        RIGHT(Key.RIGHT, Directions.Direction.RIGHT);

        private String name;
        private Directions.Direction direction;

        private DirectionKey(Key key, Directions.Direction dir) {
            this.direction = dir;
            this.name = key.stringValue();
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

        @Override
        public void setSetting(KeyCode value) {
            settingsManager.setSetting(this, value);
        }

        @Override
        public KeyCode getSetting() {
            return settingsManager.getSetting(this, SettingsManager.SettingType.CURRENT);
        }

        @Override
        public KeyCode getDefaultSetting() {
            return settingsManager.getSetting(this, SettingsManager.SettingType.DEFAULT);
        }
    }
}
