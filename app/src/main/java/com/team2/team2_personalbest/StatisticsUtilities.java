package com.team2.team2_personalbest;

public class StatisticsUtilities {

    private static double MILE_CONVERSION = 5280;
    /**
     *
     * @param steps
     * @param strideLength (in feet)
     * @return (miles)
     */
    public static double calculateWorkoutDistance(int steps, double strideLength) {
        return steps * ((1.0 / MILE_CONVERSION) * strideLength);
    }

    /**
     *
     * @param distance (miles)
     * @param hoursElapsed (hours)
     * @return (miles/hr), or return -1.0 if hoursElapsed == 0
     */
    public static double calculateAverageSpeed(double distance, double hoursElapsed) {
        if (hoursElapsed <= 0) {
            return -1;
        }
        return distance / hoursElapsed;
    }

    /**
     *
     * @param stepGoal
     * @param currStepCount
     * @return (steps left to take to reach goal), or 0 if goal has been reached
     */
    public static int calculateStepsToGo(int currStepCount, int stepGoal) {
        if (currStepCount >= stepGoal) {
            return 0;
        }
        return stepGoal - currStepCount;
    }
}