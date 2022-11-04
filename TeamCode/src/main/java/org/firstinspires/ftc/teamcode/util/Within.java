package org.firstinspires.ftc.teamcode.util;

public class Within {
    public static boolean within(double low, double high, double number) {
        return (number >= low && number <= high);
    }
}
