package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="CDAutonBlueDuck", group="Linear Opmode")
//@Disabled
public class CDAutonBlueDuck extends CDAutonBase {
    @Override
    public void initTokenWeDoNotSee() {
        duckWeDoNotSee = 1;
    }

    @Override
    public void executeAuton() {
        // Setup location
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 2, 5);
        sleep(500);
        myTurret.setTurretDirection("center", true);
        myTurret.setTurretDirection("center", false);

        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -2, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -17, 8);
        // Spin Ducks
        myDuckSpinner.setDuckSpinnerPower(.5);
        sleep(3000);
        myDuckSpinner.setDuckSpinnerPower(0);
        // Drive to approach hub
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 35, 10.0);
        // Prepare to deliver
        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 25, 10.0);
        // Deliver
        myIntake.setIntakePower(.4);
        sleep(1000);
        myIntake.setIntakePower(0);
        // Return
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -25, 10.0);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -11, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -12, 10.0);
        myElevator.setElevatorPosition(7);
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