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
            System.out.printf("Time-to-drive, queryTime, SettledNodes\n");
            long totalTime = 0;
            long totalSettledNodes = 0;
            for(int i=0; i<100; i++) {
                Random random = new Random();
                int source = random.nextInt(rn.getNodesCount());
                int dest = random.nextInt(rn.getNodesCount());
                startTime = System.currentTimeMillis();
                int dist = dijkstrasAlgorithm.computeShortestPath(source, dest);
                totalTime += dist;
                totalSettledNodes += dijkstrasAlgorithm.getSettledNodes();
                System.out.printf("%s, %d, %d\n",
                        String.format("%dm:%ds", dist/60, dist%60),
                        dijkstrasAlgorithm.getSettledNodes(),
                        System.currentTimeMillis() - startTime);
            }
            totalTime = totalTime/100;
            System.out.printf("Avg Travel Time: %s, Avg Settled Nodes: %d\n", String.format("%dm:%ds", totalTime/60, totalTime%60),
                    totalSettledNodes/100);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");
    }
}
