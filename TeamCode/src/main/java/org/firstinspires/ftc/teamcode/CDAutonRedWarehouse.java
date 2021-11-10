package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="CDAutonRedWarehouse", group="Linear Opmode")
//@Disabled
public class CDAutonRedWarehouse extends CDAutonBase {
    @Override
    public void executeAuton() {
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 3, 5);
        sleep(250);
        myTurret.setTurretDirection("right", false);
        sleep(250);
        myTurret.setTurretDirection("center", false);

        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 37, 15);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,5,10.0);
        myIntake.setIntakePower(.4);
        sleep(1000);
        myIntake.setIntakePower(0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,-10,10.0);
        myElevator.setElevatorPosition(7);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,-45,10.0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,-28,10.0);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,24,10.0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,-24,10.0);
        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 10);

    }
}


//Sample Code
//Drive
//myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 30, 10.0); //Move forward 30 inches with 5 second timeout. Negative is backward.
//myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 10, 5.0); //Move right 10 inches with 5 second timeout. Negative is left.
//myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -180, 5.0); //Turn left 180 degrees with a 5 second timeout. Positive is right.

//Elevator
//myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator)); // This will set to position based on cap location. Copy as is.
//myElevator.setElevatorPosition(26); //Move elevator to middle position. 7 is ground. 14 is bottom. 26 is middle. 39 is top. Do not set outside of range 2.5-39.

//Turret
//myTurret.setTurretDirection("center"); //Set turret position to center, right or left

//Intake and Duck Spinner
//myIntake.setIntakePower(1.0);
//myDuckSpinner.setDuckSpinnerPower(.7);
//sleep(250); //optional pause after each move in milliseconds
//Remember to set power back to zero