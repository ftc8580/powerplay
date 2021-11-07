package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="CDAutonBlueDuck", group="Linear Opmode")
//@Disabled
public class CDAutonBlueDuck extends CDAutonBase {
    @Override
    public void executeAuton() {
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 2, 5);
        myTurret.setTurretDirection("center");
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -2, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -17, 8);
        myDuckSpinner.setDuckSpinnerPower(.7);
        sleep(2500);
        myDuckSpinner.setDuckSpinnerPower(0);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 35, 10.0);
        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 25, 10.0);
        myIntake.setIntakePower(.4);
        sleep(1000);
        myIntake.setIntakePower(0);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -25, 10.0);
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -11, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -8, 10.0);
        myElevator.setElevatorPosition(7);
    }
}