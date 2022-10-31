package org.firstinspires.ftc.teamcode;

// There are work in progress / untested IMU elements in this code which we may want to use so they are preserved.
//import com.qualcomm.hardware.bosch.BNO055IMU;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.sun.tools.javac.comp.Check;

// Telemetry

//
// import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import java.util.Locale;


@TeleOp(name = "CDTeleop", group = "Linear Opmode")
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
    public double armClearToRotatePositionWithCone;
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

    // Pickup variables
    public double pickupPositionCurrent;
    public double pickupTarget;
    // Extend variables
    public double extendPosCurrent;
    public double extendAtarget;
    // Grab variables
    public double grabPosCurrent;
    public double grabAtarget;

    public CDHardware robotHardware;

    // State used for updating telemetry
    //    public Orientation angles;
    //    public Acceleration gravity;
    //    public BNO055IMU imu;

    @Override
    public void runOpMode() {
        // Initialize our classes to variables
        robotHardware = new CDHardware(hardwareMap);
        CDFourBar fourBar = new CDFourBar(robotHardware);
        CDArm arm = new CDArm(robotHardware);
        CDPickup pickup = new CDPickup(robotHardware);
        CDGrabber grabber = new CDGrabber(robotHardware);

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
            currentFourBarPosition = fourBar.getFourBarPosition(); //Variable Based
            fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
            currentFourBarThreshold = fourBar.getFourBarCurrentThreshold();

/*
            //Refresh the armpostion and report threshold
            armPosCurrent = arm.getArmPosition();
            armCurrentThreshold = arm.armCurrentThreshold;
*/

            double fourbarA = gamepad2.left_stick_y;
            //Slow at top and bottom
            //TODO put in proper values for fourbarpot current near top and bottom so it slows down
            if ((fourBarPotCurrent > 3 && fourbarA < -0.01) || (fourBarPotCurrent < .25 && fourbarA >= 0.01)) {
                fourBar.setFourBarPower(fourbarA * -.5);
                fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
                //move arm proportionally to fourbar to keep level
                //TODO Define multiplier that keeps this level- currently .1 below- may need to change direction- below removed since stays level
                //arm.setArmPower(fourbarA*-.05*.1);
            } else if (fourbarA >= 0.01 || fourbarA <= -0.01) {
                //TODO check direction on gamepad - remove *-1 below and - on .5 above if direction is wrong
                fourBar.setFourBarPower(fourbarA * -1);//Remember on controller -y is up
                fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
                //move arm proportionally to fourbar to keep level
                //TODO Define multiplier that keeps this level- currently .1 below - may need to change direction- below removed since stays level
                //arm.setArmPower(fourbarA*-0.1*.1);
            } else {
                fourBar.setFourBarPower(0.0);
                fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
//                arm.setArmPower(0.0);
            }

            //arm vertical
            armUpDownPosCurrent = arm.getArmVerticalPosition();
            armUpDownAtarget = arm.getArmVerticalPosition(); // sets this initially
            armRotPosCurrent = arm.getArmRotationPosition();
            armrotAtarget = arm.getArmRotationPosition(); // sets this initially

            boolean armUP = gamepad2.dpad_up;
            boolean armDOWN = gamepad2.dpad_down;

            // Flip controls if the arm is rotated forward
            if (armRotPosCurrent < 0.56) {
                armUP = gamepad2.dpad_down;
                armDOWN = gamepad2.dpad_up;
            }

            if (armUP) {
                armUpDownAtarget = (armUpDownPosCurrent + .0008);
                arm.setArmVerticalPosition(armUpDownAtarget);
            } else if (armDOWN) {
                armUpDownAtarget = (armUpDownPosCurrent - .0008);
                arm.setArmVerticalPosition(armUpDownAtarget);
            }

            // arm rotate
            if (gamepad2.right_stick_x > .02 || gamepad2.right_stick_x < -.02) {
                double armrotA = gamepad2.right_stick_x;
                if (armrotA > .02) {
                    armrotAtarget = (armRotPosCurrent + .001);
                    arm.setArmRotationPosition(armrotAtarget);
                } else if (armrotA < -.02) {
                    armrotAtarget = (armRotPosCurrent - .001);
                    arm.setArmRotationPosition(armrotAtarget);
                }
            }

            // Pickup
            pickupPositionCurrent = pickup.getServoPosition();
            pickupTarget = pickup.getServoPosition(); // sets this initially
            if (gamepad2.left_trigger > .01) {
                pickupTarget = pickupPositionCurrent + .0008;
                pickup.setServoPosition(pickupTarget);
            } else if (gamepad2.right_trigger > .01) {
                pickupTarget = pickupPositionCurrent - .0008;
                pickup.setServoPosition(pickupTarget);
            }

            // Extend
            extendPosCurrent = grabber.getExtendPosition();
            extendAtarget = grabber.getExtendPosition(); // sets this initially
            if (gamepad1.left_trigger > .01) {
                extendAtarget = (extendPosCurrent + .0008);
                grabber.setExtendPosition(extendAtarget);
            } else if (gamepad1.right_trigger > .01) {
                extendAtarget = (extendPosCurrent - .0008);
                grabber.setExtendPosition(extendAtarget);
            }

            //Grab
            grabPosCurrent = grabber.getGrabPosition();
            grabAtarget = grabber.getGrabPosition(); // sets this initially
            boolean grab = gamepad1.left_bumper;
            boolean release = gamepad1.right_bumper;
            if (grab) {
                grabAtarget = (grabPosCurrent + .0008);
                grabber.setGrabPosition(grabAtarget);
            } else if (release) {
                grabAtarget = (grabPosCurrent - .0008);
                grabber.setGrabPosition(grabAtarget);
            }

            //Go HOME (Back pickup position between fourbars)
            if (gamepad2.a) {
                double armRotPositionHOME = .343;
                double armVerticalPositionHOME = .565;
                double fourbarPositionHOME = .25;
                double fourbarPositiontoRotateHOME = .8;

                // Check if arm clear to rotate

                //check if arm needs to rotate and fourbar is high enough - fourbar should be above .8 for arm rotation here
                if (fourBarPotCurrent < fourbarPositiontoRotateHOME && (arm.getArmRotationPosition() < (armRotPositionHOME - .01)) || (arm.getArmRotationPosition() > (armRotPositionHOME + .01))) {
                    fourBar.setFourbarPosition(fourbarPositiontoRotateHOME, false);
//                    while (fourBar.robotHardware.fourBarMotor.isBusy()) {
//                        sleep(50);
//                    }
                    fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
                    while (fourBarPotCurrent < fourbarPositiontoRotateHOME) {
                        fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts();
                        composeTelemetry(imuTelemetry);
                    }
                    //TODO add .6 Fourbar postion if no cone for rotation - Use pickup value to determine
                    armClearToRotatePositionWithCone = (.87 * fourBarPotCurrent - .14);
                    arm.setArmVerticalPosition(arm.armClearToRotatePositionWithCone);
                    //TODO add checks and remove sleep below - remember getservo positions gets the last set position not the current position
                    arm.setArmRotationPosition(armRotPositionHOME);
                }
                //check if fourbar motor busy
//                boolean fourbarBusyCheck = fourBar.robotHardware.fourBarMotor.isBusy();
//                if (!fourbarBusyCheck) {
//                    arm.setArmVerticalPosition(armVerticalPositionHOME);
//                }
                //Double check before moving down
                if (arm.getArmRotationPosition() >= .333 && arm.getArmRotationPosition() <= .353 && arm.getArmVerticalPosition() >= .555 && arm.getArmVerticalPosition() <= .575) {
                    fourBar.setFourbarPosition(fourbarPositionHOME, false);
//                    while (fourBar.robotHardware.fourBarMotor.isBusy()) {
//                        sleep(50);
//                    }
                    fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
                    while (fourBarPotCurrent < fourbarPositiontoRotateHOME) {
                        fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts();
                        composeTelemetry(imuTelemetry);
                    }
                }
            }
            //Go FRONT Medium with cone
            if (gamepad2.y) {
                double armRotPositionFRONT = .82;
                double armVerticalPositionFRONT = .565;
                double fourbarPositionFRONTMED = .8;
                double fourbarPositiontoRotateHOME = .8;
                double armRotPositionHOME = .343;
                double armVerticalPositionHOME = .565;

                //Check if arm inside fourbar and set arm to vert HOME position
                if (fourBarPotCurrent < fourbarPositiontoRotateHOME && ((arm.getArmRotationPosition() > (armRotPositionHOME - .02)) && (arm.getArmRotationPosition() < (armRotPositionFRONT + .02)))) {
                    //set vertical arm position to HOME to ensure clears
                    arm.setArmRotationPosition(armRotPositionHOME);
                    arm.setArmVerticalPosition(armVerticalPositionHOME);
                }
                //check if arm needs to rotate and fourbar is high enough - fourbar should be above .8 for arm rotation here
                if (fourBarPotCurrent < fourbarPositiontoRotateHOME && ((arm.getArmRotationPosition() < (armRotPositionFRONT - .02)) && (arm.getArmRotationPosition() > (armRotPositionFRONT + .02)))) {
                    fourBar.setFourbarPosition(fourbarPositiontoRotateHOME, false);
                    fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
                    while (fourBarPotCurrent < fourbarPositiontoRotateHOME) {
                        fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts();
                        composeTelemetry(imuTelemetry);
                    }
                    //TODO add .6 Fourbar postion if no cone for rotation - Use pickup value to determine
                    armClearToRotatePositionWithCone = (.87 * fourBarPotCurrent - .14);
                    arm.setArmVerticalPosition(arm.armClearToRotatePositionWithCone);
                    //TODO add checks and remove sleep below - remember getservo positions gets the last set position not the current position
                }
                arm.setArmRotationPosition(armRotPositionFRONT);
                arm.setArmVerticalPosition(armVerticalPositionFRONT);
                fourBar.setFourbarPosition(fourbarPositionFRONTMED, false);
                fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
                while (fourBarPotCurrent < fourbarPositiontoRotateHOME) {
                    fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts();
                    composeTelemetry(imuTelemetry);
                }
            }

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
            CDDriveChassis myChassis = new CDDriveChassis(robotHardware);
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
                    if (Math.abs(x) > Math.abs(y)) {
                        leftFrontPower = (x - rx) / denominator;
                        leftRearPower = (-x - rx) / denominator;
                        rightFrontPower = (-x + rx) / denominator;
                        rightRearPower = (x + rx) / denominator;
                    }
                    if (Math.abs(y) >= Math.abs(x)) {
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
            telemetry.addData("PickupPosition", (pickupPositionCurrent));
            telemetry.addData("PickupTarget", (pickupTarget));
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
