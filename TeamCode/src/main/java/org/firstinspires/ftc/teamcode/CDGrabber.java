package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class CDGrabber {
    private final Servo extendServo;
    private final Servo grabServo;

    public CDGrabber(CDHardware theHardware) {
        extendServo = theHardware.grabberExtendServo;
        grabServo = theHardware.grabberServo;

        extendServo.scaleRange(0.127, 0.57);
        extendServo.setPosition(0.0);
        grabServo.scaleRange(.35, .75);
        grabServo.setPosition(0.0);
    }

    public double getExtendPosition() {
        return extendServo.getPosition();
    }

    public double getGrabPosition() {
        return grabServo.getPosition();
    }

    public void setExtendPosition(double extendPositionTarget) {
        extendServo.setPosition(extendPositionTarget);
    }

    public void setGrabPosition(double grabPositionTarget) {
        grabServo.setPosition(grabPositionTarget);
    }
}
