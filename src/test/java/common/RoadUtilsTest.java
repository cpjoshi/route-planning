package common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoadUtilsTest {
    @Test
    void getDistance() {
        Assertions.assertEquals(439988.38890248, RoadUtils.getDistance(0,0, 3,4));
        Assertions.assertEquals(439988.38890248, RoadUtils.getDistance(1,1, 4,5));
    }
}