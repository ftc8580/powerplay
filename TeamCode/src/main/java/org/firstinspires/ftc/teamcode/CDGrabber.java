package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class CDGrabber {
    private final CDRuntime runtime = new CDRuntime();
    private final Servo extendServo;
    private final Servo grabServo;

    public CDGrabber(CDHardware theHardware) {
        extendServo = theHardware.grabberExtendServo;
        grabServo = theHardware.grabberServo;

        //extendServo.scaleRange(.22, .586);
        grabServo.scaleRange(.35, .75);
    }

    public double getExtendPosition() {
        return extendServo.getPosition();
    }

    public double getGrabPosition() {
        return grabServo.getPosition();
    }

    public boolean setExtendPosition(double extendPositionTarget) {
        runtime.reset();
        //TODO redefine timeout if this is too long - needs testing
        final double TIMEOUT_SECONDS = 4.0;
        final double POSITION_THRESHOLD = .005; // base on readings from pickupServo
        while (!runtime.isTimedOut(TIMEOUT_SECONDS)) {
            if (isWithinThreshold(extendServo, extendPositionTarget, POSITION_THRESHOLD)) {
                extendServo.setPosition(extendPositionTarget);
                return true;
            }
        }
        return false;
    }

    public boolean setGrabPosition(double grabPositionTarget) {
        runtime.reset();
        //TODO redefine timeout if this is too long - needs testing
        final double TIMEOUT_SECONDS = 4.0;
        final double POSITION_THRESHOLD = .005; // base on readings from pickupServo
        while (!runtime.isTimedOut(TIMEOUT_SECONDS)) {
            if (isWithinThreshold(grabServo, grabPositionTarget, POSITION_THRESHOLD)) {
                grabServo.setPosition(grabPositionTarget);
                return true;
            }
        }
        return false;
    }



    public boolean isWithinThreshold(Servo activeServo, double servoPositionTarget, double positionThreshold) {
        double currentThresholdDifference = Math.abs(activeServo.getPosition() - servoPositionTarget);
        return currentThresholdDifference <= positionThreshold;
    }
}
