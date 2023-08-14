package common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoadUtilsTest {
    @Test
    void getDistance() {
        Assertions.assertEquals(5.0, RoadUtils.getDistance(0,0, 3,4));
        Assertions.assertEquals(5.0, RoadUtils.getDistance(1,1, 4,5));
    }
}