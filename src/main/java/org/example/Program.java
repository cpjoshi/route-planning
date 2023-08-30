package org.example;

import graph.DijkstrasAlgorithm;
import graph.RoadNetwork;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Random;

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
            System.out.println("Full graph:" + rn.toString());
            System.out.printf("TimeTaken: %d\n", System.currentTimeMillis() - startTime);

            rn.reduceToLargestConnectedComponent();
            System.out.println("Largest connected component: "+ rn.toString());

            DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(rn);
            System.out.printf("Time-to-drive, queryTime\n");
            for(int i=0; i<100; i++) {
                Random random = new Random();
                int source = random.nextInt(rn.getNodesCount());
                int dest = random.nextInt(rn.getNodesCount());
                startTime = System.currentTimeMillis();
                int dist = dijkstrasAlgorithm.computeShortestPath(source, dest);
                System.out.printf("%d, %dms\n", dist, System.currentTimeMillis() - startTime);
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");
    }
}
