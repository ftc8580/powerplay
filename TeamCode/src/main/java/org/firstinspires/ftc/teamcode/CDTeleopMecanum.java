package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.util.ElapsedTime;

//Libraries for imu
import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import org.firstinspires.ftc.robotcore.external.Func;
import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import java.util.Locale;



@TeleOp(name="CDTeleopMecanum", group="Linear Opmode")
public class CDTeleopMecanum extends LinearOpMode {
    // Initialize our classes to variables
    CDHardware myHardware = new CDHardware(hardwareMap);
    CDDriveChassis myChassis = new CDDriveChassis(myHardware);
    CDDuckSpinner myDuckSpinner = new CDDuckSpinner(myHardware);
    CDElevator myElevator = new CDElevator(myHardware);
    CDIntake myIntake = new CDIntake(myHardware);
    CDTurret myTurret = new CDTurret(myHardware);
    CDDistanceSensor myDistanceSensor = new CDDistanceSensor(myHardware);
    BNO055IMU imu = myHardware.cdimu;

    // State used for updating telemetry
    Orientation angles;
    Acceleration gravity;

    // Initialize our local variables with values
    // These "slow" variable is used to control the overall speed of the robot
    double slow = 0.60;
    double intakemult = 1.5;
    double delivermult = 1.5;
    double duckmulti = 0.6;

    //For setting elevator position using buttons
    //This is where you can set the values of the positions based off telemetry
    double elevatorposground = 3.5;
    double elevatorposbottom = 12.5;
    double elevatorposmiddle = 28.0;
    double elevatorpostop = 41.0;

    // Initialize some booleans for use later
    // Initialize our local variables for use later in telemetry or other methods
    double currentturretposition;
    double duckpower;
    double y;
    double x;
    double rx;
    double elevatorposcurrent;
    boolean elevatorisdown;
    double leftFrontPower;
    double leftRearPower;
    double rightFrontPower;
    double rightRearPower;
    boolean magneticstop;

    @Override public void runOpMode() {

       // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);

//        telemetry.addData("Status", "Fully Initialized");
//        telemetry.update();


        // Set up our telemetry dashboard, everything is now in this method
        composeTelemetry();

        //Wait for the driver to press PLAY on the driver station phone
        waitForStart();

        //Run until the end (Driver presses STOP)
        while (opModeIsActive()) {

            // Start the logging of measured acceleration
            imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

            // Loop and update the dashboard
            while (opModeIsActive()) {
                telemetry.update();
            }

            // User controls for the robot speed overall
            if (gamepad1.left_bumper) {
                slow = 0.30;

            } else if (gamepad1.right_bumper) {
                slow = 0.90;
            }

          /* This gets the current distance off the floor from the Elevator Distance Sensor
          and sets it to a variable
           */
            elevatorposcurrent = myDistanceSensor.getElevatorDistance();
            // We cubed the inputs to make the inputs more responsive
            y = Math.pow(gamepad1.left_stick_y,3); // Remember, this is reversed!
            x = Math.pow(gamepad1.left_stick_x * -1.1,3); // Counteract imperfect strafing
            rx = Math.pow(gamepad1.right_stick_x,3);

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio, but only when
            // at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            leftFrontPower = (y + x - rx) / denominator;
            leftRearPower = (y - x - rx) / denominator;
            rightFrontPower = (y - x + rx) / denominator;
            rightRearPower = (y + x + rx) / denominator;

            //move robot - drive chassis
            myChassis.setLeftFrontPower(leftFrontPower*slow);
            myChassis.setLeftRearPower(leftRearPower*slow);
            myChassis.setRightFrontPower(rightFrontPower*slow);
            myChassis.setRightRearPower(rightRearPower*slow);

            //move elevator + = up - = down
            double elevatorinput = gamepad2.left_stick_y;
            // TODO: Need to limit the elevator range with the sensor so drivers don't loosen string. Students to add while not check for elevatorupposition
            // Insert while not
            myElevator.setElevatorPower(-elevatorinput);

            // Values of the elevator position are in the variable init at the beginning
            if (gamepad2.a) {
                myElevator.setElevatorPosition(elevatorposground);
            } else if (gamepad2.x) {
                myElevator.setElevatorPosition(elevatorposbottom);
            } else if (gamepad2.b) {
                myElevator.setElevatorPosition(elevatorposmiddle);
            } else if (gamepad2.y) {
                myElevator.setElevatorPosition(elevatorpostop);
            }

            //intake ( left trigger), deliver(right trigger)
            // Convert the analog trigger to a button push
            double intake = gamepad2.left_trigger;
            double deliver = gamepad2.right_trigger;


            if (intake> 0.2) {
                myIntake.setIntakePower(intake*intakemult);
            } else if (deliver > 0.2) {
                myIntake.setIntakePower(-deliver*delivermult);
            } else {
                myIntake.setIntakePower(0.0);
            }

            //duck input is a boolean - it is on or off - if do not see option try boolean
            if (gamepad1.a) {
                duckpower = 1*duckmulti;
            } else if (gamepad1.b) {
                duckpower = -1*duckmulti;
            } else  {
                duckpower = 0;
            }
            myDuckSpinner.setDuckSpinnerPower(duckpower);

            // turret code
            // TODO: Thursday student to make an elevator is up variation of below elevatorisdown

            // Used to make sure that the elevator is up when we turn the turret past wheels
            elevatorisdown = false;
            if (elevatorposcurrent < 10) {
                elevatorisdown = true;
            }
            if (!elevatorisdown) {
                double turretA = gamepad2.right_stick_x;
                myTurret.setTurretPower(turretA);
            }
            // Dpad controls the turret position
            if (gamepad2.dpad_up) {
                myTurret.setTurretPosition(0,"right");
                if (myTurret.turretstop) {
                    myElevator.setElevatorPosition(elevatorposground);
                }
            } else if (gamepad2.dpad_left) {
                myElevator.setElevatorPosition(elevatorposmiddle);
                if (myElevator.elevatorstop) {
                    myTurret.setTurretPosition(90,"right");
                }
            } else if (gamepad2.dpad_right) {
                myElevator.setElevatorPosition(elevatorposmiddle);
                if (myElevator.elevatorstop) {
                    myTurret.setTurretPosition(-90, "right");
                }
            }
            // Refresh the turret position
            currentturretposition = myTurret.getTurrentPos();
            // magnetic switch
            magneticstop = false;
            if (myHardware.elevatormagneticswitch.isPressed()) {
                magneticstop = true;
            }
        }

    }


    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry() {

        // At the beginning of each telemetry update, grab a bunch of data
        // from the IMU that we will then display in separate lines.
        telemetry.addAction(new Runnable() { @Override public void run()
        {
            // Acquiring the angles is relatively expensive; we don't want
            // to do that in each of the three items that need that info, as that's
            // three times the necessary expense.
            angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
            gravity  = imu.getGravity();
        }
        });
        // We always want the latest position

        telemetry.addData("y input", "%.2f", y);
        telemetry.addData("x input", "%.2f", x);
        telemetry.addData("rx input", "%.2f", rx);
        telemetry.addData("motorLF ", "%.2f", leftFrontPower);
        telemetry.addData("motorRF ", "%.2f", rightFrontPower);
        telemetry.addData("motorLR ", "%.2f", leftRearPower);
        telemetry.addData("motorRR ", "%.2f", rightRearPower);
        telemetry.addData( "ElevatorDist", "%.2f", elevatorposcurrent);
        telemetry.addData("TurretLockedElevatorDown", elevatorisdown);
        telemetry.addData("TurretPosition", "%.2f", currentturretposition);

        //TODO: Add telemetry for IMU Gyro need to be tested
        //telemetry.addData("heading ", heading);
        telemetry.addData("magneticstop", magneticstop );
        telemetry.addLine()
                .addData("status", new Func<String>() {
                    @Override public String value() {
                        return imu.getSystemStatus().toShortString();
                    }
                })
                .addData("calib", new Func<String>() {
                    @Override public String value() {
                        return imu.getCalibrationStatus().toString();
                    }
                });

        telemetry.addLine()
                .addData("heading", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.firstAngle);
                    }
                })
                .addData("roll", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.secondAngle);
                    }
                })
                .addData("pitch", new Func<String>() {
                    @Override public String value() {
                        return formatAngle(angles.angleUnit, angles.thirdAngle);
                    }
                });

        telemetry.addLine()
                .addData("grvty", new Func<String>() {
                    @Override public String value() {
                        return gravity.toString();
                    }
                })
                .addData("mag", new Func<String>() {
                    @Override public String value() {
                        return String.format(Locale.getDefault(), "%.3f",
                                Math.sqrt(gravity.xAccel*gravity.xAccel
                                        + gravity.yAccel*gravity.yAccel
                                        + gravity.zAccel*gravity.zAccel));
                    }
                });
    }

    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }

    String formatDegrees(double degrees){
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
