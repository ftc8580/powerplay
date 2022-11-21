package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

@Autonomous(name="CDAutonStackLEFT", group="Linear Opmode")
//@Disabled
public class CDAutonStackLEFT extends CDAutonBase {

    //    @Override
    //    public void initTokenWeDoNotSee() {
    //        SignalWeDoNotSee = 1;
    //    }

    @Override
    public void executeAuton() {
        //double signalLocation = Integer.parseInt(positionNumber); //Values should be 1,2 or 3
        int signalLocation;

        if (positionNumber == null || positionNumber == "") {
            signalLocation = 1;
        } else {
            signalLocation = Integer.parseInt(positionNumber); //Values should be 1,2 or 3
        }

        //This auton delivers cone to medium junction
        myFourbar.resetFourBarHomePosition();
        sleep (100);
        //Pick up cone
        myArm.setArmVerticalPosition(armVertPickupLOW);
        sleep (200);
        myFourbar.resetFourBarHomePosition();
        sleep (200);
        myPickup.pickup();
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, 10.0);
        myFourbar.resetFourBarHomePosition();
        sleep (250);
        myArm.setArmVerticalPosition(armVertHOME);

        //Drive forward to deliver to medium junction
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 37, 10.0);

        //Raise fourbar to medium delivery height and rotate arm to delivery position
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        sleep(750);
        myArm.setArmRotationPosition(alleyDeliverArmRotRIGHT);
        sleep(1000);
        //Drop Cone
        myPickup.release();
        sleep(1000);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);

        //Drive forward to center of square
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 17, 10.0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -5, 10.0);
        myFourbar.resetFourBarHomePosition();

        //Deliver Cones from Stack
        //Pickup Stack Cones
        myChassis.encoderDriveTurn((CDDriveChassisAuton.TURN_SPEED + 0.1), 90, 5.0);
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.1), -29, 10.0);
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        myFourbar.resetFourBarHomePosition();
        sleep(500);
        myArm.setArmVerticalPosition(armStackPickup4); //above cone positon + 0.150. This goes down.
        sleep(500);
        myFourbar.resetFourBarHomePosition();
        sleep (200);
        myPickup.pickup();
        sleep(500);
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        sleep(500);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, (distanceStacktoLow), 10.0);
        myArm.setArmVerticalPosition(armVertHOME);

        //Raise fourbar to medium delivery height and rotate arm to delivery position then move to low
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        sleep(1000);
        myArm.setArmRotationPosition(alleyDeliverArmRotRIGHT);
        sleep(501);
        myFourbar.setFourBarPosition(alleyDeliverFourbarLOW);
        sleep(1000);
        //Drop Cone
        myPickup.release();
        sleep(1000);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);
        //sleep(1000);
        //myFourbar.resetFourBarHomePosition();;


/*
        //REPEAT ABOVE FOR SECOND CONE
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -distanceStacktoLow, 10.0);
        myArm.setArmVerticalPosition(armStackPickup1); //above cone positon + 0.150. This goes down.
        sleep(500);
        myFourbar.resetFourBarHomePosition();
        sleep (200);
        myPickup.pickup();
        sleep(500);
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        sleep(500);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, (distanceStacktoLow/2), 10.0);
        myArm.setArmVerticalPosition(armVertHOME);
        myFourbar.resetFourBarHomePosition();
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, (distanceStacktoLow/2), 10.0);

        //Raise fourbar to medium delivery height and rotate arm to delivery position then move to low
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        sleep(1000);
        myArm.setArmRotationPosition(alleyDeliverArmRotRIGHT);
        sleep(501);
        myFourbar.setFourBarPosition(alleyDeliverFourbarLOW);
        sleep(1000);
        //Drop Cone
        myPickup.release();
        sleep(1000);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);
        sleep(1000);
        myFourbar.resetFourBarHomePosition();
*/


        //Drive to correct square
        if (signalLocation == 1) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), -8, 5.0);
            myFourbar.resetFourBarHomePosition();
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 5.0);
        }
        if (signalLocation ==2) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 15, 5.0);
            myFourbar.resetFourBarHomePosition();
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 5.0);
        }
        if (signalLocation ==3) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStrafe((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 38, 5.0);
            myFourbar.resetFourBarHomePosition();
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 5.0);
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

