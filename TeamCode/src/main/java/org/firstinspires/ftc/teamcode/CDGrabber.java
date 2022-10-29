package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class CDGrabber {
    private final Servo extendServo;
    private final Servo grabServo;

    public CDGrabber(CDHardware theHardware) {
        extendServo = theHardware.grabberExtendServo;
        grabServo = theHardware.grabberServo;

        extendServo.scaleRange(.22, .586);
        grabServo.scaleRange(.35, .75);
    }

    public double getExtendPosition() {
        return extendServo.getPosition();
    }

    public double getGrabPosition() {
        return grabServo.getPosition();
    }

    public boolean setExtendPosition(double extendPositionTarget) {
        extendServo.setPosition(extendPositionTarget);
        return extendServo.getPosition() == extendPositionTarget;
    }

    public boolean setGrabPosition(double grabPositionTarget) {
        grabServo.setPosition(grabPositionTarget);
        return grabServo.getPosition() == grabPositionTarget;
    }
}
