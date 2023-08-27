package graph;

import org.xml.sax.SAXException;
import osmxml.OsmXmlElementsHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * A road network modelled as an undirected graph. We will use "arc" and "edge",
 * where "arc" is directed and "edge" is undirected. From the outside, we only
 * add "edges", but internally each edge is stored as a pair of "arcs" (with the
 * same pair of adjacent nodes but opposite directions).
 */
public class RoadNetwork implements IRoadNetwork {
    private ArrayList<ArrayList<Arc>> _adjList = null;
    private ArrayList<Node> _nodeList = null;
    private HashMap<Integer, Integer> _osmIndex = null;
    private int _numNodes = 0;
    private int _numEdges = 0;
    public RoadNetwork() {
        _adjList = new ArrayList<>();
        _nodeList = new ArrayList<>();
        _osmIndex = new HashMap<>();
    }

    @Override
    public void addNode(int osmid, float longitude, float latitude) {
        ++_numNodes;
        _nodeList.add(new Node(osmid, latitude, longitude));
        _adjList.add(new ArrayList<>());
        _osmIndex.put(osmid, _numNodes-1);
    }

    public int getNodesCount() {
        return _numNodes;
    }


    @Override
    public void addEdge(int u, int v, int cost) {
        int from = _osmIndex.get(u);
        int to = _osmIndex.get(v);
        _adjList.get(from).add(new Arc(to, cost));
        _adjList.get(to).add(new Arc(from, cost));
        _numEdges += 1;
    }

    @Override
    public Node getOsmNode(int osmid) {
        if(!_osmIndex.containsKey(osmid)) {
            return null;
        }

        int index = _osmIndex.get(osmid);
        return _nodeList.get(index);
    }

    @Override
    public List<Arc> getOsmConnections(int osmid) {
        if(!_osmIndex.containsKey(osmid)) {
            return null;
        }

        int index = _osmIndex.get(osmid);
        return _adjList.get(index);
    }

    public Node getNode(int id) {
        return _nodeList.get(id);
    }

    public List<Arc> getConnections(int id) {
        return _adjList.get(id);
    }

    @Override
    public void readFromOsmFile(String osmFilePath)
            throws ParserConfigurationException, SAXException, IOException {
        SAXParser xmlParser =  SAXParserFactory.newDefaultInstance().newSAXParser();
        xmlParser.parse(osmFilePath, new OsmXmlElementsHandler(this));
    }

    @Override
    public void reduceToLargestConnectedComponent() {
        int [] componentOf = new int[_numNodes];
        Arrays.fill(componentOf, -1);
        int currentConnectedComponent = 0;
        int maxNodes = Integer.MIN_VALUE, maxComponentId = Integer.MIN_VALUE;
        for(int i=0; i<_numNodes; i++) {
            if(componentOf[i] == -1) {
                int nodesInCurrentComponent = dfs(i, currentConnectedComponent, componentOf);
                if(nodesInCurrentComponent > maxNodes) {
                    maxNodes = nodesInCurrentComponent;
                    maxComponentId = currentConnectedComponent;
                }
                currentConnectedComponent++;
            }
        }

        int removedConnections = 0;
        for(int i=_numNodes-1; i>=0; i--) {
            if(componentOf[i] != maxComponentId) {
                //node and node-mappings removal.
                _nodeList.remove(i);
                _numNodes--;

                //edges removal
                removedConnections += _adjList.get(i).size();
                _adjList.remove(i);
            }
        }
        _numEdges -= (removedConnections/2);

        //recreate the index
        _osmIndex.clear();
        for(int i=0; i<_numNodes; i++) {
            _osmIndex.put(_nodeList.get(i).osmid(), i);
        }
    }

    int dfs(int startingNode, int componentId, int [] componentOf) {
        int totalNodesTraversed = 0;
        Queue<Integer> q = new LinkedList<>();
        HashMap<Integer, Boolean> visited = new HashMap<>();
        q.add(startingNode);
        while (!q.isEmpty()) {
            int current = q.poll();
            if(visited.containsKey(current)) {
                continue;
            }
            visited.put(current, true);
            componentOf[current] = componentId;
            ArrayList<Arc> adjlist = _adjList.get(current);
            for(Arc a : adjlist) {
                q.add(a.headNodeId());
            }
            totalNodesTraversed++;
        }
        return totalNodesTraversed;
    }

    @Override
    public String toString() {
        return String.format("V:%d, E:%d", _numNodes, _numEdges);
    }
}
