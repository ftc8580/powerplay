package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.util.RoundD;

public class CDPickup {
    private final Servo pickupServo;

    public CDPickup(CDHardware theHardware) {
        double SCALE_RANGE_MIN = 0.283;
        double SCALE_RANGE_MAX = 0.80;

        pickupServo = theHardware.pickupServo;
//        pickupServo.scaleRange(SCALE_RANGE_MIN, SCALE_RANGE_MAX);
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
