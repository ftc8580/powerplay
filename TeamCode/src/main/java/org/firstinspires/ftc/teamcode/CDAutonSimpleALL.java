package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;


@Autonomous(name="CDAutonSimpleALL", group="Linear Opmode")
//@Disabled
public class CDAutonSimpleALL extends CDAutonBase {

    //    @Override
    //    public void initTokenWeDoNotSee() {
    //        SignalWeDoNotSee = 1;
    //    }
    private double signalLocation;

    @Override
    public void executeAuton() {
        if(positionNumber == "1") {
            signalLocation = 1;
        } else if (positionNumber == "2") {
            signalLocation = 2;
        } else if (positionNumber == "3") {
            signalLocation = 3;
        } else {
            signalLocation = 1;
        }

        //double signalLocation = Integer.parseInt(positionNumber); //Values should be 1,2 or 3
        //int signalLocation = (int)positionNumber;

        //This is a simple auton to pick up cone and park based on signal location

        //Ensure Fourbar all the way down
        myFourbar.setFourBarPosition(fourbarHOME);
        sleep (200);
        //Pick up cone
        myArm.setArmVerticalPosition(armVertPickupLOW);
        sleep (201);
        myFourbar.setFourBarPosition(fourbarHOME);
        sleep (200);
        myPickup.pickup();
        //sleep (500);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, 10.0);
        myFourbar.setFourBarPosition(fourbarHOME);
        sleep (200);
        myArm.setArmVerticalPosition(armVertHOME);

        //Drive forward 2 squares plus to center of square
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 49, 10.0);

        //Drive to correct square
        if (signalLocation == 1) {
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -23, 5.0);
        }
        if (signalLocation == 3) {
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 23, 5.0);
        }
    }

}

//Sample Code
//Drive
//myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 30, 10.0); //Move forward 30 inches with 5 second timeout. Negative is backward.
//myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 10, 5.0); //Move right 10 inches with 5 second timeout. Negative is left.
//myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -180, 5.0); //Turn left 180 degrees with a 5 second timeout. Positive is right.

//Fourbar Height
//myFourbar.setFourBarDirection("low"); //Set to deliver on "low", "medium" or "high" junction)

//Arm Vertical
//myArm.setArmVerticalPosition(CDArm.ARM_VERTICAL_POSITION_HOME)
    //Use CDArm.ARM_VERTICAL_POSITION_HOME for Fourbar Heights of low or medium
    //Use 0.31 for Fourbar Height high
    //For picking up cones... use CDArm.ARM_VERTICAL_PICKUP_LOW_POSITION or CDArm.ARM_VERTICAL_PICKUP_HIGH_POSITION

//Arm Rotation
//myArm.setArmRotationPosition(CDArm.ALLEY_DELIVERY_LEFT_ROTATION);
    //Use CDArm.ALLEY_DELIVERY_LEFT_ROTATION for left deliver
    //Use CDArm.ALLEY_DELIVERY_RIGHT_ROTATION for right deliver

//Pickup
    //myPickup.pickup();
    //myPickup.release();
