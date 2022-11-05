package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CDHardware;
import org.firstinspires.ftc.teamcode.util.ServoUtils;

public class CDGrabber extends SubsystemBase {
    private static final double EXTEND_SCALE_RANGE_MIN = 0.2;
    private static final double EXTEND_SCALE_RANGE_MAX = 0.67;
    private static final double GRAB_SCALE_RANGE_MIN = 0.3;
    private static final double GRAB_SCALE_RANGE_MAX = 0.75;

    private final Servo extendServo;
    private final Servo grabServo;

    public CDGrabber(CDHardware theHardware) {
        extendServo = theHardware.grabberExtendServo;
        grabServo = theHardware.grabberServo;

        extendServo.scaleRange(EXTEND_SCALE_RANGE_MIN, EXTEND_SCALE_RANGE_MAX);
        grabServo.scaleRange(GRAB_SCALE_RANGE_MIN, GRAB_SCALE_RANGE_MAX);
        
        extendServo.setPosition(0.0);
        grabServo.setPosition(1.0);
    }

    public double getExtendPosition() {
        return extendServo.getPosition();
    }

    public double getGrabPosition() {
        return grabServo.getPosition();
    }

    public double getExtendTimeMs(double extendPositionTarget) {
        return ServoUtils.getSweepTimeMs(
                getExtendPosition(),
                extendPositionTarget,
                EXTEND_SCALE_RANGE_MIN,
                EXTEND_SCALE_RANGE_MAX
        );
    }

    public double getGrabTimeMs(double grabPositionTarget) {
        return ServoUtils.getSweepTimeMs(
                getGrabPosition(),
                grabPositionTarget,
                GRAB_SCALE_RANGE_MIN,
                GRAB_SCALE_RANGE_MAX
        );
    }

    public synchronized void setExtendPosition(double extendPositionTarget) {
        extendServo.setPosition(extendPositionTarget);
    }

    public synchronized void setGrabPosition(double grabPositionTarget) {
        grabServo.setPosition(grabPositionTarget);
    }
}
