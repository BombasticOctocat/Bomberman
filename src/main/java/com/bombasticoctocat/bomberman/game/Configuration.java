package com.bombasticoctocat.bomberman.game;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.w3c.dom.*;

public class Configuration {
    private static HashMap<Integer, LevelConfiguration> levels = new HashMap<>();

    static {
        Document document = getConfigurationDocument();
        NodeList nodes = document.getElementsByTagName("levelConfiguration");
        for (int i = 0; i < nodes.getLength(); ++i) {
            LevelConfiguration levelConfiguration = getLevelConfiguration((Element) nodes.item(i));
            levels.put(levelConfiguration.level, levelConfiguration);
        }
        System.out.println("after static");
    }

    private static LevelConfiguration getLevelConfiguration(Element node) {
        int level = Integer.parseInt(node.getAttribute("level"));

        double bricksDensity = Double.valueOf(node.getElementsByTagName("bricksDensity").item(0).getTextContent());

        Powerup powerup = Powerup.valueOf(node.getElementsByTagName("powerup").item(0).getTextContent());

        Element penaltyGoomba = (Element) node.getElementsByTagName("penaltyGoomba").item(0);
        int penaltyGoombaLevel = Integer.valueOf(penaltyGoomba.getAttribute("level"));
        int penaltyGoombaAmount = Integer.valueOf(penaltyGoomba.getAttribute("amount"));

        NodeList goombasList = node.getElementsByTagName("goomba");
        ArrayList<Goomba.Type> goombas = new ArrayList<>();
        for (int i = 0; i < goombasList.getLength(); ++i) {
            Element goombaNode = (Element) goombasList.item(i);
            int goombaLevel = Integer.valueOf(goombaNode.getAttribute("level"));
            int goombaAmount = Integer.valueOf(goombaNode.getAttribute("amount"));
            Goomba.Type goombaType = Goomba.Type.valueOf("LEVEL" + goombaLevel);
            for (int j = 0; j < goombaAmount; ++j) {
                goombas.add(goombaType);
            }
        }

        return new LevelConfiguration(level, bricksDensity, goombas, powerup, penaltyGoombaAmount, penaltyGoombaLevel);
    }

    private static Document getConfigurationDocument() {
        try {
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(Configuration.class.getResource("levels_configuration.xsd"));
            schema.newValidator().validate(new StreamSource(Configuration.class.getResourceAsStream("levels_configuration.xml")));

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.parse(Configuration.class.getResourceAsStream("levels_configuration.xml"));
        } catch (Exception e) {
            throw new RuntimeException("Exception during trying to load levels_configuration.xml", e);
        }
    }

    public static LevelConfiguration forLevel(int level) {
        if (!levels.containsKey(level)) {
            throw new RuntimeException("Couldn't find level " + level + " in levels configuration");
        }
        return levels.get(level);
    }
}
