package graph;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface IRoadNetwork {
    int getNodesCount();
    void addNode(int osmid, float longitude, float latitude);

    void addEdge(int u, int v, int cost);

    Node getNode(int id);

    List<Arc> getConnections(int id);

    Node getOsmNode(int osmid);

    List<Arc> getOsmConnections(int osmid);

    void readFromOsmFile(String osmFilePath)
            throws ParserConfigurationException, SAXException, IOException;

    void reduceToLargestConnectedComponent();
}
