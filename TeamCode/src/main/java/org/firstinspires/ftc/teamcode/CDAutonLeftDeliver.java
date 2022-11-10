package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

@Autonomous(name="CDAutonLeftDeliver", group="Linear Opmode")
//@Disabled
public class CDAutonLeftDeliver extends CDAutonBase {

    //    @Override
    //    public void initTokenWeDoNotSee() {
    //        SignalWeDoNotSee = 1;
    //    }


    @Override
    public void executeAuton() {
        double signalLocation = Integer.parseInt(positionNumber); //Values should be 1,2 or 3

        //This auton delivers cone to medium junction
        myFourbar.setFourBarPosition(CDFourBar.LOWER_POSITION_HOME);
        sleep (100);
        //Pick up cone
        myArm.setArmVerticalPosition(CDArm.ARM_VERTICAL_PICKUP_LOW_POSITION);
        sleep (200);
        myPickup.pickup();
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, 10.0);
        myArm.setArmVerticalPosition(CDArm.ARM_VERTICAL_PICKUP_HIGH_POSITION);

        //Drive forward to deliver to medium junction
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 37, 10.0);

        //Raise fourbar to medium delivery height and rotate arm to delivery position
        myFourbar.setFourBarPosition(Alley_Delivery_Fourbar_Medium);
        sleep(1000);
        myArm.setArmRotationPosition(CDArm.ALLEY_DELIVERY_RIGHT_ROTATION);
        sleep(1000);
        //Drop Cone
        myPickup.release();
        sleep(1000);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(CDArm.ARM_ROTATION_POSITION_HOME);

        //Drive forward to center of square
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 12, 10.0);

        //Drive to correct square
        if (signalLocation == 1) {
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -23, 5.0);
            //Only if near cone stack. Try to pick up cone. NOT SURE IF THIS IS EVEN POSSIBLE.
/*            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 5.0);
            myArm.setArmVerticalPosition(0.200);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -4, 10.0);
            myArm.setArmVerticalPosition(0.350); //above cone positon + 0.150. This goes down.
            myPickup.pickup();
            myArm.setArmVerticalPosition(0.1);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 4, 10.0);
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 5.0);
            myArm.setArmVerticalPosition(CDArm.ARM_VERTICAL_PICKUP_HIGH_POSITION);*/
        }
        if (signalLocation ==3) {
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
    //Use CDArm.ARM_ROTATION_POSITION_HOME to set arm rotation to back

//Pickup
    //myPickup.pickup();
    //myPickup.release();
