package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="CDAutonRedWarehouse", group="Linear Opmode")
//@Disabled
public class CDAutonRedWarehouse extends LinearOpMode {

    @Override
    public void runOpMode() {
        ElapsedTime myTimer = new ElapsedTime();
        //double moveBackTimer = -1;

        CDHardware myHardware = new CDHardware(hardwareMap);
        CDDriveChassisAuton myChassis = new CDDriveChassisAuton(myHardware);
        CDDuckSpinner myDuckSpinner = new CDDuckSpinner(myHardware);
        CDElevator myElevator = new CDElevator(myHardware);
        CDIntake myIntake = new CDIntake(myHardware);
        CDTurret myTurret = new CDTurret(myHardware);
        // CDGyroscope myGyro = new CDGyroscope();
        CDDistanceSensor myDistanceSensor = new CDDistanceSensor(myHardware);

        //Send telemetry to signify robot waiting
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();

        myChassis.robotHardware.rightrearmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.leftrearmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myTurret.robotHardware.turretmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        myChassis.robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myTurret.robotHardware.turretmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //Send telemetry to indicate successful Encoder reset
        telemetry.addData("MotorStartPos (RR, RF, LR, LF)", " %7d %7d %7d %7d", myChassis.robotHardware.rightrearmotor.getCurrentPosition(), myChassis.robotHardware.rightfrontmotor.getCurrentPosition(), myChassis.robotHardware.leftrearmotor.getCurrentPosition(), myChassis.robotHardware.leftfrontmotor.getCurrentPosition());
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Wait fo the game to start (driver presses PLAY)
        waitForStart();
        //myTimer.reset();
        // TODO: Need to use the timer to program the robot in Auton competition

        //Step through each leg of path
        //Note: Reverse movement is obtained by selling a negative distance not speed
        /*Drive code is written using 3 methods
         *     speed should be either DRIVE_SPEED or TURN_SPEED
         * These assumes that each movement is relative to the last stopping place
         * 1. encoderDriveStraight (speed, straightInches, straightTimeout)
         * 2. encoderDriveStrafe (speed, strafeInches, strafeTimeout)
         * 3. encodeDriveTurn (speed, turnDeg, turnTimeout)
         */

        if (opModeIsActive()) {

            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -24, 10.0);
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -15, 15);
            myTurret.setTurretPosition(60, "NONE");
            myElevator.setElevatorPosition(28);
            myIntake.setIntakePower(-30);
            myTurret.setTurretPosition(124, "NONE");
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 15, 15);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,40,10.0);
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -3, 15);
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED,25,10.0);

            //THIS MESSAGE IS ONLY SO I CA PUSH IT ONTO DEVELOP AN D TEST

            //myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 30, 20.0); //Move forward 30 inches with 10 second timeout
            //sleep(250); //optional pause after each move in milliseconds
            //myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -20, 10.0); //Move back 20 inches with 10 second timeout
            //myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 10, 5); //Move right 10 inches with 5 second timeout
            //myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -15, 15); //Move left 15 inches with 15 second timeout
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10); //Turn right 90 degrees with 10 second timeout
            //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -180, 20); //Turn left 180 degrees with a 20 second timeout
            //sleep(30); //optional pause after each move in milliseconds
            //myElevator.setElevatorPosition(28); //Move elevator to middle position. Do not set outside of range 2.5-39.
           // myTurret.setTurretPosition(0, "right"); //Starting on right side - Set turret position to center
           // myTurret.setTurretPosition(-90, "right");  //Starting on right side - Set turret position -90 (right)
            //myTurret.setTurretPosition(90, "right");   //Starting on right side - Set turret position 90 (left)

            //  myIntake.setIntakePower(1.0);
            //  myDuckSpinner.setDuckSpinnerPower(.7);


            //Run until the end of the match (Driver presses STOP)
            //telemetry.addData("MotorCurrentPos (RR, RF, LR, LF)", " %7d %7d %7d %7d", myChassis.robotHardware.rightrearmotor.getCurrentPosition(), myChassis.robotHardware.rightfrontmotor.getCurrentPosition(), myChassis.robotHardware.leftrearmotor.getCurrentPosition(), myChassis.robotHardware.leftfrontmotor.getCurrentPosition());
            //telemetry.update();
            //sleep (5000);
            //TODO figure out how to show target position. 3 lines above will give final position.
            //TODO: Add telemetry for IMU Gyro
        }
    }
}
