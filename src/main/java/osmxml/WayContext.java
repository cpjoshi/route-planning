package osmxml;

import java.util.ArrayList;

public class WayContext {
    public int id;
    public boolean isRoad;
    public String roadType;
    public ArrayList<Integer> nodesInPath;
    public WayContext(int id) {
        this.id = id;
        nodesInPath = new ArrayList<>();
    }
}
