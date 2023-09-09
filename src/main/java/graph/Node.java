package graph;

public record Node(long osmid, float longitude, float latitude) {
    public Node deepCopy() {
        return new Node(this.osmid, this.longitude, this.latitude);
    }
}
