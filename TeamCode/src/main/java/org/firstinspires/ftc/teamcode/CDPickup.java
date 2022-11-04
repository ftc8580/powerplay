package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.util.ServoUtils;

public class CDPickup extends SubsystemBase {
    private static final double SCALE_RANGE_MIN = 0.283;
    private static final double SCALE_RANGE_MAX = 0.80;

    private final Servo pickupServo;

    public CDPickup(CDHardware theHardware) {
        pickupServo = theHardware.pickupServo;
        pickupServo.scaleRange(SCALE_RANGE_MIN, SCALE_RANGE_MAX);
    }

    public double getSweepTimeMs(double servoPositionTarget) {
        return ServoUtils.getSweepTimeMs(
                getServoPosition(),
                servoPositionTarget,
                SCALE_RANGE_MIN,
                SCALE_RANGE_MAX
        );
    }

    public double getServoPosition() {
        return pickupServo.getPosition();
    }

    public synchronized void setServoPosition(double servoPositionTarget) {
        pickupServo.setPosition(servoPositionTarget);
    }
}
