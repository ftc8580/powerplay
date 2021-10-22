package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
// import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
      // CDGyroscope myGyro = new CDGyroscope();
      CDDistanceSensor myDistanceSensor = new CDDistanceSensor(myHardware);

      //CDGyroscope myGyro = new CDGyroscope();

      telemetry.addData("Status", "Fully Initialized");
      telemetry.update();
      
      //Wait for the driver to press PLAY on the driver station phone
      waitForStart();
      
      //Run until the end (Driver presses STOP)
      while (opModeIsActive()) {
          // This "slow" variable is used to control the overall speed of the robot
          double slow = 0.60;

          if (gamepad1.left_bumper) {
              slow = 0.30;

          } else if (gamepad1.right_bumper) {
              slow = 0.90;
          }

          /* This gets the current distance off the floor from the Elevator Distance Sensor
          and sets it to a variable
           */
        double elevatorposcurrent = myDistanceSensor.getElevatorDistance();

          // We cubed the inputs to make the inputs more responsive
          double y = Math.pow(gamepad1.left_stick_y,3); // Remember, this is reversed!
          double x = Math.pow(gamepad1.left_stick_x * -1.1,3); // Counteract imperfect strafing
          double rx = Math.pow(gamepad1.right_stick_x,3);

          // Denominator is the largest motor power (absolute value) or 1
          // This ensures all the powers maintain the same ratio, but only when
          // at least one is out of the range [-1, 1]
          double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
          double leftFrontPower = (y + x - rx) / denominator;
          double leftRearPower = (y - x - rx) / denominator;
          // TODO: FIX DIRECTION
          double rightFrontPower = (y - x + rx) / denominator;
          double rightRearPower = (y + x + rx) / denominator;

          //move robot - drive chassis
          myChassis.setLeftFrontPower(leftFrontPower*slow);
          myChassis.setLeftRearPower(leftRearPower*slow);
          myChassis.setRightFrontPower(rightFrontPower*slow);
          myChassis.setRightRearPower(rightRearPower*slow);

          //move elevator + = up - = down
          double elevator = gamepad2.left_stick_y;
          // TODO: Need to limit the elevator range with the encoder sensor
          myElevator.setElevatorPower(-elevator);
            //Set elevator position using buttons
          double elevatorposground = 3.5;
          double elevatorposbottom = 12.5;
          double elevatorposmiddle = 28.0;
          double elevatorpostop = 41.0;
          double elevatorpostarget = 0.0;
          boolean elevatorstop = true;
          if (gamepad2.a) {
            elevatorpostarget=elevatorposground;
              elevatorstop = false;
          } else if (gamepad2.x) {
              elevatorpostarget=elevatorposbottom;
              elevatorstop = false;
          } else if (gamepad2.b) {
              elevatorpostarget=elevatorposmiddle;
              elevatorstop = false;
          } else if (gamepad2.y) {
              elevatorpostarget=elevatorpostop;
              elevatorstop = false;
          }

          while (!elevatorstop) {
              if (elevatorposcurrent > elevatorpostarget) {
                  myElevator.setElevatorPower(-1);
              } else if (elevatorposcurrent < elevatorpostarget) {
                  myElevator.setElevatorPower(1);
              } else {
                  myElevator.setElevatorPower(0);
                  elevatorstop = true;
              }
          }

          //intake ( left trigger), deliver(right trigger)
          // Convert the analog trigger to a button push
          double intake = gamepad2.left_trigger;
          double deliver = gamepad2.right_trigger;
          double intakemult = 1.5;
          double delivermult = 1.5;

          if (intake> 0.2) {
              myIntake.setIntakePower(intake*intakemult);
          } else if (deliver > 0.2) {
              myIntake.setIntakePower(-deliver*delivermult);
          } else {
              myIntake.setIntakePower(0.0);
          }

          //duck input is a boolean - it is on or off - if do not see option try boolean
          double duckpower;
          double duckmulti = 0.6;

          if (gamepad1.a) {
              duckpower = 1*duckmulti;
          } else if (gamepad1.b) {
              duckpower = -1*duckmulti;
          } else  {
            duckpower = 0;
          }
          myDuckSpinner.setDuckSpinnerPower(duckpower);

          // turret code
          //TODO: This variable Turretslow should be in the CDTurret class and addressed at the object level

          double turretA = gamepad2.right_stick_x;
          // TODO: Turret is not limited by the encoder, risk of breaking robot
          // TODO: Set up encoder sensor for motorTurret
          myTurret.setTurretPower(turretA);

         //double heading = myGyro.getHeading(AngleUnit.DEGREES);

          // magnetic switch
          boolean magneticstop = false;
          if (myHardware.elevatormagneticswitch.isPressed()) {
              magneticstop = true;
          }
         telemetry.addData("y input", "%.2f", y);
         telemetry.addData("x input", "%.2f", x);
         telemetry.addData("rx input", "%.2f", rx);
         telemetry.addData("motorLF ", "%.2f", leftFrontPower);
         telemetry.addData("motorRF ", "%.2f", rightFrontPower);
         telemetry.addData("motorLR ", "%.2f", leftRearPower);
         telemetry.addData("motorRR ", "%.2f", rightRearPower);
         telemetry.addData( "ElevatorDist", "%.2f", elevatorposcurrent);
         //TODO: Add telemetry for IMU Gyro need to be tested
         //telemetry.addData("heading ", heading);
          telemetry.addData("magneticstop", magneticstop );
         telemetry.update();


      }
      
  }
}
