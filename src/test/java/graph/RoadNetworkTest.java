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
        Assertions.assertEquals("V:5, E:6", rn.toString());
        Assertions.assertEquals("[Arc[headNodeId=500865, cost=28]]", rn.getConnections(500863).toString());
    }
}