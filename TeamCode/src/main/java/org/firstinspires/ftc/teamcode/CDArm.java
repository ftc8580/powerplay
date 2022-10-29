package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class CDArm {

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

    private final ElapsedTime elapsedRunTime = new ElapsedTime();

    CDHardware robotHardware;
    //Arm up down using servo

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

    public CDArm(CDHardware theHardware){

        robotHardware = theHardware;
        //robotHardware.armupdownservo.scaleRange();
        //robotHardware.armrotservo.scaleRange();
        robotHardware.intakeservo.scaleRange(.283, .80);
        robotHardware.extendservo.scaleRange(.22, .586);
        robotHardware.grabservo.scaleRange(.35, .75);

    }
    //public double getArmThreshold () { return armCurrentThreshold; }
    //public double getArmRotationThreshold() {return armRotationCurrentThreshold; }

/*    public void setArmPower(double pow) {
        robotHardware.armmotor.setPower(pow);
    }*/

    public double getArmUpDownPosition() { return robotHardware.armupdownservo.getPosition(); } //Position from arm updown servo
    public double getArmRotPosition() {return robotHardware.armrotservo.getPosition(); }  //Position from arm rotation servo
    public double getIntakePosition() {return robotHardware.intakeservo.getPosition(); }  //position of Intake servo
    public double getExtendPosition() {return robotHardware.extendservo.getPosition(); }  //position of Extend servo
    public double getGrabPosition() {return robotHardware.grabservo.getPosition(); }  //position of Grab servo



    public boolean setArmUpDownPosition(double armUpDownPositionTarget) {
        robotHardware.armupdownservo.setPosition(armUpDownPositionTarget);
        return (robotHardware.armupdownservo.getPosition() == armUpDownPositionTarget);
    }
    public boolean setArmRotPosition(double armRotationPositionTarget) {
        robotHardware.armrotservo.setPosition(armRotationPositionTarget);
        return (robotHardware.armrotservo.getPosition() == armRotationPositionTarget);
    }
    public boolean setIntakePosition(double intakePositionTarget) {
        robotHardware.intakeservo.setPosition(intakePositionTarget);
        return (robotHardware.intakeservo.getPosition() == intakePositionTarget);
    }
    public boolean setExtendPosition(double extendPositionTarget) {
        robotHardware.extendservo.setPosition(extendPositionTarget);
        return (robotHardware.extendservo.getPosition() == extendPositionTarget);
    }
    public boolean setGrabPosition(double grabPositionTarget) {
        robotHardware.grabservo.setPosition(grabPositionTarget);
        return (robotHardware.armrotservo.getPosition() == grabPositionTarget);
    }

//TODO DELETE BELOW - Leaving to use timeout reference in case needed
 /*   public boolean setArmPosition(double armPositionTarget) {
        elapsedRunTime.reset();
        double armPositionTimeoutSec = 4.0;
        final double THRESHOLD_POSITION = 1.0; // base on encoder readings from armmotor ArmPosiiton
        double armSpeedMultiple = 1.0; // to slow down the arm if needed
        armStopped = false; // initially we want the arm to move for the while loop
        armError = false;
        //while ((runtime.seconds() < armPositionTimeoutSec) && !armstop && !magneticstop && !armerror) {
        while ((elapsedRunTime.seconds() < armPositionTimeoutSec) && !armStopped && !armError) {
            // Simple check to see if the magnetic switch is contacted
            // if (upelevatormagnetswitch.isPressed()) {
            //   magneticstop = true;
            //}
//            if (armlastpos == armposcurrent) {
//                elevatorerror = true;
//                return true; // There was an error, the value didn't change.
//            }
            armLastPosition = getArmPosition(); // updates every loop to say where we are in the beginning.
            armCurrentThreshold = Math.abs(armLastPosition - armPositionTarget);
            //TODO update values below to reflect where near end ROM
            if (armLastPosition < 10 || armLastPosition > 35) {
                armSpeedMultiple = .05;
            } else {
                armSpeedMultiple = 0.10;
            }
            if (armCurrentThreshold <= THRESHOLD_POSITION)  {
                setArmPower(0); // need to stop the arm before leaving the loop
                armStopped = true; // leave the while loop
            //TODO check if -1 and 1 multipliers below move arm in correct direction - if not then reverse both
            } else if (armLastPosition > armPositionTarget) {
                setArmPower(-1 * armSpeedMultiple);
            } else if (armLastPosition < armPositionTarget) {
                setArmPower(1 * armSpeedMultiple);
            }
            armPositionCurrent = robotHardware.armmotor.getCurrentPosition(); // updates every loop to see where we ended up.
        }
        return false; // Returns false if the arm succeeded in moving to requested position, no error.
    }*/


}
