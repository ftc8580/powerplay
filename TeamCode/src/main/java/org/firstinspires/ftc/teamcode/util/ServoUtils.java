package org.firstinspires.ftc.teamcode.util;

public class ServoUtils {
    private static final double MS_PER_DEGREE = 3.67;
    private static final double DEFAULT_DEGREE_RANGE = 270;
    private static final double DEFAULT_SCALE_RANGE_MIN = 0;
    private static final double DEFAULT_SCALE_RANGE_MAX = 1;
    private static final double THRESHOLD_MULTIPLIER = 6;

    public static double getSweepTimeMs(double currentPosition, double targetPosition) {
        return getSweepTimeMs(currentPosition, targetPosition, DEFAULT_SCALE_RANGE_MIN, DEFAULT_SCALE_RANGE_MAX, DEFAULT_DEGREE_RANGE);
    }

    public static double getSweepTimeMs(double currentPosition, double targetPosition, double scaleRangeMin, double scaleRangeMax) {
        return getSweepTimeMs(currentPosition, targetPosition, scaleRangeMin, scaleRangeMax, DEFAULT_DEGREE_RANGE);
    }

    public static double getSweepTimeMs(double currentPosition, double targetPosition, double scaleRangeMin, double scaleRangeMax, double degreeRange) {
        double positionDelta = Math.abs(currentPosition - targetPosition);
        double scaledDelta = positionDelta * (scaleRangeMax - scaleRangeMin);
        double scaledDegrees = scaledDelta * degreeRange;
        return scaledDegrees * MS_PER_DEGREE * THRESHOLD_MULTIPLIER;
    }
}
