package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="CDAutonRedWarehouse_LONG", group="Linear Opmode")
//@Disabled
public class CDAutonRedWarehouse_LONG extends CDAutonBase {
    @Override
    public void initTokenWeDoNotSee() {
        duckWeDoNotSee = 3;
    }

    public double intakepos;
    static final int endofroundtimevalue = 90;

    static final double AUTON_LONG_SPEED = 0.6;
    static final double AUTON_LONG_TURN = 0.5;
    static final double AUTON_LONG_APPROACH_SPEED = 0.4;

    static final double capturedobjintakedist = 6.65;
    static final double slowerintake = -0.4;
    static final double regularintake = -0.8;
    static final double deliverintake = 0.5;

    public void HuntForBlockReturnToBegin() {
        int driveincr = 3;
        int drivecyclecount = 0;
        while (intakepos > capturedobjintakedist) {
            //check block there (less than 6 then block is there)
            intakepos = myDistanceSensor.getIntakeDistance();
            telemetry.addData("IntakeDist", "%.2f", intakepos);
            telemetry.update();
            myIntake.setIntakePower(regularintake);
            myChassis.encoderDriveStraight(AUTON_LONG_APPROACH_SPEED, driveincr, 2.0);
            intakepos = myDistanceSensor.getIntakeDistance();
            //myIntake.setIntakePower(0);
            drivecyclecount=drivecyclecount+1;
        }
        myIntake.setIntakePower(0);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED, -drivecyclecount*driveincr, 10.0);
    }
    @Override
    public void executeAuton() {
        ElapsedTime myTimer = new ElapsedTime();
        myTimer.reset();

        // Start back at right edge
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -3, 5);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED,3,10.0);
        myChassis.encoderDriveTurn(AUTON_LONG_TURN, -35, 10);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -5, 18);

        sleep(100);
        myTurret.setTurretDirection("left", true);
        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));

        // go to elevator
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -13, 18);
        myChassis.encoderDriveStrafe(AUTON_LONG_APPROACH_SPEED, -3, 18);
        //myChassis.encoderDriveTurn(AUTON_LONG_TURN, -55, 10);
        myIntake.setIntakePower(deliverintake);
        sleep(1000);
        myIntake.setIntakePower(0);
        //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 55, 10);

        // Return
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 21, 18);
        myChassis.encoderDriveTurn(AUTON_LONG_TURN, 35, 10);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 12, 18);

        // Enter warehouse after delivery
        myTurret.setTurretDirection("center", true);
        sleep(100);
        myTurret.setTurretDirection("center", false);
        myElevator.setElevatorPosition(myElevator.elevatorposground);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED,26,10.0);
        //position above is just inside warehouse

        //turn on intake move forward then back
        HuntForBlockReturnToBegin();
        //check block there (less than 6 then block is there)
        intakepos = myDistanceSensor.getIntakeDistance();
        telemetry.addData("IntakeDist", "%.2f", intakepos);
        telemetry.update();

        //delivery 2 if block
        if ((myTimer.seconds()<endofroundtimevalue) && (intakepos < capturedobjintakedist)) {
            myElevator.setElevatorPosition(myElevator.elevatorpostop);
            myTurret.setTurretDirection("left", false);
            //go to hub
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 4, 5);
            myChassis.encoderDriveStraight(AUTON_LONG_SPEED,-26,10.0);
            sleep(100);
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -12, 10);
            sleep(100);
            myChassis.encoderDriveTurn(AUTON_LONG_TURN, -35, 10);
            sleep(100);
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -18, 18);
            myChassis.encoderDriveStrafe(AUTON_LONG_APPROACH_SPEED, -3, 18);
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -55, 10);
            myIntake.setIntakePower(deliverintake);
            sleep(1000);
            myIntake.setIntakePower(0);
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 55, 10);
            // Return
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 21, 18);
            myChassis.encoderDriveTurn(AUTON_LONG_TURN, 35, 10);
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 18, 18);
            // Enter warehouse
        }

        //if no block try again
        intakepos = myDistanceSensor.getIntakeDistance();
        if ((myTimer.seconds()<endofroundtimevalue) && (intakepos > capturedobjintakedist)) {
            intakepos = myDistanceSensor.getIntakeDistance();
            myTurret.setTurretDirection("center", false);
            sleep(100);
            myTurret.setTurretDirection("center", false);
            myElevator.setElevatorPosition(myElevator.elevatorposground);
            myChassis.encoderDriveStraight(AUTON_LONG_SPEED,26,10.0);
            HuntForBlockReturnToBegin();
        }
        intakepos = myDistanceSensor.getIntakeDistance();

        //delivery 2 if block
        if ((myTimer.seconds()<endofroundtimevalue) && (intakepos < capturedobjintakedist)) {
            myElevator.setElevatorPosition(myElevator.elevatorpostop);
            myTurret.setTurretDirection("left", false);
            //go to hub
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 4, 5);
            myChassis.encoderDriveStraight(AUTON_LONG_SPEED,-26,10.0);
            sleep(100);
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -12, 10);
            sleep(100);
            myChassis.encoderDriveTurn(AUTON_LONG_TURN, -35, 10);
            sleep(100);
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -18, 18);
            myChassis.encoderDriveStrafe(AUTON_LONG_APPROACH_SPEED, -3, 18);
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -55, 10);
            myIntake.setIntakePower(deliverintake);
            sleep(1000);
            myIntake.setIntakePower(0);
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 55, 10);
            // Return
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 21, 18);
            myChassis.encoderDriveTurn(AUTON_LONG_TURN, 35, 10);
            myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 18, 18);
            // Enter warehouse
            myChassis.encoderDriveStraight(AUTON_LONG_SPEED,26,10.0);
        }

        //park
        myElevator.setElevatorPosition(myElevator.elevatorposmiddle);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED,-25,10.0);
        myTurret.setTurretDirection("right", false);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED,28,10.0);
        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10);
        myTurret.setTurretDirection("center", false);
        intakepos = myDistanceSensor.getIntakeDistance();
        if (intakepos > capturedobjintakedist) {
            myElevator.setElevatorPosition(myElevator.elevatorposground);
            myIntake.setIntakePower(regularintake);
            myChassis.encoderDriveStraight(AUTON_LONG_APPROACH_SPEED, 12, 10.0);
            myIntake.setIntakePower(0);
            myChassis.encoderDriveStraight(AUTON_LONG_SPEED, -12, 10.0);
        }



//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,-5,10.0);
//        myElevator.setElevatorPosition(7);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,45,10.0);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,28,10.0);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,-24,10.0);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,24,10.0);
//        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10);
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


//OLD FROM 1ST COMPETITION
//    public void executeAuton() {
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -3, 5);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -24, 10.0);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -12, 15);
//        myTurret.setTurretDirection("left", true);
//        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));
//        myIntake.setIntakePower(.4);
//        sleep(1000);
//        myIntake.setIntakePower(0);
//        myTurret.setTurretDirection("center", true);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 15, 15);
//        myElevator.setElevatorPosition(14);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,40,10.0);
//        myElevator.setElevatorPosition(7);
//        myIntake.setIntakePower(-.6);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,8,10.0);
//        myIntake.setIntakePower(0);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,-48,10.0);
//        myElevator.setElevatorPosition(39);
//        myTurret.setTurretDirection("left", true);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -15, 15);
//        myIntake.setIntakePower(.4);
//        sleep(1000);
//        myIntake.setIntakePower(0);
//        myTurret.setTurretDirection("center", true);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 15, 15);
//        myElevator.setElevatorPosition(14);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -36, 15);
//        myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 10);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,-25,10.0);
//        myElevator.setElevatorPosition(7);
//    }
//}