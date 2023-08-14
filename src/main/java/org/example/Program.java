package org.example;

import graph.RoadNetwork;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * to run this: java -cp .\route-planning.main.jar org.example.Program
 */
public class Program {
    /**
     * Main method.
     * @param args
     */
    public static void main(final String[] args) {
        try {
            long startTime = System.currentTimeMillis();
            RoadNetwork rn = new RoadNetwork();
            rn.readFromOsmFile("file:/C:/Users/chjos/Downloads/saarland/saarland.osm");
            long timeTaken = System.currentTimeMillis() - startTime;
            System.out.println(rn.toString());
            System.out.printf("TimeTaken: %d\n", timeTaken);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");
    }
}
