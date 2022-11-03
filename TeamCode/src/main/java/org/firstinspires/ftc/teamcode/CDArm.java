package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

public class CDArm extends SubsystemBase {
    // This is where we set the values for our distance sensor
    //TODO reset values below to values from armmotor encoder
/*    public double defaultArmPosition = 26.0;
    public double armPositionBottom = 4.0;
    public double armPositionLowJunction = 14.0;
    public double armPositionMediumJunction = 26.0;
    public double armPositionHighJunction = 26.0;
    public double armPositionStopUp = 39.5;
    public double armPositionStopDown = 0;
    //TODO reset values below to values from armservo
    public double armRotationBack = 0;
    public double armRotationRight = .33;
    public double armRotationFront = .66;
    public double armRotationLeft = 1;*/

    private final Servo verticalServo;
    private final Servo rotationServo;
    private final CDPickup pickup;

    /*public boolean armStopped;
    public boolean armError;
    public double armCurrentThreshold;
    public double armPositionCurrent;
    public double armLastPosition;*/

    //Arm rotation using servo
    /*public boolean armRotationStopped;
    public boolean armRotationError;
    //public double armRotationCurrentThreshold;
    public double armRotationPositionCurrent;
    public double armRotationLastPosition;*/
    public double armClearToRotatePositionWithCone = .565; //(.87* fourBarPositionCurrent - .14);
    //TODO define armClearToRotatePositionNoCone y=.92x+.01

    public CDArm(CDHardware theHardware){
        verticalServo = theHardware.armVerticalServo;
        rotationServo = theHardware.armRotationServo;
        pickup = new CDPickup(theHardware);

        verticalServo.scaleRange(.4, .8);
        rotationServo.scaleRange(.15, .37);

        verticalServo.setPosition(0.565);
        rotationServo.setPosition(0.338);
    }

    public double getArmVerticalPosition() {
        return verticalServo.getPosition();
    }

    public double getArmRotationPosition() {
        return rotationServo.getPosition();
    }

    public void setArmVerticalPosition(double armVerticalPositionTarget) {
        verticalServo.setPosition(armVerticalPositionTarget);
    }

    public void setArmRotationPosition(double armRotationPositionTarget) {
        rotationServo.setPosition(armRotationPositionTarget);
    }

    public void openPickup() {
        pickup.setServoPosition(1);
    }

    public void closePickup() {
        pickup.setServoPosition(0);
    }
}
