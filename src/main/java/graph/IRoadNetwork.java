package graph;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;

public interface IRoadNetwork {
    int getNodesCount();
    void addNode(long osmid, float longitude, float latitude);

    void addEdge(long u, long v, int cost);

    Node getNode(int id);

    int getNodeIndex(long osmid);

    List<Arc> getConnections(int id);

    Node getOsmNode(long osmid);

    List<Arc> getOsmConnections(long osmid);

    void readFromOsmFile(String osmFilePath)
            throws ParserConfigurationException, SAXException, IOException;

    void reduceToLargestConnectedComponent();
}
