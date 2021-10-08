package org.firstinspires.ftc.team8580;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="CDTeleopMecanum", group="Linear Opmode")
public class CDTeleopMecanum extends LinearOpMode { 

  @Override
  public void runOpMode() {
      ElapsedTime myTimer = new ElapsedTime();
      double moveBackTimer = -1;
      
      CDHardware myHardware = new CDHardware(hardwareMap);
      
        myHardware.leftfrontmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        myHardware.leftrearmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        myHardware.rightfrontmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        myHardware.rightrearmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        
        myHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        myHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        myHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        myHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
      
      telemetry.addData("Status", "Fully Initialized");
      telemetry.update();
      
      //Wait for the driver to press PLAY on the driver station phone
      waitForStart();
      
      //Run until the end (Driver presses STOP)
      while (opModeIsActive()) {
          double y = -gamepad1.left_stick_y; // Remember, this is reversed!
          double x = gamepad1.left_stick_x * -1.1; // Counteract imperfect strafing
          double rx = gamepad1.right_stick_x;

          // Denominator is the largest motor power (absolute value) or 1
          // This ensures all the powers maintain the same ratio, but only when
          // at least one is out of the range [-1, 1]
          double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
          double frontLeftPower = (y + x + rx) / denominator;
          double backLeftPower = (y - x + rx) / denominator;
          double frontRightPower = (y - x - rx) / denominator;
          double backRightPower = (y + x - rx) / denominator;

          myHardware.leftfrontmotor.setPower(frontLeftPower);
          myHardware.leftrearmotor.setPower(backLeftPower);
          myHardware.rightfrontmotor.setPower(frontRightPower);
          myHardware.rightrearmotor.setPower(backRightPower);

          //move elevator + = up - = down
          double elevator = gamepad2.left_stick_y;
          myHardware.elevatormotor.setPower(elevator);

          //intake ( left trigger), deliver(right trigger)
          double intake = gamepad2.left_trigger;
          myHardware.intakemotor.setPower(intake);

          double deliver = gamepad2.right_trigger;
          myHardware.intakemotor.setPower(intake);

          //boolean is on off if do not see option try boolean
          boolean duckA = gamepad1.a;
          double duckpower;

          if (gamepad1.a) {
              duckpower = 0.5;
          } else if (gamepad1.b) {
              duckpower = -0.5;
          } else  {
          duckpower = 0;
      }

          myHardware.duckspinnermotor.setPower(duckpower);


          double turretA = gamepad1.left_stick_x;
          myHardware.turretmotor.setPower(turretA);


         telemetry.addData("y ", "%.2f", y);
         telemetry.addData("x ", "%.2f", x);
         telemetry.addData("rx ", "%.2f", rx);
         telemetry.update();
      }
      
  }
}
