package common;

import java.util.HashMap;

public class RoadUtils {
    private static HashMap<String, Integer> roadTypeSpeeds = new HashMap<>();
    static {
        roadTypeSpeeds.put("motorway", 110);
        roadTypeSpeeds.put("trunk", 110);
        roadTypeSpeeds.put("primary", 70);
        roadTypeSpeeds.put("secondary", 60);
        roadTypeSpeeds.put("tertiary", 50);
        roadTypeSpeeds.put("motorway_link", 50);
        roadTypeSpeeds.put("trunk_link", 50);
        roadTypeSpeeds.put("primary_link", 50);
        roadTypeSpeeds.put("secondary_link", 50);
        roadTypeSpeeds.put("road", 40);
        roadTypeSpeeds.put("unclassified", 40);
        roadTypeSpeeds.put("residential", 30);
        roadTypeSpeeds.put("unsurfaced", 30);
        roadTypeSpeeds.put("living_street", 10);
        roadTypeSpeeds.put("service", 5);
    }

    public static boolean isValidRoadType(String roadType) {
        return roadTypeSpeeds.containsKey(roadType);
    }
    public static int getRoadSpeed(String roadType) {
        return roadTypeSpeeds.getOrDefault(roadType, -1);
    }

    /**
     * Earth is sphere, and it's weird to calculate the euclidean distance of (lat, long) coordinates.
     * but professor says euclidean distance is good enough.
     * @param lat1
     * @param long1
     * @param lat2
     * @param long2
     * @return
     */
    public static double getDistance(float lat1, float long1, float lat2,
                                     float long2) {
        return Math.sqrt(Math.pow(lat2-lat1, 2) + Math.pow(long2 - long1, 2));
    }
}
