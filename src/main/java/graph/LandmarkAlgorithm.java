package graph;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * An implementation of A* with the landmarks heuristic, as explained in
 * Lecture 3 of the course Efficient Route Planning, SS 2012.
 */
public class LandmarkAlgorithm {
    RoadNetwork _roadNetwork = null;
    DijkstrasAlgorithm _dijkstrasAlgorithm = null;
    HashMap<Integer, int[]> _landmarksDistances = new HashMap<>();
    private int [] distances = null;
    private int [] parents = null;
    private int _sourceNodeId, _targetNodeId, _nodesSettled;

    public LandmarkAlgorithm(RoadNetwork roadNetwork) {
        _roadNetwork = roadNetwork;
        _dijkstrasAlgorithm = new DijkstrasAlgorithm(_roadNetwork);
        distances = new int[_roadNetwork.getNodesCount()];
        parents = new int[_roadNetwork.getNodesCount()];
    }

    /**
     * Select the given number of landmarks at random.
     * @param numLandMarks
     */
    public void selectLandMarks(int numLandMarks) {
        Random random = new Random();
        while (numLandMarks > 0) {
            int landMarkId = random.nextInt(_roadNetwork.getNodesCount()-1);
            if(_landmarksDistances.containsKey(landMarkId)) {
                continue;
            }
            _landmarksDistances.put(landMarkId, null);
            numLandMarks--;
        }
    }

    /**
     * Precompute the distances to and from the selected landmarks.
     * NOTE: For our undirected / symmetric graphs, the distances *from* the
     * landmarks are enough, see Array<Array<int>> landmarkDistances below.
     */
    public void precomputeLandmarkDistances() {
        for (Integer landmarkId: _landmarksDistances.keySet()) {
            _dijkstrasAlgorithm.computeShortestPath(landmarkId, -1);
            _landmarksDistances.put(landmarkId, Arrays.copyOf(
                    _dijkstrasAlgorithm.getDistances(),
                    _dijkstrasAlgorithm.getDistances().length));
        }
    }

    /**
     * Compute the shortest paths from the given source to the given target node,
     * using A* with the landmark heuristic.
     * NOTE: this algorithm only works in point-to-point mode, so the option
     * targetNodeId == -1 does not make sense here.
     * @param sourceNodeId
     * @param targetNodeId
     * @return
     */
    public int computeShortestPath(int sourceNodeId, int targetNodeId) {
        if(targetNodeId == -1) {
            throw new IllegalArgumentException("this algorithm only works in point-to-point mode, targetNodeId can't be -1");
        }
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(parents, -1);
        _nodesSettled = 0;
        _sourceNodeId = sourceNodeId;
        _targetNodeId = targetNodeId;

        //special case when the source node is a landmark.
        //we have already cached all nodes distances from a landmark.
        //we simply return the landmark to targetNode distance below.
        if(_landmarksDistances.containsKey(sourceNodeId)) {
            int [] landMarkDistances = _landmarksDistances.get(sourceNodeId);
            distances[targetNodeId] = landMarkDistances[targetNodeId];
            return distances[targetNodeId];
        }

        boolean [] settled = new boolean[_roadNetwork.getNodesCount()];
        PriorityQueue<Arc> pq = new PriorityQueue<>(Comparator.comparingInt(Arc::cost));
        pq.add(new Arc(sourceNodeId, 0));
        distances[sourceNodeId] = 0;

        while (!pq.isEmpty()) {
            Arc arc = pq.poll();
            if(settled[arc.headNodeId()]) {
                continue;
            }

            List<Arc> connections = _roadNetwork.getConnections(arc.headNodeId());
            for (Arc road: connections) {
                if(settled[road.headNodeId()]) {
                    continue;
                }

                //the node we have reached is a landmark.
                //from a landmark to target node distance is pre-computed.
                if(_landmarksDistances.containsKey(road.headNodeId())) {
                    int[] landMarkDistances = _landmarksDistances.get(road.headNodeId());
                    int distanceToTargetViaThisLandMark = distances[arc.headNodeId()] + road.cost() + landMarkDistances[targetNodeId];
                    if(distanceToTargetViaThisLandMark < distances[targetNodeId]) {
                        distances[targetNodeId] = distanceToTargetViaThisLandMark;
                        pq.add(new Arc(targetNodeId, distanceToTargetViaThisLandMark));
                    }
                }

                int newDistance = distances[arc.headNodeId()] + road.cost();
                if(newDistance < distances[road.headNodeId()]) {
                    distances[road.headNodeId()] = newDistance;
                    //add the heuristic time in the pq so that we pick better nodes in next iteration.
                    pq.add(new Arc(road.headNodeId(),
                            newDistance + getLandMarksHeuristicCost(road.headNodeId(), targetNodeId)));
                    parents[road.headNodeId()] = arc.headNodeId();
                }
            }

            settled[arc.headNodeId()] = true;
            _nodesSettled++;
            if(settled[targetNodeId]) {
                break;
            }
        }

        return distances[targetNodeId];
    }

    int getLandMarksHeuristicCost(int currentNode, int targetNodeId) {
        int maxH = 0;
        for (Integer landmarkId: _landmarksDistances.keySet()) {
            //Math.abs(distance(currentNode, landmarkId) - distance(landmarkId, targetNodeId))
            int [] landMarkDistances = _landmarksDistances.get(landmarkId);
            int hu = Math.abs(landMarkDistances[currentNode] - landMarkDistances[targetNodeId]);
            maxH = Math.max(hu, maxH);
        }
        return maxH;
    }

    public int[] getDistances() {
        return distances;
    }

    public int getSettledNodes() {
        return _nodesSettled;
    }
}
