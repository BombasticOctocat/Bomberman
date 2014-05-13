package com.bombasticoctocat.bomberman;

import java.io.InputStream;
import java.util.*;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class SettingsManager {
    Preferences preferences = Preferences.userNodeForPackage(SettingsManager.class);
    Map<String, String> setings = new HashMap<>();

    private void buildSettings(Node node, String prefix) {
        prefix = prefix.replaceAll("^settings\\.", "") + node.getNodeName();
        NodeList children = node.getChildNodes();
        for (int i = 0; i < children.getLength(); ++i) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.TEXT_NODE) {
                Text text = (Text) child;
                String content = text.getTextContent().trim();
                if (content.length() > 0) {
                    setings.put(prefix, content);
                }
            }
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                buildSettings(child, prefix + ".");
            }
        }
    }

    SettingsManager() {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            InputStream fis = getClass().getResourceAsStream("default_settings.xml");

            Document document = builder.parse(fis);
            document.normalizeDocument();

            buildSettings(document.getDocumentElement(), "");

            for (Map.Entry<String, String> setting: setings.entrySet()) {
                setting.setValue(preferences.get(setting.getKey(), setting.getValue()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    String getSetting(String name) {
        return setings.get(name);
    }

    void setSetting(String name, String value) {
        setings.put(name, value);
        preferences.put(name, value);
    }

    Map<String, String> getSettings(String pattern) {
        Map<String, String> res = new HashMap<>();
        for (Map.Entry<String, String> entry: setings.entrySet()) {
            if (entry.getKey().matches(pattern)) {
                res.put(entry.getKey(), entry.getValue());
            }
        }
        return res;
    }

    void setSetings(Map<String, String> values) {
        for (Map.Entry<String, String> entry: values.entrySet()) {
            setSetting(entry.getKey(), entry.getValue());
        }
    }
}
