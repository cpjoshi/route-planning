package graph;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

class RoadNetworkTest {

    @Test
    void readFromOsmFile_Nodes() throws IOException, URISyntaxException,
            ParserConfigurationException, SAXException {
        IRoadNetwork rn = new RoadNetwork();
        URI uripath = getClass().getClassLoader().getResource("osmnodes.xml").toURI();
        rn.readFromOsmFile(uripath.getPath());
        Assertions.assertEquals(rn.toString(), "V:10, E:0");
    }

    @Test
    void readFromOsmFile_Network() throws IOException, URISyntaxException,
            ParserConfigurationException, SAXException {
        IRoadNetwork rn = new RoadNetwork();
        URI uripath = getClass().getClassLoader().getResource("osmnodesAndWays.xml").toURI();
        rn.readFromOsmFile(uripath.getPath());
        Assertions.assertEquals("V:5, E:3", rn.toString());
        Assertions.assertEquals("[Arc[headNodeId=3, cost=2]]", rn.getOsmConnections(500863).toString());
    }

    @Test
    void reduceToLargestConnectedComponent_1() throws IOException, URISyntaxException,
            ParserConfigurationException, SAXException {
        IRoadNetwork rn = new RoadNetwork();
        URI uripath = getClass().getClassLoader().getResource("osmnodesAndWays.xml").toURI();
        rn.readFromOsmFile(uripath.getPath());
        Assertions.assertEquals("V:5, E:3", rn.toString());
        Assertions.assertEquals("[Arc[headNodeId=3, cost=2]]", rn.getOsmConnections(500863).toString());

        rn.reduceToLargestConnectedComponent();
        Assertions.assertEquals("V:3, E:2", rn.toString());
        Assertions.assertEquals("[Arc[headNodeId=1, cost=2]]", rn.getOsmConnections(500863).toString());
    }

    @Test
    void reduceToLargestConnectedComponent_2() {
        IRoadNetwork rn = new RoadNetwork();
        rn.addNode(0,0,0);
        rn.addNode(1,1,1);
        rn.addNode(2,2,2);

        rn.addEdge(0,1,1);
        rn.addEdge(1,2,2);
        rn.addEdge(2,0,2);

        rn.reduceToLargestConnectedComponent();
        Assertions.assertEquals("V:3, E:3", rn.toString());
        Assertions.assertEquals("[Arc[headNodeId=2, cost=2], Arc[headNodeId=0, cost=1]]", rn.getOsmConnections(1).toString());
    }

    @Test
    void reduceToLargestConnectedComponent_3() {
        IRoadNetwork rn = new RoadNetwork();
        rn.addNode(0,0,0);
        rn.addNode(1,1,1);
        rn.addNode(2,2,2);
        rn.addNode(3,2,2);
        rn.addNode(4,2,2);
        rn.addNode(5,2,2);
        rn.addNode(6,2,2);

        //loop
        rn.addEdge(0,1,1);
        rn.addEdge(1,2,2);
        rn.addEdge(2,0,2);

        //no-loop
        rn.addEdge(3,4,1);
        rn.addEdge(4,5,2);
        rn.addEdge(5,6,2);
        Assertions.assertEquals("V:7, E:6", rn.toString());
        rn.reduceToLargestConnectedComponent();
        Assertions.assertEquals("V:4, E:3", rn.toString());
        Assertions.assertEquals("[Arc[headNodeId=1, cost=1]]", rn.getOsmConnections(3).toString());
    }

    @Test
    void reduceToLargestConnectedComponent_4() {
        IRoadNetwork rn = new RoadNetwork();
        rn.addNode(10,0,0);
        rn.addNode(11,1,1);
        rn.addNode(12,2,2);
        rn.addNode(13,2,2);
        rn.addNode(14,2,2);
        rn.addNode(15,2,2);
        rn.addNode(16,2,2);

        rn.addEdge(10, 11, 2);
        rn.addEdge(11, 14, 2);
        rn.addEdge(14, 16, 3);
        rn.addEdge(16, 10, 3);
        Assertions.assertEquals("V:7, E:4", rn.toString());
        rn.reduceToLargestConnectedComponent();
        Assertions.assertEquals("V:4, E:4", rn.toString());
        Assertions.assertFalse(rn.getConnections(1).toString().equals("[Arc[headNodeId=0, cost=2], Arc[headNodeId=4, cost=2]]"));
        Assertions.assertEquals("[Arc[headNodeId=2, cost=2], Arc[headNodeId=0, cost=2]]", rn.getConnections(1).toString());
    }


}