package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="CDAutonMecanumDriveByTime", group="Linear Opmode")
// TODO: First prove out Telop before enabling Auton to protect the robot
@Disabled
public class CDAutonMecanumDriveByTime extends LinearOpMode {

  @Override
  public void runOpMode() {
    telemetry.addData("Status", "Initialized");
    telemetry.update();

    ElapsedTime myTimer = new ElapsedTime();
    double moveBackTimer = -1;
      
    CDHardware myHardware = new CDHardware(hardwareMap);
    CDDriveChassis myChassis = new CDDriveChassis(myHardware);

    //Wait fo the game to start (driver presses PLAY)
    waitForStart();
    myTimer.reset();
    // TODO: Need to use the timer to program the robot in Auton competition

    //Run until the end of the match (Driver presses STOP)
    while (opModeIsActive()) {
        
      //Setup a variable for each drive wheel to save power level for telemetry
      double leftFrontPower;
      double leftRearPower;
      double rightFrontPower;
      double rightRearPower;

      leftFrontPower = 0.5;
      leftRearPower = 0.5;
      rightFrontPower = 0.5;
      rightRearPower = 0.5;
      
      //Go Straight
      if (myTimer.seconds()<3) {
        leftFrontPower = 0.5;
        leftRearPower = 0.5;
        rightFrontPower = 0.5;
        rightRearPower = 0.5;
      }
      //Turn Right
      else if (myTimer.seconds()<4) {
        leftFrontPower = 0.5;
        leftRearPower = -0.5;
        rightFrontPower = -0.5;
        rightRearPower = 0.5;
      }      
      //Go Straight
      else if (myTimer.seconds()<7) {
        leftFrontPower = 0.5;
        leftRearPower = 0.5;
        rightFrontPower = 0.5;
        rightRearPower = 0.5;
      }
      //Turn Left
      else if (myTimer.seconds()<8) {
        leftFrontPower = -0.5;
        leftRearPower = 0.5;
        rightFrontPower = 0.5;
        rightRearPower = -0.5;
      }
      //Go Straight
      else if (myTimer.seconds()<11) {
        leftFrontPower = 0.5;
        leftRearPower = 0.5;
        rightFrontPower = 0.5;
        rightRearPower = 0.5;
      }
      //dont Turn
      else if (myTimer.seconds()<12) {
        leftFrontPower = 0.5;
        leftRearPower = 0.5;
        rightFrontPower = -0.5;
        rightRearPower = -0.5;
      }
      //Go Straight
      else if (myTimer.seconds()<15) {
        leftFrontPower = 0.5;
        leftRearPower = 0.5;
        rightFrontPower = 0.5;
        rightRearPower = 0.5;
      }
      //Stop
      else if (myTimer.seconds()>=15) {
        leftFrontPower = 0.0;
        leftRearPower = 0.0;
        rightFrontPower = 0.0;
        rightRearPower = 0.0;
      }
      
      //Send power to wheel motors
      myChassis.setLeftFrontPower(leftFrontPower);
      myChassis.setLeftRearPower(leftRearPower);
      myChassis.setRightFrontPower(rightFrontPower);
      myChassis.setRightRearPower(rightRearPower);
      
      //Show elapsed time and wheel power
      telemetry.addData("Status", "Run Time: " + myTimer.toString());
      telemetry.addData("motorLF ", "%.2f", leftFrontPower);
      telemetry.addData("motorRF ", "%.2f", rightFrontPower);
      telemetry.addData("motorLR ", "%.2f", leftRearPower);
      telemetry.addData("motorRR ", "%.2f", rightRearPower);
      //TODO: Add telemetry for IMU Gyro
      telemetry.update();
    }
  }
}
