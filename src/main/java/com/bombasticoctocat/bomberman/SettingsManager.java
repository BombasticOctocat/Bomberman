package com.bombasticoctocat.bomberman;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class SettingsManager {
    public enum SettingType {
        CURRENT, DEFAULT
    }

    private final Preferences preferences = Preferences.userNodeForPackage(SettingsManager.class);
    private final Map<String, String> settings = new HashMap<>();
    private final Map<String, String> settingsDefault = new HashMap<>();

    private void buildSettings(Node node, String prefix) {
        prefix = prefix.replaceAll("^settings\\.", "") + node.getNodeName();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                Text text = (Text) child;
                String content = text.getTextContent().trim();
                if (content.length() > 0) {
                    settingsDefault.put(prefix, content);
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

    private String getSetting(String name, SettingType type) {
        if (type == SettingType.CURRENT) {
            return settings.get(name);
        } else {
            return settingsDefault.get(name);
        }
    }

    private void setSetting(String name, String value) {
        settings.put(name, value);
        preferences.put(name, value);
    }

    public <T extends Enum<T>> T getSetting(Settings.EnumField<T> key, SettingType type) {
        try {
            Class<? extends Enum<T>> enumClass = key.getEnumClass();
            Method valueOFMethod = enumClass.getDeclaredMethod("valueOf", String.class);
            return (T) valueOFMethod.invoke(null, getSetting(key.stringValue(), type));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends Enum<T>> void setSetting(Settings.EnumField<T> key, Enum<T> value) {
        setSetting(key.stringValue(), value.toString());
    }
}
