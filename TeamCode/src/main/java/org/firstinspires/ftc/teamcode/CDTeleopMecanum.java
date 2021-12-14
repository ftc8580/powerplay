package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

//import com.qualcomm.robotcore.util.ElapsedTime;

//Libraries for imu
//import com.qualcomm.hardware.bosch.BNO055IMU;

import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.Locale;


@TeleOp(name="CDTeleopMecanum", group="Linear Opmode")
public class CDTeleopMecanum extends LinearOpMode implements Runnable {

    // Initialize our teleopThread
    private Thread teleopGamepad1Thread;
    // Initialize our local variables with values
    // These "slow" variable is used to control the overall speed of the robot
    // TODO: Work with Drive Team to determine proper baseSpeed, duckmulti
    public double baseSpeed = 0.70;
    public double intakemult = 1.0;
    public double delivermult = 0.75;
    public double duckmulti = 0.6;
    public final double DuckIncrement = 0.01; // amount to ramp motor each CYCLE_MS cycle
    public final int DuckCycleIncrement = 5; // period of each cycle in ms (.0001 sec)
    public final double Duck_Max_Fwd = 0.8; // Maximum FWD power applied to motor
    public final double Duck_Max_Rev = -0.6; // Maximum REV power applied to motor

    public boolean imuTelemetry = false;
    //For setting elevator position using buttons
    //This is where you can set the values of the positions based off telemetry
    //TODO Check that these values are updated for the latest elevator so that freight can be put in proper level of alliance hub

    // Initialize our local variables for use later in telemetry or other methods
    public double currentturretposition;
    public double currentturretthreshold;
    public double duckpower;
    public double y;
    public double x;
    public double rx;
    public double elevatorposcurrent;
    public double elevatorposlast = 14.0; // Arbitrary
    public double elevatorcurrentthreshold;
    public boolean elevatorisdown;
    public double eleDownThresh;
    public double elevatorEaseOut;
    public double threshWheelsCurrent;
    public double leftFrontPower;
    public double leftRearPower;
    public double rightFrontPower;
    public double rightRearPower;
    public boolean elevatorupmagnetswitch;
    public double robotSpeed;
    public double turretpotcurrent;
    public boolean turreterror = false;
    public boolean elevatorerror = false;
    // State used for updating telemetry
    public Orientation angles;
    public Acceleration gravity;
    public CDHardware myHardware;
    //    public BNO055IMU imu;
    public CDElevator myElevator;
    public CDTurret myTurret;
    public CDDistanceSensor myDistanceSensor;

    @Override
    public void runOpMode() {
        // Initialize our classes to variables
        myHardware = new CDHardware(hardwareMap);
        CDElevator myElevator = new CDElevator(myHardware);
        CDIntake myIntake = new CDIntake(myHardware);
        CDTurret myTurret = new CDTurret(myHardware);
        myDistanceSensor = new CDDistanceSensor(myHardware);
        // IMU Is commented out as we don't use it
//        imu = myHardware.cdimu;
        // Set up the parameters with which we will use our IMU. Note that integration
        // algorithm here just reports accelerations to the logcat log; it doesn't actually
        // provide positional information.
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
//        parameters.loggingEnabled      = true;
//        parameters.loggingTag          = "IMU";
//        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
//        imu.initialize(parameters);

        //Wait for the driver to press PLAY on the driver station phone
        // make a new thread
        telemetry.addData("Status", "Fully Initialized");
        telemetry.update();
        teleopGamepad1Thread = new Thread(this);
        waitForStart();
        teleopGamepad1Thread.start(); // Start the teleopThread

        //Run until the end (Driver presses STOP)

        // Polling rate for logging gets set to zero before the while loop
        int i = 0;

        while (opModeIsActive()) {
            // Start the logging of measured acceleration
            // Disabling IMU as it is not in use Uncomment here to use in opmode
//            imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);

            // Everything Gamepad 2

          /* This gets the current distance off the floor from the Elevator Distance Sensor
          and sets it to a variable. The following lines of code are focused on keeping track of the elevator
           */
            elevatorposcurrent = myElevator.getElevatorPosition();
            elevatorcurrentthreshold = myElevator.ELEVATORCURRENTTHRESHOLD;


            // magnetic switch
            elevatorupmagnetswitch = false;
            if (myHardware.elevatormagneticswitch.isPressed()) {
                elevatorupmagnetswitch = true;
            }
            // Used to make sure that the elevator is up when we turn the turret past wheels
            elevatorisdown = false;
            if (elevatorposcurrent <= myElevator.wheelheightforelevator) {
                elevatorisdown = true;
            }
            double elevatorinput = (gamepad2.left_stick_y * 1.0);
            double elevatorEaseOut = 1.0;

            // elevator watchdog
            if ((elevatorinput > .01) && (Math.abs(elevatorposcurrent-elevatorposlast) < .01)) {
                myElevator.setElevatorPower(-elevatorinput);
            } else if ((elevatorinput < -.01) && (Math.abs(elevatorposcurrent-elevatorposlast) < .01)){
                myElevator.setElevatorPower(-elevatorinput);
            } else {
                //
                if ((elevatorposcurrent <= myElevator.elevatorposground && elevatorinput > .01) || ((elevatorupmagnetswitch || elevatorposcurrent >= myElevator.elevatorpostop) && elevatorinput < -.01)) {
                    myElevator.setElevatorPower(0);
                } else {
                    if ((elevatorinput > .01 && elevatorposcurrent < 10) || (elevatorinput < -.01 && elevatorposcurrent > 35)) {
                        elevatorEaseOut = .85;
                    } else if (elevatorinput < .1) {
                        elevatorEaseOut = 1.0;
                    }
                    myElevator.setElevatorPower(-elevatorinput * elevatorEaseOut);
                }
            }
            // For elevator watchdog
            elevatorposlast = myElevator.getElevatorPosition();

            //  Values of the elevator position are defined in the variable init at the beginning
            // Dpad controls the position of the elevator
            if (gamepad2.dpad_down) {
                myElevator.setElevatorPosition(myElevator.elevatorposground);
            } else if (gamepad2.dpad_left) {
                myElevator.setElevatorPosition(myElevator.elevatorposbottom);
            } else if (gamepad2.dpad_right) {
                myElevator.setElevatorPosition(myElevator.elevatorposmiddle);
            } else if (gamepad2.dpad_up) {
                myElevator.setElevatorPosition(myElevator.elevatorpostop);
            }
            // Buttons control the turret and elevator position
            if (gamepad2.y) {
                turreterror = myTurret.setTurretDirection("center", false);
                if (myTurret.turretstop && !turreterror) {
                    myElevator.setElevatorPosition(myElevator.elevatorposground);
                }
            } else if (gamepad2.x) {
                myElevator.setElevatorPosition(myElevator.elevatorposmiddle);
                if (myElevator.elevatorstop) {
                    turreterror = myTurret.setTurretDirection("left", false);
                }
            } else if (gamepad2.b) {
                myElevator.setElevatorPosition(myElevator.elevatorposmiddle);
                if (myElevator.elevatorstop) {
                    turreterror = myTurret.setTurretDirection("right", false);
                }
            }

            //intake ( left trigger), deliver(right trigger)
            // Convert the analog trigger to a button push
            double intake = gamepad2.left_trigger;
            double deliver = gamepad2.right_trigger;
            if (intake > 0.2) {
                myIntake.setIntakePower(intake*intakemult);
            } else if (deliver > 0.2) {
                myIntake.setIntakePower(-deliver*delivermult);
            } else {
                myIntake.setIntakePower(0.0);
            }

            // TURRET CODE
            // Handle turret lockup error:
            if (turreterror) {
                telemetry.addLine("DANGER: THE TURRET VALUES AREN'T CHANGING!");
                telemetry.update();
            }
            // Refresh the turret position and reported threshold
            currentturretposition = myTurret.getTurrentPos(); // Variable Based
            turretpotcurrent = myTurret.getTurretPotVolts(); // Potentiometer voltage based
            currentturretthreshold = myTurret.getTurretCurrentThreshold();

            double turretA = gamepad2.right_stick_x;
            if (elevatorisdown && ((turretpotcurrent >= 1.5 && turretA <= -0.01) || (turretpotcurrent <= 1.85 && turretA >= 0.01))) {
                myTurret.setTurretPower(turretA*.5);
            } else if (!elevatorisdown && (turretA >= 0.01 || turretA <= 0.01)){
                myTurret.setTurretPower(turretA);
            } else {
                myTurret.setTurretPower(0.0);
            }

            // End gamepad 2

            // Telemetry Stuff -
            // need to slow down the logging
            if (i == 10) {
                composeTelemetry(imuTelemetry);
                i = 0;
            } else {
                i++;
            }
        }
    }

    // Threaded gamepad 1
    @SuppressWarnings("BusyWait")
    public void run() {
        try {
            CDDriveChassis myChassis = new CDDriveChassis(myHardware);
            CDDuckSpinner myDuckSpinner = new CDDuckSpinner(myHardware);
            while (opModeIsActive()) {

                // Everything gamepad 1:
                // User controls for the robot speed overall
                if (gamepad1.left_trigger != 0) {
                    robotSpeed = baseSpeed * 1.4;
                } else if (gamepad1.right_trigger != 0) {
                    robotSpeed = baseSpeed * .4;
                } else {
                    robotSpeed = baseSpeed;
                }
                // We cubed the inputs to make the inputs more responsive
                y = Math.pow(gamepad1.left_stick_y, 3); // Remember, this is reversed!
                x = Math.pow(gamepad1.left_stick_x * -1.1, 3); // Counteract imperfect strafing
                rx = Math.pow(gamepad1.right_stick_x, 3) * 0.5;  //Reduced turn speed to make it easier to control

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                leftFrontPower = (y + x - rx) / denominator;
                leftRearPower = (y - x - rx) / denominator;
                rightFrontPower = (y - x + rx) / denominator;
                rightRearPower = (y + x + rx) / denominator;

                //move robot - drive chassis
                myChassis.setLeftFrontPower(leftFrontPower * robotSpeed);
                myChassis.setLeftRearPower(leftRearPower * robotSpeed);
                myChassis.setRightFrontPower(rightFrontPower * robotSpeed);
                myChassis.setRightRearPower(rightRearPower * robotSpeed);

                //duck input is a boolean - it is on or off - if do not see option try boolean

                if (gamepad1.x) {
                    duckpower += DuckIncrement;
                    if (duckpower >= Duck_Max_Fwd) {
                        duckpower = Duck_Max_Fwd;
                    }
                    myDuckSpinner.setDuckSpinnerPower(duckpower);
                    sleep(DuckCycleIncrement);
                } else if (gamepad1.y) {
                    duckpower -= DuckIncrement;
                    if (duckpower <= Duck_Max_Rev) {
                        duckpower = Duck_Max_Rev;
                    }
                    myDuckSpinner.setDuckSpinnerPower(duckpower);
                    sleep(DuckCycleIncrement);
                } else if (gamepad1.a) {
                    duckpower = 0.8;
                    myDuckSpinner.setDuckSpinnerPower(duckpower);
                } else if (gamepad1.b) {
                    duckpower = -0.8;
                    myDuckSpinner.setDuckSpinnerPower(duckpower);
                } else {
                    duckpower = 0;
                    myDuckSpinner.setDuckSpinnerPower(duckpower);
                }
            }
            // End gamepad 1
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------

    void composeTelemetry(boolean imuTelemetry) {
        telemetry.clearAll();
        if (imuTelemetry) {
            // At the beginning of each telemetry update, grab a bunch of data
            // from the IMU that we will then display in separate lines.
//            Comment block disables telemetry reporting!
//            telemetry.addAction(new Runnable() {
//                @Override
//                public void run() {
//                    // Acquiring the angles is relatively expensive; we don't want
//                    // to do that in each of the three items that need that info, as that's
//                    // three times the necessary expense.
//                    angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//                    gravity = imu.getGravity();
//                }
//            });
//            //telemetry.addData("heading ", heading);
//            telemetry.addLine()
//                    .addData("status", new Func<String>() {
//                        @Override public String value() {
//                            return imu.getSystemStatus().toShortString();
//                        }
//                    })
//                    .addData("calib", new Func<String>() {
//                        @Override public String value() {
//                            return imu.getCalibrationStatus().toString();
//                        }
//                    });
//
//            telemetry.addLine()
//                    .addData("heading", new Func<String>() {
//                        @Override public String value() {
//                            return formatAngle(angles.angleUnit, angles.firstAngle);
//                        }
//                    })
//                    .addData("roll", new Func<String>() {
//                        @Override public String value() {
//                            return formatAngle(angles.angleUnit, angles.secondAngle);
//                        }
//                    })
//                    .addData("pitch", new Func<String>() {
//                        @Override public String value() {
//                            return formatAngle(angles.angleUnit, angles.thirdAngle);
//                        }
//                    });
//
//            telemetry.addLine()
//                    .addData("grvty", new Func<String>() {
//                        @Override public String value() {
//                            return gravity.toString();
//                        }
//                    })
//                    .addData("mag", new Func<String>() {
//                        @Override public String value() {
//                            return String.format(Locale.getDefault(), "%.3f",
//                                    Math.sqrt(gravity.xAccel*gravity.xAccel
//                                            + gravity.yAccel*gravity.yAccel
//                                            + gravity.zAccel*gravity.zAccel));
//                        }
//                    });
            // Loop and update the dashboard
        } else {
            telemetry.addData("y input", "%.2f", y);
            telemetry.addData("x input", "%.2f", x);
            telemetry.addData("rx input", "%.2f", rx);
            telemetry.addData("motorLF ", "%.2f", leftFrontPower);
            telemetry.addData("motorRF ", "%.2f", rightFrontPower);
            telemetry.addData("motorLR ", "%.2f", leftRearPower);
            telemetry.addData("motorRR ", "%.2f", rightRearPower);
            telemetry.addData("ElevatorDist", "%.2f", elevatorposcurrent);
            //           intakepos = myDistanceSensor.getIntakeDistance();
            telemetry.addData("IntakeDist", "%.2f", myDistanceSensor.getIntakeDistance());
            telemetry.addData("CurrElevatorThresh", "%.2f", elevatorcurrentthreshold);
            telemetry.addData("CurrElevatorDownThresh", "%.2f", eleDownThresh);
            telemetry.addData("TurretLockedElevatorDown", elevatorisdown);
//            telemetry.addData("TurretPosition", "%.2f", currentturretposition);
            telemetry.addData("TurretPotCurrent", "%.2f", turretpotcurrent);
            telemetry.addData("CurrTurretThreshold", "%.2f", currentturretthreshold);
            telemetry.addData("magneticstop", elevatorupmagnetswitch);
            telemetry.addData("turreterror", turreterror);
            telemetry.addData("elevatorerror", elevatorerror);

        }
        // Loop and update the dashboard
        telemetry.update();
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
