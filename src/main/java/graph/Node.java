package graph;

public record Node(int osmid, float longitude, float latitude) {
    public Node deepCopy() {
        return new Node(this.osmid, this.longitude, this.latitude);
    }
}
