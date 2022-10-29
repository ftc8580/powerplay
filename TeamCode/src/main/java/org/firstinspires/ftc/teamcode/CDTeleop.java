package org.firstinspires.ftc.teamcode;

// There are work in progress / untested IMU elements in this code which we may want to use so they are preserved.
//import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

// Telemetry

//
// import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import java.util.Locale;


@TeleOp(name="CDTeleop", group="Linear Opmode")
public class CDTeleop extends LinearOpMode implements Runnable {

    // Initialize our teleopThread
    private Thread teleopGamepad1Thread;
    // Initialize our local variables with values
    // The basespeed "slow" variable is used to control the overall speed of the robot
    // TODO: Work with Drive Team to determine
    public double baseSpeed = 0.90;

    public boolean imuTelemetry = false;
    //For setting elevator position using buttons
    //This is where you can set the values of the positions based off telemetry
    //TODO Check that these values are updated for the latest elevator so that freight can be put in proper level of alliance hub

    // Initialize our local variables for use later in telemetry or other methods
    //Drive variables
    public double y;
    public double x;
    public double rx;
    public double leftFrontPower;
    public double leftRearPower;
    public double rightFrontPower;
    public double rightRearPower;
    public double robotSpeed;
    public boolean constrainMovement;
    //Fourbar variables
    public double currentFourBarPosition;
    public double currentFourBarThreshold;
    public double fourBarPotCurrent;
    public boolean fourBarError;
    //Arm UpDown variables
    public double armUpDownPosCurrent;
    public double armUpDownAtarget;
/*    public int armPosCurrent;
    public double armPosLast = 1.1; // Arbitrary
    public double armUpMulti = 1.0; // In case we want to slow down the arm with the analog input.
    public double armCurrentThreshold;
    public boolean armIsDown;
    public double armDownThresh;
    public double armEaseOut;
    public double armUpDownPower = 0.5;
    public int armtargetPosition;*/
    //public boolean armupmagnetswitch;
    //public boolean armError = false;

    //ArmRot variables
    public double armRotPosCurrent;
    public double armrotAtarget;
/*    public double armRotPosLast;
    public double armRotCurrentThreshold;*/
    //Pickup variables
    //public double pickUpPosCurrent;

    //Intake variables
    public double intakePosCurrent;
    public double intakeAtarget;
    //Extend variables
    public double extendPosCurrent;
    public double extendAtarget;
    //Grab variables
    public double grabPosCurrent;
    public double grabAtarget;

    public CDHardware myHardware;
    // public org.firstinspires.ftc.teamcode.CDHardware myHardware;
    public CDFourBar myFourbar;
    public CDArm myArm;
    //public CDPickup myPickup;

    // State used for updating telemetry
    //    public Orientation angles;
    //    public Acceleration gravity;
    //    public BNO055IMU imu;

    @Override
    public void runOpMode() {
        // Initialize our classes to variables
        myHardware = new CDHardware(hardwareMap);
        CDFourBar myFourbar = new CDFourBar(myHardware);
        CDArm myArm = new CDArm(myHardware);
        //CDPickup myPickup = new CDPickup(myHardware);

        // Configure initial variables
        //TODO if we want pacman model to be default this should be set to true
        constrainMovement = false;

        //Wait for the driver to press PLAY on the driver station/phone
        telemetry.addData("Status", "Fully Initialized");
        telemetry.update();

        // make a new thread
        teleopGamepad1Thread = new Thread(this); // Define teleopThread
        waitForStart();
        //Run until the end (Driver presses STOP)
        teleopGamepad1Thread.start(); // Start the teleopThread

        // Polling rate for logging gets set to zero before the while loop
        int i = 0;

        while (opModeIsActive()) {

        // Everything Gamespad 2 will be handled between these two comments
            //
            // GAMEPAD 2 Code!
            // FOURBAR CODE
            if (fourBarError) {
                telemetry.addLine("DANGER: THE FOURBAR VALUES AREN'T CHANGING!");
                telemetry.update();
            }
            //Refresh the fourbarposition and report threshold
            currentFourBarPosition = myFourbar.getFourbarPos(); //Variable Based
            fourBarPotCurrent = myFourbar.getFourbarPotVolts(); //Potentiometer voltage based
            currentFourBarThreshold = myFourbar.getFourbarCurrentThreshold();

/*
            //Refresh the armpostion and report threshold
            armPosCurrent = myArm.getArmPosition();
            armCurrentThreshold = myArm.armCurrentThreshold;
*/

            double fourbarA = gamepad2.left_stick_y;
            //Slow at top and bottom
            //TODO put in proper values for fourbarpot current near top and bottom so it slows down
            if ((fourBarPotCurrent > 3 && fourbarA <-0.01) || (fourBarPotCurrent <.25 && fourbarA >=0.01)) {
                myFourbar.setFourbarPower(fourbarA*-.5);
                //move arm proportionally to fourbar to keep level
                //TODO Define multiplier that keeps this level- currently .1 below- may need to change direction- below removed since stays level
                //myArm.setArmPower(fourbarA*-.05*.1);
            } else if (fourbarA >=0.01 || fourbarA <=-0.01) {
                //TODO check direction on gamepad - remove *-1 below and - on .5 above if direction is wrong
                myFourbar.setFourbarPower(fourbarA*-1);//Remember on controller -y is up
                //move arm proportionally to fourbar to keep level
                //TODO Define multiplier that keeps this level- currently .1 below - may need to change direction- below removed since stays level
                //myArm.setArmPower(fourbarA*-0.1*.1);
            } else {
                myFourbar.setFourbarPower(0.0);
//                myArm.setArmPower(0.0);
            }

//            armPosCurrent = myArm.getArmPosition();
//            double armUpDown = gamepad2.right_stick_y;
//            if (armUpDown > 0.2 || armUpDown < -0.2) {
//                myArm.setArmPower(armUpDown * armUpMulti);
//            } else {
//                myArm.setArmPower(0.0);
//            }
            //fine tune arm up
            //armPosCurrent = myArm.getArmPosition();
/*            boolean armUP = gamepad2.dpad_up;
            boolean armDOWN = gamepad2.dpad_down;
            //TODO add max arm position
            if (armUP) { //(((armposcurrent < 100) && armUP)) {
                //TODO setting arm power to .05 since this is fine tune and dpad value is always 1 - adjust if needed
                armtargetPosition = 0;
                myArm.robotHardware.armmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                myArm.setArmPower(1.0* armUpDownPower);
            } else if (armDOWN) { //((armposcurrent > 100) && armDOWN) {  //fine tune arm down
                // TODO setting arm power to .05 since this is fine tune and dpad value is always 1 - adjust if needed
                armtargetPosition = 0;
                myArm.robotHardware.armmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                myArm.setArmPower(-0.5* armUpDownPower);
            //} else {
            //  myArm.setArmPower(0.0);
            } else if (armtargetPosition == 0) {
                armtargetPosition = myArm.getArmPosition();
                myArm.robotHardware.armmotor.setTargetPosition(armtargetPosition);
                myArm.robotHardware.armmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                myArm.robotHardware.armmotor.setPower(.25);
            }*/

            // arm updown
            armUpDownPosCurrent = myArm.getArmUpDownPosition();
            armUpDownAtarget = myArm.getArmUpDownPosition(); // sets this initially
            boolean armUP = gamepad2.dpad_up;
            boolean armDOWN = gamepad2.dpad_down;
            if (armUP) {
                armUpDownAtarget = (armUpDownPosCurrent + .0008);
                myArm.setArmUpDownPosition(armUpDownAtarget);
                armUP = !armUP;
                }
            else if (armDOWN) {
                armUpDownAtarget = (armUpDownPosCurrent - .0008);
                myArm.setArmUpDownPosition(armUpDownAtarget);
                armDOWN = !armDOWN;
            }

            // arm rotate
            armRotPosCurrent = myArm.getArmRotPosition();
            armrotAtarget = myArm.getArmRotPosition(); // sets this initially
            if (gamepad2.right_stick_x > .02 || gamepad2.right_stick_x < -.02) {
                double armrotA = gamepad2.right_stick_x;
                if (armrotA > .02) {
                    armrotAtarget = (armRotPosCurrent + .00008);
                    myArm.setArmRotPosition(armrotAtarget);
                }else if (armrotA < -.02) {
                    armrotAtarget = (armRotPosCurrent - .00008);
                    myArm.setArmRotPosition(armrotAtarget);
                }
            }

            // Pickup
            intakePosCurrent = myArm.getIntakePosition();
            intakeAtarget = myArm.getIntakePosition(); // sets this initially
            if (gamepad2.left_trigger > .01) {
                intakeAtarget = (intakePosCurrent + .0008);
                myArm.setIntakePosition(intakeAtarget);
            }
            else if (gamepad2.right_trigger >.01) {
                intakeAtarget = (intakePosCurrent - .0008);
                myArm.setIntakePosition(intakeAtarget);
            }

            // Extend
            extendPosCurrent = myArm.getExtendPosition();
            extendAtarget = myArm.getExtendPosition(); // sets this initially
            if (gamepad1.left_trigger > .01) {
                extendAtarget = (extendPosCurrent + .0008);
                myArm.setExtendPosition(extendAtarget);
            }
            else if (gamepad1.right_trigger >.01) {
                extendAtarget = (extendPosCurrent - .0008);
                myArm.setExtendPosition(extendAtarget);
            }

            //Grab
            grabPosCurrent = myArm.getGrabPosition();
            grabAtarget = myArm.getGrabPosition(); // sets this initially
            boolean grab = gamepad1.left_bumper;
            boolean release = gamepad1.right_bumper;
            if (grab) {
                grabAtarget = (grabPosCurrent + .0008);
                myArm.setGrabPosition(grabAtarget);
            }
            else if (release) {
                grabAtarget = (grabPosCurrent - .0008);
                myArm.setGrabPosition(grabAtarget);
            }




            //TODO change multiplier below to impact how fast it moves - may need to add pause or timer to slow down???

            /*float pickupclosednum = gamepad2.left_trigger;
            //Close Pickup
            if (pickupclosednum >=0.05) {
                myPickup.setPickupPosition(myPickup.pickupclosed);
            }

            float pickupopennum = gamepad2.right_trigger;
            //Open Pickup
            if (pickupopennum >=0.05) {
                myPickup.setPickupPosition(myPickup.pickupopen);
            }
*/
        // End Gamepad 2

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

    // Threaded Gamepad 1. Everything Gamepad 1 will happen below.
    public void run() {
        try {
            CDDriveChassis myChassis = new CDDriveChassis(myHardware);
            while (opModeIsActive()) {
            // Everything gamepad 1:
                // User controls for the robot speed overall
                //if (gamepad1.left_trigger != 0) {
                if (gamepad1.y) {
                        robotSpeed = baseSpeed * 1.0;
                    //} else if (gamepad1.right_trigger != 0) {
                    } else if (gamepad1.x) {
                        robotSpeed = baseSpeed * .4;
                    } else {
                        robotSpeed = baseSpeed;
                    }

                if (gamepad1.a) {
                    //Button A = unconstrained movement
                    constrainMovement = false;
                }
                if (gamepad1.b) {
                    //Button B = constrained movement
                    constrainMovement = true;
                    // Flip the boolean to toggle modes for drive constraints
                    //constrainMovement = !constrainMovement;
                }
                // We cubed the inputs to make the inputs more responsive
                y = Math.pow(gamepad1.left_stick_y, 3); // Remember, this is reversed!
                x = Math.pow(gamepad1.left_stick_x * -1.1, 3); // Counteract imperfect strafing
                rx = Math.pow(gamepad1.right_stick_x, 3) * 0.5;  //Reduced turn speed to make it easier to control

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                if (constrainMovement) {
                   if (Math.abs(x)>Math.abs(y)) {
                       leftFrontPower = (x - rx) / denominator;
                       leftRearPower = (-x - rx) / denominator;
                       rightFrontPower = (-x + rx) / denominator;
                       rightRearPower = (x + rx) / denominator;
                   }
                   if (Math.abs(y)>=Math.abs(x)) {
                       leftFrontPower = (y - rx) / denominator;
                       leftRearPower = (y - rx) / denominator;
                       rightFrontPower = (y + rx) / denominator;
                       rightRearPower = (y + rx) / denominator;
                   }
                } else {
                    leftFrontPower = (y + x - rx) / denominator;
                    leftRearPower = (y - x - rx) / denominator;
                    rightFrontPower = (y - x + rx) / denominator;
                    rightRearPower = (y + x + rx) / denominator;
                }
                //move robot - drive chassis
                myChassis.setLeftFrontPower(leftFrontPower * robotSpeed);
                myChassis.setLeftRearPower(leftRearPower * robotSpeed);
                myChassis.setRightFrontPower(rightFrontPower * robotSpeed);
                myChassis.setRightRearPower(rightRearPower * robotSpeed);
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
            telemetry.clearAll(); // Just doing something to satisfy the placeholder.
////             At the beginning of each telemetry update, grab a bunch of data
////             from the IMU that we will then display in separate lines.
////            Comment block disables telemetry reporting!
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
            telemetry.addData("FourbarPotCurrent", "%.2f", fourBarPotCurrent);
            telemetry.addData("CurrFourbarThreshold", "%.2f", currentFourBarThreshold);
            telemetry.addData("fourbarerror", fourBarError);
            //telemetry.addData("CurrArmThresh", "%.2f", armCurrentThreshold);
            //telemetry.addData("CurrArmDownThresh", "%.2f", armDownThresh);
            //telemetry.addData("ArmPosition", armPosCurrent);
            //telemetry.addData("armerror", armError);
            telemetry.addData("ArmUpDownPosition", (armUpDownPosCurrent));
            telemetry.addData("ArmUpDownTarget", (armUpDownAtarget));
            telemetry.addData("ArmRotPosition", armRotPosCurrent);
            telemetry.addData("ArmRotTarget", armrotAtarget);
            telemetry.addData("IntakePosition", (intakePosCurrent));
            telemetry.addData("IntakeTarget", (intakeAtarget));
            telemetry.addData("ExtendPosition", (extendPosCurrent));
            telemetry.addData("ExtendTarget", (extendAtarget));
            telemetry.addData("GrabPosition", (grabPosCurrent));
            telemetry.addData("GrabTarget", (grabAtarget));
            //telemetry.addData("PickupPosition", pickUpPosCurrent);
        }
        // Loop and update the dashboard
        telemetry.update();
    }
    //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------

//    String formatAngle(AngleUnit angleUnit, double angle) {
//        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
//    }
//
//    String formatDegrees(double degrees){
//        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
//    }
}
