package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.util.ElapsedTime;
//import com.qualcomm.robotcore.util.Hardware;
//import com.qualcomm.robotcore.hardware.DcMotor;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp(name="CDTeleopMecanum", group="Linear Opmode")
public class CDTeleopMecanum extends LinearOpMode { 

  @Override
  public void runOpMode() {

      CDHardware myHardware = new CDHardware(hardwareMap);
      CDDriveChassis myChassis = new CDDriveChassis(myHardware);
      CDDuckSpinner myDuckSpinner = new CDDuckSpinner(myHardware);
      CDElevator myElevator = new CDElevator(myHardware);
      CDIntake myIntake = new CDIntake(myHardware);
      CDTurret myTurret = new CDTurret(myHardware);


      telemetry.addData("Status", "Fully Initialized");
      telemetry.update();
      
      //Wait for the driver to press PLAY on the driver station phone
      waitForStart();
      
      //Run until the end (Driver presses STOP)
      while (opModeIsActive()) {
          double slow = .25;
          if (gamepad1.x) {
              slow = 0.5;
          } else if (gamepad1.y) {
              slow = .75;
          }
          double y = gamepad1.left_stick_y; // Remember, this is reversed!
          double x = gamepad1.left_stick_x * -1.1; // Counteract imperfect strafing
          double rx = gamepad1.right_stick_x;

          // Denominator is the largest motor power (absolute value) or 1
          // This ensures all the powers maintain the same ratio, but only when
          // at least one is out of the range [-1, 1]
          double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
          double leftFrontPower = (y + x + rx) / denominator;
          double leftRearPower = (y - x + rx) / denominator;
          // TODO: FIX DIRECTION
          double rightFrontPower = (y - x - rx) / denominator;
          double rightRearPower = (y + x - rx) / denominator;

          //move robot - drive chassis
          myChassis.setLeftFrontPower(leftFrontPower*slow);
          myChassis.setLeftRearPower(leftRearPower*slow);
          myChassis.setRightFrontPower(rightFrontPower*slow);
          myChassis.setRightRearPower(rightRearPower*slow);

          //move elevator + = up - = down
          double elevator = gamepad2.left_stick_y;
          // TODO: Need to limit the elevator range with the encoder sensor
          myElevator.setElevatorPower(elevator*slow);

          //intake ( left trigger), deliver(right trigger)
          double intake = gamepad2.left_trigger;
          myIntake.setIntakePower(intake);

          double deliver = -gamepad2.right_trigger;
          myIntake.setIntakePower(deliver);

          //duck input is a boolean - it is on or off - if do not see option try boolean
          // TODO: motorDuckSpinner is defined, but not installed on robot by build team
          double duckpower;

          if (gamepad1.a) {
              duckpower = 1;
          } else if (gamepad1.b) {
              duckpower = -1;
          } else  {
            duckpower = 0;
          }
          myDuckSpinner.setDuckSpinnerPower(duckpower*slow);

          // turret codd
          double turretA = gamepad2.left_stick_x;
          // TODO: Turret is not limited by the encoder, risk of breaking robot
          // TODO: Set up encoder sensor for motorTurret
          myTurret.setTurretPower(turretA*slow);

         telemetry.addData("y input", "%.2f", y);
         telemetry.addData("x input", "%.2f", x);
         telemetry.addData("rx input", "%.2f", rx);
         telemetry.addData("motorLF ", "%.2f", leftFrontPower);
         telemetry.addData("motorRF ", "%.2f", rightFrontPower);
         telemetry.addData("motorLR ", "%.2f", leftRearPower);
         telemetry.addData("motorRR ", "%.2f", rightRearPower);
         telemetry.update();
      }
      
  }
}
