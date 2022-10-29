package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class CDArmIntake {
    public double SCALE_RANGE_MIN = 0.283;
    public double SCALE_RANGE_MAX = 0.80;

    private final CDRuntime runtime = new CDRuntime();

    private final Servo intakeServo;

    public CDArmIntake(CDHardware theHardware) {
        intakeServo = theHardware.armIntakeServo;
        intakeServo.scaleRange(SCALE_RANGE_MIN, SCALE_RANGE_MAX);
    }

    public double getServoPosition() {
        return intakeServo.getPosition();
    }

    public boolean setPickupPosition(double servoPositionTarget) {
        runtime.reset();
        //TODO redefine timeout if this is too long - needs testing
        final double TIMEOUT_SECONDS = 4.0;
        //TODO determine if threshold_pos is needed when working with servo
        final double POSITION_THRESHOLD = .005; // base on readings from intakeServo
        while (!runtime.isTimedOut(TIMEOUT_SECONDS)) {
            if (isWithinThreshold(servoPositionTarget, POSITION_THRESHOLD)) {
                intakeServo.setPosition(servoPositionTarget);
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
