package graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DijkstrasAlgorithmTest {
    @Test
    void computeShortestPath() {
        RoadNetwork rn = new RoadNetwork();
        rn.addNode(0,1,1);
        rn.addNode(1,1,1);
        rn.addNode(2,1,1);
        rn.addNode(3,1,1);
        rn.addNode(4,1,1);
        rn.addNode(5,1,1);
        rn.addNode(6,1,1);
        rn.addNode(7,1,1);
        rn.addNode(8,1,1);

        rn.addEdge(0,1,1);
        rn.addEdge(0,2,1);
        rn.addEdge(0,3,2);
        rn.addEdge(1,4,2);
        rn.addEdge(2,4,1);
        rn.addEdge(2,5,1);
        rn.addEdge(3,5,3);
        rn.addEdge(4,8,7);
        rn.addEdge(5,6,1);
        rn.addEdge(5,7,3);
        rn.addEdge(5,8,4);
        rn.addEdge(6,7,1);
        rn.addEdge(7,8,1);
        Assertions.assertEquals("V:9, E:13", rn.toString());
        rn.reduceToLargestConnectedComponent();
        Assertions.assertEquals("V:9, E:13", rn.toString());

        DijkstrasAlgorithm dijkstrasAlgorithm = new DijkstrasAlgorithm(rn);
        int dist = dijkstrasAlgorithm.computeShortestPath(0, 8);
        Assertions.assertEquals(5, dist);
        Assertions.assertEquals(2, dijkstrasAlgorithm.computeShortestPath(0,4));
        Assertions.assertEquals(5, dijkstrasAlgorithm.computeShortestPath(4,8));

        System.out.println(dijkstrasAlgorithm.shortestPathString());
    }
}