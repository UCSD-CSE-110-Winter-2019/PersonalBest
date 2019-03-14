package com.team2.team2_personalbest;

/**
 * This class is used to calculate statistics
 */
public class StatisticsUtilities {

    private static double MILE_CONVERSION = 5280;
    private static final double TO_GET_AVERAGE_STRIDE = 0.413;
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

    public  static double convertMillToHour(long milliSeconds){
        return milliSeconds / (1000 * 3600);
    }

    public static double calculateAveStrideLength(double height) {
        return height * TO_GET_AVERAGE_STRIDE;
    }

    public static double convertInchToMile(double inch) {
        return inch * 1.57828e-5;
    }

}