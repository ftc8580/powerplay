package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name="CDAutonBlueDuck", group="Linear Opmode")
//@Disabled
public class CDAutonBlueDuck extends CDAutonBase {

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



