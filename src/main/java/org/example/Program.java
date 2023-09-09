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
            String fileUrl = "file:/C:/Users/chjos/Downloads/saarland/saarland.osm";
            System.out.println("Analyzing saarland...");
            osmDataAnalysis(fileUrl);

            fileUrl = "file:/C:/Users/chjos/Downloads/baden-wuerttemberg/baden-wuerttemberg.osm";
            System.out.println("Analyzing baden-wuerttemberg...");
            osmDataAnalysis(fileUrl);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        System.out.println("Done!");
    }

    private static void osmDataAnalysis(String fileUrl)
            throws ParserConfigurationException, SAXException, IOException {
        RoadNetwork rn = new RoadNetwork();
        long startTime = System.currentTimeMillis();
        rn.readFromOsmFile(fileUrl);
        System.out.println("Full graph:" + rn.toString());
        System.out.printf("TimeTaken: %d\n", System.currentTimeMillis() -
                startTime);

        rn.reduceToLargestConnectedComponent();
        System.out.println("Largest connected component: "+ rn.toString());

        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(rn);
        System.out.printf("Time-to-drive, queryTime, SettledNodes\n");
        long totalTime = 0;
        long totalSettledNodes = 0;
        long totalQueryTime = 0;

        Random random = new Random();
        for(int i=0; i<100; i++) {
            int source = random.nextInt(rn.getNodesCount());
            int dest = random.nextInt(rn.getNodesCount());
            startTime = System.currentTimeMillis();
            int dist = dijkstrasAlgorithm.computeShortestPath(source, dest);
            long queryTime = System.currentTimeMillis() - startTime;
            totalTime += dist;
            totalSettledNodes += dijkstrasAlgorithm.getSettledNodes();
            totalQueryTime += queryTime;

            System.out.printf("%s, %d, %d\n",
                    String.format("%dm:%ds", dist/60, dist%60),
                    dijkstrasAlgorithm.getSettledNodes(),
                    queryTime);
        }
        totalTime = totalTime/100;
        System.out.printf("Avg Travel Time: %s, Avg Settled Nodes: %d, Avg Query Time: %d\n",
                String.format("%dm:%ds", totalTime/60, totalTime%60),
                totalSettledNodes/100,
                totalQueryTime/100);
    }
}
