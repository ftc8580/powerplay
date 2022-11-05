package org.firstinspires.ftc.teamcode.util;

public class MathUtils {
    public static boolean isWithinRange(double lowerBound, double upperBound, double target) {
        return (target >= lowerBound && target <= upperBound);
    }

    public static double roundDouble(double input) {
        return Math.round(input * 1000.0) / 1000.0;
    }
}