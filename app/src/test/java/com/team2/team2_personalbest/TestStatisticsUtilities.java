package com.team2.team2_personalbest;

import org.junit.Test;

import static org.junit.Assert.*;

public class TestStatisticsUtilities {

    @Test
    public void testDistanceZeroSteps() {
        assertEquals(0, StatisticsUtilities.calculateWorkoutDistance(0, 1), 0);
    }

    @Test
    public void testDistanceRealInput() {
        assertEquals(1.33, StatisticsUtilities.calculateWorkoutDistance(5442, 1.3), 0.1);
    }

    @Test
    public void testDistanceZeroStepsZeroStride() {
        assertEquals(0, StatisticsUtilities.calculateWorkoutDistance(0, 0), 0);
    }

    @Test
    public void testAverageSpeedZeroDistance() {
        assertEquals(0, StatisticsUtilities.calculateAverageSpeed(0, 342.2), 0);
    }

    @Test
    public void testAverageSpeedPositiveDistance() {
        assertEquals((12 / 6.23), StatisticsUtilities.calculateAverageSpeed(12, 6.23), 0);
    }

    @Test
    public void testAverageSpeedZeroTime() {
        assertEquals(-1, StatisticsUtilities.calculateAverageSpeed(12, 0), 0);
    }

    @Test
    public void testCalculateStepsLeftZeroGoal() {
        assertEquals(0, StatisticsUtilities.calculateStepsToGo(1234, 0));
    }

    @Test
    public void testCalculateStepsGoalReachedBorder() {
        assertEquals(0, StatisticsUtilities.calculateStepsToGo(1234, 1234));
    }

    @Test
    public void testCalculateStepsGoalReachedNotBorder() {
        assertEquals(0, StatisticsUtilities.calculateStepsToGo(1244, 1234));
    }

    @Test
    public void testCalculateStepsGoalNotReached() {
        assertEquals(5000 - 1244, StatisticsUtilities.calculateStepsToGo(1244, 5000));
    }
}
