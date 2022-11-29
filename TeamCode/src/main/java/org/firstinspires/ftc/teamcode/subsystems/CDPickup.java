package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CDHardware;
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.util.ServoUtils;

public class CDPickup extends SubsystemBase {
    private static final double SCALE_RANGE_MIN = 0.35;
    private static final double SCALE_RANGE_MAX = 0.71;

    public static final double CLOSED_POSITION = 1.0; // 13mm
    public static final double OPEN_POSITION = 0.0;

    private final Servo pickupServo;

    public boolean isPickupClosed;

    public CDPickup(CDHardware theHardware) {
        isPickupClosed = false;
        pickupServo = theHardware.pickupServo;
        pickupServo.scaleRange(SCALE_RANGE_MIN, SCALE_RANGE_MAX);
        setServoPosition(OPEN_POSITION);
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
        return MathUtils.roundDouble(pickupServo.getPosition());
    }

    public void pickup() {
        setServoPosition(CLOSED_POSITION);
        isPickupClosed = true;
    }

    public void release() {
        setServoPosition(OPEN_POSITION);
        isPickupClosed = false;
    }

    private void setServoPosition(double servoPositionTarget) {
        pickupServo.setPosition(servoPositionTarget);
    }
}
