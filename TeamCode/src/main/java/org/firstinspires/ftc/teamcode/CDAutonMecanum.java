package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="CDAutonMecanum", group="Linear Opmode")
//@Disabled
public class CDAutonMecanum extends LinearOpMode {

  @Override
  public void runOpMode() {
   //ElapsedTime myTimer = new ElapsedTime();
   //double moveBackTimer = -1;
      
    CDHardware myHardware = new CDHardware(hardwareMap);
    CDDriveChassisAuton myChassis = new CDDriveChassisAuton(myHardware);

    //Send telemetry to signify robot waiting
    telemetry.addData("Status", "Resetting Encoders");
    telemetry.update();

    myChassis.robotHardware.rightrearmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    myChassis.robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    myChassis.robotHardware.leftrearmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    myChassis.robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

    myChassis.robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    myChassis.robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    myChassis.robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    myChassis.robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
    /*Drive code is written using 4 methods
    *     speed should be either DRIVE_SPEED or TURN_SPEED
    * These assumes that each movement is relative to the last stopping place
    * 1. encoderDriveStraight (speed, straightInches, straightTimeout)
    * 2.
    * 3.
    * 4.
     */
    if (opModeIsActive()) {

      myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 30, 20.0); //Move forward 30 inches with 10 second timeout
      //sleep(250); //optional pause after each move in milliseconds
      myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -20, 10.0); //Move back 20 inches with 10 second timeout


      //Run until the end of the match (Driver presses STOP)
      //telemetry.addData("MotorCurrentPos (RR, RF, LR, LF)", " %7d %7d %7d %7d", myChassis.robotHardware.rightrearmotor.getCurrentPosition(), myChassis.robotHardware.rightfrontmotor.getCurrentPosition(), myChassis.robotHardware.leftrearmotor.getCurrentPosition(), myChassis.robotHardware.leftfrontmotor.getCurrentPosition());
      //telemetry.update();
      //sleep (5000);
      // TODO figure out how to show target position. 3 lines above will give final position.
      //TODO: Add telemetry for IMU Gyro
    }
  }
}
