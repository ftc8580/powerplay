package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;

@Autonomous(name="CDAutonStackRIGHT", group="Linear Opmode")
//@Disabled
public class CDAutonStackRIGHT extends CDAutonBase {

    @Override
    public void executeAuton() {

        int signalLocation;

        if (positionNumber == null || positionNumber == "") {
            signalLocation = 3;
        } else {
            signalLocation = Integer.parseInt(positionNumber); //Values should be 1,2 or 3
        }

        //This auton delivers cone to medium junction
        myArm.setArmRotationPosition(armRotHOME);
        myFourbar.resetFourBarHomePosition();
        sleep (100);
        //Pick up cone
        myArm.setArmVerticalPosition(armVertPickupLOW);
        sleep (150);
        myFourbar.resetFourBarHomePosition();
        sleep (150);
        myPickup.pickup();
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, 10.0);
        myFourbar.resetFourBarHomePosition();
        sleep (150);
        myArm.setArmVerticalPosition(armVertHOME);

        //Drive forward to deliver to medium junction
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 37, 10.0);

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

        //Drive forward to center of square
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED), 21.1, 10.0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -7.01, 10.0);
        myFourbar.resetFourBarHomePosition();

        //Deliver Cones from Stack
        //Pickup Stack Cones
        myChassis.encoderDriveTurn((CDDriveChassisAuton.TURN_SPEED + 0.6), -85, 5.0);
        myArm.setArmVerticalPosition(armStackPickupHIGH);
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.1), -28, 10.0);
        myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED - 0.15), -1.9, 10.0);
        myFourbar.resetFourBarHomePosition();
        sleep(350);
        myArm.setArmVerticalPosition(armStackPickup4); //above cone positon + 0.150. This goes down.
        sleep(250);
        myArm.setArmRotationPosition(0.341);
        sleep (150);
        myFourbar.resetFourBarHomePosition();
        sleep (150);
        myPickup.pickup();
        sleep(350);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 1.51, 2);
        sleep(350);
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
        sleep(350);
        myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        //Rotate arm back to HOME position to prevent collision
        myArm.setArmRotationPosition(armRotHOME);
        //sleep(1000);
        //myFourbar.resetFourBarHomePosition();


        //REPEAT ABOVE FOR SECOND CONE
        // NOTE: This is commented out because we were timing out before we could park during drive practice
        // myArm.setArmVerticalPosition(armStackPickupHIGH);
        // myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.1), -distanceStacktoLow, 10.0);
        // sleep(350);
        // myFourbar.resetFourBarHomePosition();a
        // sleep(350);
        // myArm.setArmVerticalPosition(armStackPickup3); //above cone positon + 0.150. This goes down.
        // sleep(350);
        // myArm.setArmRotationPosition(0.343);
        // sleep (250);
        // myFourbar.resetFourBarHomePosition();
        // sleep (150);
        // myPickup.pickup();
        // sleep(250);
        // myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 1.51, 2);
        // sleep(250);
        // myArm.setArmVerticalPosition(armStackPickupHIGH);
        // sleep(500);
        // myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, (distanceStacktoLow-1.5), 10.0);
        // myArm.setArmVerticalPosition(armVertHOME);

        //Raise fourbar to medium delivery height and rotate arm to delivery position then move to low
        // myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        // sleep(250);
        // myArm.setArmRotationPosition(alleyDeliverArmRotLEFT);
        // sleep(350);
        // myFourbar.setFourBarPosition(alleyDeliverFourbarLOW);
        // sleep(400);
        // //Drop Cone
        // myPickup.release();
        // sleep(250);
        // myFourbar.setFourBarPosition(alleyDeliverFourbarMEDIUM);
        // //Rotate arm back to HOME position to prevent collision
        // myArm.setArmRotationPosition(armRotHOME);


        //DRIVE TO PARK in correct square
        if (signalLocation == 3) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), -8, 5.0);
            myArm.setArmRotationPosition(armRotHOME);
            myFourbar.resetFourBarHomePosition();
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 5.0);
        }
        if (signalLocation ==2) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 15, 5.0);
            myArm.setArmRotationPosition(armRotHOME);
            myFourbar.resetFourBarHomePosition();
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 5.0);
        }
        if (signalLocation ==1) {
            //TODO adjust from last cone deliver
            myChassis.encoderDriveStraight((CDDriveChassisAuton.DRIVE_SPEED + 0.2), 38, 5.0);
            myArm.setArmRotationPosition(armRotHOME);
            myFourbar.resetFourBarHomePosition();
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 5.0);
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

