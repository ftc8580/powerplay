package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;

@Autonomous(name="CDAutonFASTStackRIGHT", group="Linear Opmode")
//@Disabled
public class CDAutonFASTStackRIGHT extends CDAutonBase {

    @Override
    public void executeAuton() {

        int signalLocation;

        if (positionNumber == null || positionNumber == "") {
            signalLocation = 3;
        } else {
            signalLocation = Integer.parseInt(positionNumber); //Values should be 1,2 or 3
        }

        //This auton delivers cone to medium junction
        //myArm.setArmRotationPosition(armRotHOME);
        myFourbar.resetFourBarHomePosition();
        sleep (50);
        //Pick up cone
        myArm.setArmVerticalPosition(armVertPickupLOW);
        sleep (150);
        //myFourbar.resetFourBarHomePosition();
        //sleep (100);
        myPickup.pickup();
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, 10.0);
        myFourbar.resetFourBarHomePosition();
        //sleep (100);
        myArm.setArmVerticalPosition(armVertHOME);

        //Drive forward to deliver to medium junction
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.25), 37, 10.0);

        //Raise fourbar to medium delivery height and rotate arm to delivery position
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        sleep(250);
        myArm.setArmRotationPosition(alleyDeliverArmRotLEFT);
        sleep(500);
        //Drop Cone
        myPickup.release();
        sleep(300);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);
        sleep(200);
        myFourbar.setFourBarPosition(alleyDeliverFourbarLOW);

        //Drive forward to center of square
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED), 20.1, 10.0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -6.01, 10.0);
        myFourbar.resetFourBarHomePosition();

        //Deliver Cones from Stack
        //Pickup Stack Cones
        myChassis.encoderDriveTurn((CDDriveChassisAuton.TURN_SPEED + 0.6), -85, 5.0);
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.1), -28, 10.0);
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED - 0.15), -1.9, 10.0);
        myFourbar.resetFourBarHomePosition();
        //myArm.setArmRotationPosition(0.341);
        myArm.setArmVerticalPosition(armStackPickup5); //above cone position + 0.150. This goes down.
        sleep(250);
        myFourbar.resetFourBarHomePosition();
        //sleep (150);
        myPickup.pickup();
        sleep(300);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 1.51, 2);
        //sleep(100);
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        sleep(300);
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.05), (distanceStacktoLow-1.5), 10.0);
        myArm.setArmVerticalPosition(armVertHOME);

        //Raise fourbar to medium delivery height and rotate arm to delivery position then move to low
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        sleep(350);
        myArm.setArmRotationPosition(alleyDeliverArmRotLEFT);
        sleep(350);
        myFourbar.setFourBarPosition(alleyDeliverFourbarLOW);
        sleep(350);
        //Drop Cone
        myPickup.release();
        sleep(200);
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);
        sleep(300);
        myFourbar.resetFourBarHomePosition();


        //REPEAT ABOVE FOR SECOND CONE
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.05), -distanceStacktoLow, 10.0);
        myFourbar.resetFourBarHomePosition();
        sleep(100);
        //myArm.setArmRotationPosition(0.341);
        myArm.setArmVerticalPosition(armStackPickup4); //above cone position + 0.150. This goes down.
        sleep(250);
        myFourbar.resetFourBarHomePosition();
        //sleep (150);
        myPickup.pickup();
        sleep(250);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 1.51, 2);
        //sleep(100);
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        sleep(300);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, (distanceStacktoLow-1.5), 10.0);
        myArm.setArmVerticalPosition(armVertHOME);

        //Raise fourbar to medium delivery height and rotate arm to delivery position then move to low
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        sleep(350);
        myArm.setArmRotationPosition(alleyDeliverArmRotLEFT);
        sleep(350);
        myFourbar.setFourBarPosition(alleyDeliverFourbarLOW);
        sleep(350);
        //Drop Cone
        myPickup.release();
        sleep(200);
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);


        //DRIVE TO PARK in correct square
        if (signalLocation == 3) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), -8, 5.0);
            myArm.setArmRotationPosition(armRotHOME);
            //myFourbar.resetFourBarHomePosition();
            myFourbar.setFourBarPosition(alleyDeliverFourbarHIGH);
           // myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 85, 5.0);
        }
        if (signalLocation ==2) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 15, 5.0);
            myArm.setArmRotationPosition(armRotHOME);
            //myFourbar.resetFourBarHomePosition();
            myFourbar.setFourBarPosition(alleyDeliverFourbarHIGH);
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 85, 5.0);
        }
        if (signalLocation ==1) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 38, 5.0);
            myArm.setArmRotationPosition(armRotHOME);
            //myFourbar.resetFourBarHomePosition();
            myFourbar.setFourBarPosition(alleyDeliverFourbarHIGH);
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 85, 5.0);
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

