package graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * An implementation of Dijkstra'a algorithm for computing shortest paths.
 */
public class DijkstrasAlgorithm {
    IRoadNetwork _graph = null;
    private int [] distances = null;
    private int [] parents = null;
    private int _sourceNodeId, _targetNodeId, _nodesSettled;
    public DijkstrasAlgorithm(RoadNetwork graph) {
        _graph = graph;
        distances = new int[_graph.getNodesCount()];
        parents = new int[_graph.getNodesCount()];
    }

    public int computeShortestPath(int sourceNodeId, int targetNodeId) {
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(parents, -1);
        _nodesSettled = 0;
        _sourceNodeId = sourceNodeId;
        _targetNodeId = targetNodeId;
        boolean [] settled = new boolean[_graph.getNodesCount()];
        PriorityQueue<Arc> pq = new PriorityQueue<>(Comparator.comparingInt(Arc::cost));
        pq.add(new Arc(sourceNodeId, 0));
        distances[sourceNodeId] = 0;

        while (!pq.isEmpty()) {
            Arc arc = pq.poll();
            if(settled[arc.headNodeId()]) {
                continue;
            }

            List<Arc> connections = _graph.getConnections(arc.headNodeId());
            for (Arc road: connections) {
                if(settled[road.headNodeId()]) {
                    continue;
                }

                int newDistance = road.cost() + distances[arc.headNodeId()];
                if(newDistance < distances[road.headNodeId()]) {
                    distances[road.headNodeId()] = newDistance;
                    pq.add(new Arc(road.headNodeId(), newDistance));
                    parents[road.headNodeId()] = arc.headNodeId();
                }
            }

            settled[arc.headNodeId()] = true;
            _nodesSettled++;
            if(targetNodeId != -1 && settled[targetNodeId]) {
                break;
            }
        }

        return (targetNodeId == -1) ? -1 : distances[targetNodeId];
    }

    public int[] getDistances() {
        return distances;
    }

    public int getSettledNodes() {
        return _nodesSettled;
    }

    public String shortestPathString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_targetNodeId);
        sb.append("<-");
        while (parents[_targetNodeId] != _sourceNodeId) {
            sb.append(parents[_targetNodeId]);
            sb.append("<-");
            _targetNodeId = parents[_targetNodeId];
        }
        sb.append(_sourceNodeId);
        return sb.toString();
    }
}
