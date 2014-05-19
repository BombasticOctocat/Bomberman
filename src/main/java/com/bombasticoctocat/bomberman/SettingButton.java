package com.bombasticoctocat.bomberman;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class SettingButton extends Button {
    @FXML
    private ObjectProperty<Settings.Key> settingKey = new SimpleObjectProperty<>(this, "settingKey");

    public Settings.Key getSettingKey() {
        return settingKey.get();
    }

    public void setSettingKey(Settings.Key key) {
        settingKey.set(key);
    }

    public ObjectProperty<Settings.Key> settingKeyProperty() {
        return settingKey;
    }
}
