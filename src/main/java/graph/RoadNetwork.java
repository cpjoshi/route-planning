package graph;

import org.xml.sax.SAXException;
import osmxml.OsmXmlElementsHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A road network modelled as an undirected graph. We will use "arc" and "edge",
 * where "arc" is directed and "edge" is undirected. From the outside, we only
 * add "edges", but internally each edge is stored as a pair of "arcs" (with the
 * same pair of adjacent nodes but opposite directions).
 */
public class RoadNetwork implements IRoadNetwork {
    private HashMap<Integer, ArrayList<Arc>> _adjList = null;
    private HashMap<Integer, Node> _nodeList = null;
    private int _numNodes = 0;
    private int _numEdges = 0;
    public RoadNetwork() {
        _adjList = new HashMap<>();
        _nodeList = new HashMap<>();
    }

    @Override
    public void addNode(int osmid, float longitude, float latitude) {
        _nodeList.putIfAbsent(osmid, new Node(osmid, latitude, longitude));
        _adjList.putIfAbsent(osmid, new ArrayList<>());
        ++_numNodes;
    }

    @Override
    public void addEdge(int u, int v, long cost) {
        _adjList.get(u).add(new Arc(v, cost));
        _adjList.get(v).add(new Arc(u, cost));
        _numEdges += 1;
    }

    @Override
    public Node getNode(int osmid) {
        return _nodeList.getOrDefault(osmid, null);
    }

    @Override
    public List<Arc> getConnections(int osmid) {
        return _adjList.getOrDefault(osmid, new ArrayList<>());
    }

    @Override
    public void readFromOsmFile(String osmFilePath)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParser xmlParser =  SAXParserFactory.newDefaultInstance().newSAXParser();
        xmlParser.parse(osmFilePath, new OsmXmlElementsHandler(this));
    }

    @Override
    public String toString() {
        return String.format("V:%d, E:%d", _numNodes, _numEdges);
    }
}
