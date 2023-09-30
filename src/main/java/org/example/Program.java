package org.example;

import graph.DijkstrasAlgorithm;
import graph.LandmarkAlgorithm;
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
            //osmDataAnalysis(fileUrl);
            osmDataShortestDistanceComparison(fileUrl);

            fileUrl = "file:/C:/Users/chjos/Downloads/baden-wuerttemberg/baden-wuerttemberg.osm";
            System.out.println("Analyzing baden-wuerttemberg...");
            //osmDataAnalysis(fileUrl);
            osmDataShortestDistanceComparison(fileUrl);
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
            int dijikstraDist = dijkstrasAlgorithm.computeShortestPath(source, dest);
            long dijkstraQueryTime = System.currentTimeMillis() - startTime;
            totalTime += dijikstraDist;
            totalSettledNodes += dijkstrasAlgorithm.getSettledNodes();
            totalQueryTime += dijkstraQueryTime;

            System.out.printf("%s, %d, %d\n",
                    String.format("%dm:%ds", dijikstraDist/60, dijikstraDist%60),
                    dijkstrasAlgorithm.getSettledNodes(),
                    dijkstraQueryTime);
        }
        totalTime = totalTime/100;
        System.out.printf("Avg Travel Time: %s, Avg Settled Nodes: %d, Avg Query Time: %d\n",
                String.format("%dm:%ds", totalTime/60, totalTime%60),
                totalSettledNodes/100,
                totalQueryTime/100);
    }

    private static void osmDataShortestDistanceComparison(String fileUrl)
            throws ParserConfigurationException, SAXException, IOException {
        RoadNetwork rn = new RoadNetwork();
        rn.readFromOsmFile(fileUrl);
        rn.reduceToLargestConnectedComponent();
        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(rn);
        //Setup landmark test as well.
        long startTime = System.currentTimeMillis();
        LandmarkAlgorithm landmarkAlgorithm = new LandmarkAlgorithm(rn);
        landmarkAlgorithm.selectLandMarks(42);
        landmarkAlgorithm.precomputeLandmarkDistances();
        System.out.printf("Time for adding 42 landmarks: %d ms\n", System.currentTimeMillis()-startTime);


        System.out.printf("dj-timeToDrive, lm-timeToDrive, dj-queryTime, lm-queryTime, dj-SettledNodes, lm-SettledNodes\n");
        long totalTime = 0;
        long totalSettledNodes = 0;
        long totalQueryTime = 0;

        Random random = new Random();
        for(int i=0; i<100; i++) {
            int source = random.nextInt(rn.getNodesCount());
            int dest = random.nextInt(rn.getNodesCount());
            startTime = System.currentTimeMillis();
            int dijikstraDist = dijkstrasAlgorithm.computeShortestPath(source, dest);
            long dijkstraQueryTime = System.currentTimeMillis() - startTime;

            startTime = System.currentTimeMillis();
            int lmDist = landmarkAlgorithm.computeShortestPath(source, dest);
            long lmQueryTime = System.currentTimeMillis() - startTime;

            totalTime += lmDist;
            totalSettledNodes += landmarkAlgorithm.getSettledNodes();
            totalQueryTime += lmQueryTime;

            System.out.printf("%s, %s, %d, %d, %d, %d\n",
                    String.format("%dm:%ds", dijikstraDist/60, dijikstraDist%60),
                    String.format("%dm:%ds", lmDist/60, lmDist%60),
                    dijkstrasAlgorithm.getSettledNodes(),
                    landmarkAlgorithm.getSettledNodes(),
                    dijkstraQueryTime,
                    lmQueryTime);
        }

        totalTime = totalTime/100;
        System.out.printf("Avg Travel Time: %s, Avg Settled Nodes: %d, Avg Query Time: %d\n",
                String.format("%dm:%ds", totalTime/60, totalTime%60),
                totalSettledNodes/100,
                totalQueryTime/100);
    }
}
