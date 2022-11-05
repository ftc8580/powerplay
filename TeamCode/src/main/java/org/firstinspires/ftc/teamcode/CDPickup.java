package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.util.RoundD;

public class CDPickup {
    private final Servo pickupServo;

    public CDPickup(CDHardware theHardware) {
        double SCALE_RANGE_MIN = 0.50;
        double SCALE_RANGE_MAX = 0.70;

        pickupServo = theHardware.pickupServo;
        pickupServo.setPosition(0.0);
        pickupServo.scaleRange(SCALE_RANGE_MIN, SCALE_RANGE_MAX);
    }

    public double getServoPosition() {
        return RoundD.roundD(pickupServo.getPosition());
    }

    public synchronized void pickup() {

    }

    public synchronized void release() {

    }
    public synchronized void setServoPosition(double servoPositionTarget) {
        pickupServo.setPosition(servoPositionTarget);
    }
}
