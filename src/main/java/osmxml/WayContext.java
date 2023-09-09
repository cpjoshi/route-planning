package osmxml;

import java.util.ArrayList;

public class WayContext {
    public Long id;
    public boolean isRoad;
    public String roadType;
    public ArrayList<Long> nodesInPath;
    public WayContext(Long id) {
        this.id = id;
        nodesInPath = new ArrayList<>();
    }
}
