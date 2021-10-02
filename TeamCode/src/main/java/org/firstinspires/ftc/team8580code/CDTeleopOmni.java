package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="CDTeleopOmni", group="Linear Opmode")
public class CDTeleopOmni extends LinearOpMode { 

  @Override
  public void runOpMode() {
      ElapsedTime myTimer = new ElapsedTime();
      double moveBackTimer = -1;
      
      CDHardware myHardware = new CDHardware(hardwareMap);
      CDDriveChassis myChassis = new CDDriveChassis(myHardware);
      
      telemetry.addData("Status", "Fully Initialized");
      telemetry.update();
      
      //Wait for the driver to press PLAY on the driver station phone
      waitForStart();
      
      //Run until the end (Driver presses STOP)
      while (opModeIsActive()) {
          double drive;
          double turn;
          
          drive = gamepad1.left_stick_y;
          turn = gamepad1.right_stick_x;
          
          myChassis.setLeftPower(drive - turn);
          myChassis.setRightPower(drive + turn);
          
          telemetry.addData("drive ", "%.2f", drive);
          telemetry.addData("turn ", "%.2f", drive);
          telemetry.update();
      }
      
  }
}
