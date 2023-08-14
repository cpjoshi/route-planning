package org.example;

import graph.RoadNetwork;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Program {
    /**
     * Main method.
     * @param args
     */
    public static void main(final String[] args) {
        try {
            RoadNetwork rn = new RoadNetwork();
            rn.readFromOsmFile("file:/C:/Users/chjos/Downloads/saarland/saarland.osm");
            System.out.println(rn.toString());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");
    }
}
