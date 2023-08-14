package graph;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface IRoadNetwork {
    void addNode(int osmid, float longitude, float latitude);

    void addEdge(int u, int v, long cost);

    Node getNode(int osmid);

    List<Arc> getConnections(int osmid);
    void readFromOsmFile(String osmFilePath)
            throws ParserConfigurationException, SAXException, IOException;
}
