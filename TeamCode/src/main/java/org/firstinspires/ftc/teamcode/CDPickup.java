package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class CDPickup {
    private final CDRuntime runtime = new CDRuntime();
    private final Servo pickupServo;

    public CDPickup(CDHardware theHardware) {
        double SCALE_RANGE_MIN = 0.283;
        double SCALE_RANGE_MAX = 0.80;

        pickupServo = theHardware.pickupServo;
        pickupServo.scaleRange(SCALE_RANGE_MIN, SCALE_RANGE_MAX);
    }

    public double getServoPosition() {
        return pickupServo.getPosition();
    }

    public boolean setServoPosition(double servoPositionTarget) {
        runtime.reset();
        //TODO redefine timeout if this is too long - needs testing
        final double TIMEOUT_SECONDS = 4.0;
        //TODO determine if threshold_pos is needed when working with servo
        final double POSITION_THRESHOLD = .005; // base on readings from pickupServo
        while (!runtime.isTimedOut(TIMEOUT_SECONDS)) {
            if (isWithinThreshold(servoPositionTarget, POSITION_THRESHOLD)) {
                pickupServo.setPosition(servoPositionTarget);
                return true; // Returns true if the arm succeeded in moving to requested position.
            }
        }
        return false; // Returns false if the arm failed in moving to requested position.
    }

    public boolean isWithinThreshold(double servoPositionTarget, double positionThreshold) {
        double currentThresholdDifference = Math.abs(getServoPosition() - servoPositionTarget);
        return currentThresholdDifference <= positionThreshold;
    }
}
