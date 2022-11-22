package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name="CDAutonBasicLEFT", group="Linear Opmode")
//@Disabled
public class CDAutonBasicLEFT extends CDAutonBase {

    @Override
    public void executeAuton() {

        int signalLocation;

        if (positionNumber == null || positionNumber == "") {
            signalLocation = 1;
        } else {
            signalLocation = Integer.parseInt(positionNumber); //Values should be 1,2 or 3
        }

        //This auton delivers cone to medium junction
        myFourbar.setFourBarPosition(fourbarHOME);
        sleep (100);
        //Pick up cone
        myArm.setArmVerticalPosition(0.8);
        sleep (200);
        myFourbar.setFourBarPosition(fourbarHOME);
        sleep (200);
        myPickup.pickup();
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, 10.0);
        myFourbar.setFourBarPosition(fourbarHOME);
        sleep (250);
        myArm.setArmVerticalPosition(armVertHOME);

        //Drive forward to deliver to medium junction
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 37, 10.0);

        //Raise fourbar to medium delivery height and rotate arm to delivery position
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        sleep(750);
        myArm.setArmRotationPosition(alleyDeliverArmRotRIGHT);
        sleep(1000);
        //Drop Cone
        //myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM - 0.02);
        myPickup.release();
        sleep(1000);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);

        //Drive forward to center of square
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 20, 10.0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -8, 10.0);
        myFourbar.setFourBarPosition(fourbarHOME);


        //Drive to correct square
        if (signalLocation == 1) {
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -23, 5.0);
        }
        if (signalLocation ==3) {
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 23, 5.0);
        }

        finishAuton();
    }

}

//Sample Code
//Drive
//myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 30, 10.0); //Move forward 30 inches with 5 second timeout. Negative is backward.
//myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 10, 5.0); //Move right 10 inches with 5 second timeout. Negative is left.
//myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -180, 5.0); //Turn left 180 degrees with a 5 second timeout. Positive is right.

//Fourbar Height
//myFourbar.resetFourBarHomePosition(); //This takes fourbar down to HOME position
//myFourbar.setFourBarPosition("low"); //Set to deliver on "low", "medium" or "high" junction)
//myFourbar.setFourBarPosition("alleyDeliverFourbarMEDIUM"); //See variables in CDAuton Base or enter value between .23 and 1.12

//Arm Vertical
//myArm.setArmVerticalPosition(CDArm.ARM_VERTICAL_POSITION_HOME) //See variables in CDAuton Base
//Use armVertHOME or CDArm.ARM_VERTICAL_POSITION_HOME for Fourbar Heights of low and medium
//Use 0.31 for Fourbar Height high
//For picking up cones... Use armVertHOME or armVertPickupLow //use CDArm.ARM_VERTICAL_PICKUP_LOW_POSITION or CDArm.ARM_VERTICAL_PICKUP_HIGH_POSITION

//Arm Rotation
//myArm.setArmRotationPosition(alleyDeliverArmRotLEFT);
//See variables in CDAuton Base
//Use alleyDeliverArmRotLEFT or CDArm.ALLEY_DELIVERY_LEFT_ROTATION for left deliver
//Use alleyDeliverArmRotRIGHT or CDArm.ALLEY_DELIVERY_RIGHT_ROTATION for right deliver
//Use artRotHOME or CDArm.ARM_ROTATION_POSITION_HOME to set arm rotation to back

//Pickup
//myPickup.pickup();
//myPickup.release();
