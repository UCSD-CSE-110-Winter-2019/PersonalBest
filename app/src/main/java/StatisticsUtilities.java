public class StatisticsUtilities {

    public static double calculateWorkoutDistance(int steps, double strideLength) {
        return steps * strideLength;
    }

    public static double calculateAverageSpeed(double distance, double hoursElapsed) {
        return distance / hoursElapsed;
    }

    public static int calculateStepsToGo(int stepGoal, int currStepCount) {
        return stepGoal - currStepCount;
    }
}