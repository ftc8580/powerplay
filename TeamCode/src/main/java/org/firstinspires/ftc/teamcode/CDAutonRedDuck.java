package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="CDAutonRedDuck", group="Linear Opmode")
//@Disabled
public class CDAutonRedDuck extends CDAutonBase {
    @Override
    public void initTokenWeDoNotSee() {
        duckWeDoNotSee = 3;
    }

    @Override
    public void executeAuton() {
        //Drive to duck spinner
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -3, 5);
        myTurret.setTurretDirection("center", true);
        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 10);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -19, 8);
        // Spin Ducks
        sleep(500);
        myDuckSpinner.setDuckSpinnerPower(-.5);
        sleep(3500);
        myDuckSpinner.setDuckSpinnerPower(0);
        // Drive to approach hub
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 32, 10.0);
        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10);
        // Slam wall
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -13, 15.0);
        // Prepare to deliver
        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));
        // Approach
        myTurret.setTurretDirection("center", true);
        myTurret.setTurretDirection("center", true);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 34, 10.0);
        // Deliver
        myIntake.setIntakePower(.4);
        sleep(1000);
        myIntake.setIntakePower(0);
        // Return
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -30, 10.0);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 11, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -6, 10.0);
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
