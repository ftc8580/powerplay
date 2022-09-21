package org.firstinspires.ftc.teamcode.samples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="CDAutonBlueWarehouse_LONGDIAG", group="Linear Opmode")
@Disabled
public class CDAutonBlueWarehouse_LONGDIAG extends CDAutonBase {
    @Override
    public void initTokenWeDoNotSee() {
        duckWeDoNotSee = 1;
    }

    public double intakepos;
    static final int endofroundtimevalue = 25;

    static final double AUTON_LONG_SPEED = .7;
    static final double AUTON_LONG_TURN = 0.4;
    static final double AUTON_LONG_APPROACH_SPEED = 0.4;

    static final double capturedobjintakedist = 6.5;
    static final double slowerintake = -0.4;
    static final double regularintake = -0.8;
    static final double deliverintake = 0.5;

    public boolean RoundtripWarehouse() {
        myElevator.setElevatorPosition(myElevator.elevatorposmiddle);
        myTurret.setTurretDirection("right", false);
        //go to hub
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -9, 5);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED,-36,10.0);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED,-2,10.0);
        sleep(200);
        // Start back at right edge
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 9, 5);
        myElevator.setElevatorPosition(myElevator.elevatorpostop);

        // go to deliver
        myChassis.encoderDriveDiag(AUTON_LONG_SPEED, -23.0, 20, false);
        myChassis.encoderDriveDiag(AUTON_LONG_SPEED, -3.0, 20, false);
        //Deliver
        myIntake.setIntakePower(deliverintake);
        sleep(325);
        myIntake.setIntakePower(0);

        // Return
        myChassis.encoderDriveDiag(AUTON_LONG_SPEED, 26, 20, true);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -21, 18);

        // Enter warehouse after delivery
        myTurret.setTurretDirection("center", true);
        sleep(100);
        myTurret.setTurretDirection("center", false);
        myElevator.setElevatorPosition(myElevator.elevatorposground);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED,33,10.0);

        //position above is just inside warehouse
        return true;
    }
    public boolean HuntForBlockReturnToBegin() {
        double driveincr = 4.0;
        double maxdistance = 24.0;
        int drivecyclecount = 0;
        do {
            //check block there (less than 6 then block is there)
            intakepos = myDistanceSensor.getIntakeDistance();
            sleep(100);
            myIntake.setIntakePower(-0.9);
            myChassis.encoderDriveStraight(0.6, driveincr, 10.0);
            sleep(100);
            myIntake.setIntakePower(0);
            sleep(150);
            intakepos = myDistanceSensor.getIntakeDistance();
            drivecyclecount = drivecyclecount + 1;
            if (drivecyclecount == maxdistance/driveincr) {return true;}
        } while (intakepos > capturedobjintakedist);
        myIntake.setIntakePower(0);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED, -drivecyclecount*driveincr, 10.0);
        return true;
    }
    @Override
    public void executeAuton() {
        ElapsedTime myTimer = new ElapsedTime();
        myTimer.reset();

        // Start back at right edge
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, 3, 5);

        sleep(100);
        myTurret.setTurretDirection("right", true);
        myElevator.setElevatorPosition(getDuckDeliveryLocation(duckLocation, myElevator));

        // go to elevator
        myChassis.encoderDriveDiag(AUTON_LONG_SPEED, -28.0, 20, false);
        myChassis.encoderDriveDiag(AUTON_LONG_SPEED, -3.0, 20, false);
        myIntake.setIntakePower(deliverintake);
        sleep(325);
        myIntake.setIntakePower(0);

        // Return
        myChassis.encoderDriveDiag(AUTON_LONG_SPEED, 31, 20, true);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED, -20, 18);

        // Enter warehouse after delivery
        myTurret.setTurretDirection("center", true);
        sleep(200);
        myTurret.setTurretDirection("center", false);
        myElevator.setElevatorPosition(myElevator.elevatorposground);
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED,33,10.0);
        //position above is just inside warehouse

        //turn on intake move forward then back
        HuntForBlockReturnToBegin();
        //check block there (less than 6 then block is there)
        intakepos = myDistanceSensor.getIntakeDistance();
        telemetry.addData("IntakeDist", "%.2f", intakepos);
        telemetry.update();

        //delivery 2 if block
        if ((myTimer.seconds()<endofroundtimevalue) && (intakepos < capturedobjintakedist)) {
            RoundtripWarehouse();
        }

        //if no block try again
        intakepos = myDistanceSensor.getIntakeDistance();
        if ((myTimer.seconds()<endofroundtimevalue) && (intakepos > capturedobjintakedist)) {
            HuntForBlockReturnToBegin();
        }

        intakepos = myDistanceSensor.getIntakeDistance();

        //delivery 2 if block
        if ((myTimer.seconds()<endofroundtimevalue) && (intakepos < capturedobjintakedist)) {
            RoundtripWarehouse();
        }

        //park
        myChassis.encoderDriveStraight(AUTON_LONG_SPEED, 6, 5.0);
        myElevator.setElevatorPosition(myElevator.elevatorposmiddle);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED,22,5.0);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED,1,5.0);
//        myTurret.setTurretDirection("right", false);
        myChassis.encoderDriveTurn(AUTON_LONG_TURN, -90, 5.0);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED,22,5.0);
        myChassis.encoderDriveStrafe(AUTON_LONG_SPEED,3,5.0);
        myTurret.setTurretDirection("center", false);

        intakepos = myDistanceSensor.getIntakeDistance();
        if (intakepos > capturedobjintakedist) {
            myElevator.setElevatorPosition(myElevator.elevatorposground);
            HuntForBlockReturnToBegin();
        }



//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,-5,10.0);
//        myElevator.setElevatorPosition(7);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,45,10.0);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,28,10.0);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,-24,10.0);
//        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,24,10.0);
//        myChassis.encoderDriveTurn(AUTON_LONG_TURN, 90, 10);
    }
}


//Sample Code
//Drive
//myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 30, 10.0); //Move forward 30 inches with 5 second timeout. Negative is backward.
//myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 10, 5.0); //Move right 10 inches with 5 second timeout. Negative is left.
//myChassis.encoderDriveTurn(AUTON_LONG_TURN, -180, 5.0); //Turn left 180 degrees with a 5 second timeout. Positive is right.

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
//        myChassis.encoderDriveTurn(AUTON_LONG_TURN, -90, 10);
//        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED,-25,10.0);
//        myElevator.setElevatorPosition(7);
//    }
//}