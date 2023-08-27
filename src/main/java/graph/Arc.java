package graph;

/**
 * A weighted path to a osmId node.
 * @param headNodeId (tailNode ------> headNode)
 * @param cost is time taken to travel to this node.
 */
public record Arc(int headNodeId, int cost) {
}
