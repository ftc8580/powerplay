package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="CDAutonRedDuck", group="Linear Opmode")
//@Disabled
public class CDAutonRedDuck extends CDAutonBase {
    @Override
    public void executeAuton() {
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -3, 5);
        myTurret.setTurretDirection("center");
        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 10);
        //myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -1, 5);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -30, 8);
        sleep(200);
        myDuckSpinner.setDuckSpinnerPower(-.6);
        sleep(2500);
        myDuckSpinner.setDuckSpinnerPower(0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 32, 10.0);
        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));
        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -5, 10.0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 33, 10.0);
        myTurret.setTurretDirection("center");
        myIntake.setIntakePower(.4);
        sleep(1000);
        myIntake.setIntakePower(0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -29, 10.0);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 11, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -6, 10.0);
        myElevator.setElevatorPosition(7);
    }
}

