package com.bombasticoctocat.bomberman;

import java.io.InputStream;
import java.util.*;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import com.bombasticoctocat.bomberman.game.Directions;
import javafx.scene.input.KeyCode;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class SettingsManager {
    public interface Field {
        public String stringValue();
    }

    public enum DirectionKey implements Field {
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

        public String stringValue() {
            return name;
        }

        public Directions.Direction getDirection() {
            return direction;
        }
    }

    public enum Key implements Field {
        PAUSE("controls.pause"),
        BOMB("controls.bomb");

        private String name;
        private Key(String name) {
            this.name = name;
        }
        public String stringValue() {
            return name;
        }
    }

    private final Preferences preferences = Preferences.userNodeForPackage(SettingsManager.class);
    private final Map<String, String> settings = new HashMap<>();

    private void buildSettings(Node node, String prefix) {
        prefix = prefix.replaceAll("^settings\\.", "") + node.getNodeName();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                Text text = (Text) child;
                String content = text.getTextContent().trim();
                if (content.length() > 0) {
                    settings.put(prefix, content);
                }
            }
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                buildSettings(child, prefix + ".");
            }
        }
    }

    public SettingsManager() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream fis = getClass().getResourceAsStream("default_settings.xml");

            Document document = builder.parse(fis);
            document.normalizeDocument();

            buildSettings(document.getDocumentElement(), "");

            for (Map.Entry<String, String> setting: settings.entrySet()) {
                setting.setValue(preferences.get(setting.getKey(), setting.getValue()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public KeyCode getKeySetting(Field key) {
        return KeyCode.valueOf(settings.get(key.stringValue()));
    }

    public void setSetting(String name, String value) {
        settings.put(name, value);
        preferences.put(name, value);
    }

    public Map<String, String> getSettings(String pattern) {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, String> entry: settings.entrySet()) {
            if (entry.getKey().matches(pattern)) {
                res.put(entry.getKey(), entry.getValue());
            }
        }
        return res;
    }

    public void setSettings(Map<String, String> values) {
        for (Map.Entry<String, String> entry: values.entrySet()) {
            setSetting(entry.getKey(), entry.getValue());
        }
    }
}
