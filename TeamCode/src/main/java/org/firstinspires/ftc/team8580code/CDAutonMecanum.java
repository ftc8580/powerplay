package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="CDAutonMecanum", group="Linear Opmode")

public class CDAutonMecanum extends LinearOpMode {

    // todo: write your code here
  @Override
  public void runOpMode() {
    telemetry.addData("Status", "Initialized");
    telemetry.update();
      
    ElapsedTime myTimer = new ElapsedTime();
    double moveBackTimer = -1;
      
    CDHardware myHardware = new CDHardware(hardwareMap);
      
    //Most robots need the motor on one side to ve reversed to drive forward
    myHardware.leftfrontmotor.setDirection(DcMotorSimple.Direction.FORWARD);
    myHardware.leftrearmotor.setDirection(DcMotorSimple.Direction.FORWARD);
    myHardware.rightfrontmotor.setDirection(DcMotorSimple.Direction.REVERSE);
    myHardware.rightrearmotor.setDirection(DcMotorSimple.Direction.REVERSE);
     
    myHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    myHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    myHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    myHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        
    //Wait fo the fame to start (driver presses PLAY)
    waitForStart();
    myTimer.reset();
        
    //Run until the end of the match (Driver presses STOP)
    while (opModeIsActive()) {
        
      //Setup a variable for each drive wheel to save power level for telemetry
      double frontLeftPower;
      double backLeftPower;
      double frontRightPower;
      double backRightPower; 
      
      frontLeftPower = 0.5;
      backLeftPower = 0.5;
      frontRightPower = 0.5;
      backRightPower = 0.5; 
      
      //Go Straight
      if (myTimer.seconds()<3) {
        frontLeftPower = 0.5;
        backLeftPower = 0.5;
        frontRightPower = 0.5;
        backRightPower = 0.5;      
      }
      //Turn Right
      if (myTimer.seconds()<4) {
        frontLeftPower = 0.5;
        backLeftPower = 0.5;
        frontRightPower = -0.5;
        backRightPower = -0.5;      
      }      
      //Go Straight
      if (myTimer.seconds()<7) {
        frontLeftPower = 0.5;
        backLeftPower = 0.5;
        frontRightPower = 0.5;
        backRightPower = 0.5;      
      }
      //Turn Right
      if (myTimer.seconds()<8) {
        frontLeftPower = 0.5;
        backLeftPower = 0.5;
        frontRightPower = -0.5;
        backRightPower = -0.5;      
      }
      //Go Straight
      if (myTimer.seconds()<11) {
        frontLeftPower = 0.5;
        backLeftPower = 0.5;
        frontRightPower = 0.5;
        backRightPower = 0.5; 
      }
      //Turn Right
      if (myTimer.seconds()<12) {
        frontLeftPower = 0.5;
        backLeftPower = 0.5;
        frontRightPower = -0.5;
        backRightPower = -0.5;      
      }
      //Go Straight
      if (myTimer.seconds()<15) {
        frontLeftPower = 0.5;
        backLeftPower = 0.5;
        frontRightPower = 0.5;
        backRightPower = 0.5; 
      }
      //Stop
      if (myTimer.seconds()>=15) {
        frontLeftPower = 0.0;
        backLeftPower = 0.0;
        frontRightPower = 0.0;
        backRightPower = 0.0;   
      }
      
      //Send power to wheel motors
      myHardware.leftfrontmotor.setPower(frontLeftPower);
      myHardware.leftrearmotor.setPower(backLeftPower);
      myHardware.rightfrontmotor.setPower(frontRightPower);
      myHardware.rightrearmotor.setPower(backRightPower);
      
      //Show elapsed time and wheel power
      telemetry.addData("Status", "Run Time: " + myTimer.toString());
      telemetry.addData("motorLF ", "%.2f", frontLeftPower);
      telemetry.addData("motorRF ", "%.2f", frontRightPower);
      telemetry.addData("motorLR ", "%.2f", backLeftPower);
      telemetry.addData("motorRR ", "%.2f", backRightPower);
      telemetry.update();
    }
  }
}
