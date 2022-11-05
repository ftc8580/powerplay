package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.button.GamepadButton;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;
import com.arcrobotics.ftclib.gamepad.GamepadEx;
import com.arcrobotics.ftclib.gamepad.GamepadKeys;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.commandgroups.MoveToDeliver;
import org.firstinspires.ftc.teamcode.commandgroups.MoveToHome;
import org.firstinspires.ftc.teamcode.commands.ArmMoveVertical;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDGrabber;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;
import org.firstinspires.ftc.teamcode.util.CDTelemetry;

@TeleOp(name = "CDTeleop", group = "Linear Opmode")
public class CDTeleop extends LinearOpMode implements Runnable {
    // Static variables for tuning
    private static final double INVERT_ARM_LIMIT = 0.63;
    private static final double ARM_ROTATION_MOVE_SPEED = 0.01;
    private static final double ARM_VERTICAL_MOVE_SPEED = 0.003;
    private static final double PICKUP_MOVE_SPEED = 0.02;
    private static final double GRABBER_EXTEND_MOVE_SPEED = 0.02;
    private static final double GRABBER_GRAB_MOVE_SPEED = 0.02;

    // Initialize our teleopThread
    private Thread teleopGamepad1Thread;
    // Initialize our local variables with values
    // The baseSpeed "slow" variable is used to control the overall speed of the robot
    // TODO: Work with Drive Team to determine
    public double baseSpeed = 0.50;

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
    public double currentFourBarThreshold;
    public double fourBarPotCurrent;
    public boolean fourBarError;
    //Arm UpDown variables
    public double armVerticalPositionCurrent;
    public double armUpDownAtarget;

    //ArmRot variables
    public double armRotationPosition;
    public double armrotAtarget;

    // Pickup variables
    public double pickupPositionCurrent;
    public double pickupTarget;
    // Extend variables
    public double extendPosCurrent;
    public double extendAtarget;
    // Grab variables
    public double grabPosCurrent;
    public double grabAtarget;

    public Telemetry robotTelemetry;

    public CDHardware robotHardware;
    public CDFourBar fourBar;
    public CDArm arm;
    public CDPickup pickup;
    public CDGrabber grabber;

    private GamepadEx chassisOp;
    private GamepadEx fourBarOp;

    GamepadButton armUpButton;
    GamepadButton armDownButton;
    GamepadButton homeButton;
    GamepadButton deliverButton;

    public CDTeleop() {
        super();

        // Initialize telemetry
        robotTelemetry = CDTelemetry.initialize(telemetry);

        // Initialize controllers
        chassisOp = new GamepadEx(gamepad1);
        fourBarOp = new GamepadEx(gamepad2);

        // Chassis Buttons
        // TODO

        // Four Bar Buttons
        armUpButton = fourBarOp.getGamepadButton(GamepadKeys.Button.DPAD_UP);
        armDownButton = fourBarOp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN);
        homeButton = fourBarOp.getGamepadButton(GamepadKeys.Button.A);
        deliverButton = fourBarOp.getGamepadButton(GamepadKeys.Button.Y);

        // Initialize our classes to variables
        robotHardware = new CDHardware(hardwareMap);
        fourBar = new CDFourBar(robotHardware);
        arm = new CDArm(robotHardware);
        pickup = new CDPickup(robotHardware);
        grabber = new CDGrabber(robotHardware);
    }

    @Override
    public void runOpMode() {
        // Configure initial variables
        // TODO: if we want pacman model to be default this should be set to true
        constrainMovement = false;

        //Wait for the driver to press PLAY on the driver station/phone
        robotTelemetry.addData("Status", "Fully Initialized");
        robotTelemetry.update();

        // Make a new thread
        teleopGamepad1Thread = new Thread(this); // Define teleopThread
        waitForStart();
        // Run until the end (Driver presses STOP)
        teleopGamepad1Thread.start(); // Start the teleopThread

        // Polling rate for logging gets set to zero before the while loop
        int i = 0;

        while (opModeIsActive()) {
            // Clear telemetry output on each loop
            robotTelemetry.clearAll();

            /******************************
             * GAMEPAD 2 CODE
             ******************************/

            // FOURBAR CODE
            if (fourBarError) {
                robotTelemetry.addLine("DANGER: THE FOURBAR VALUES AREN'T CHANGING!");
                robotTelemetry.update();
            }
            //Refresh the fourbarposition and report threshold
            fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts(); //Potentiometer voltage based
            currentFourBarThreshold = fourBar.getFourBarCurrentThreshold();
/*
            //Refresh the armpostion and report threshold
            armPosCurrent = arm.getArmPosition();
            armCurrentThreshold = arm.armCurrentThreshold;
*/
            double fourBarSpeed = fourBarOp.getLeftY();
            if (fourBarSpeed != 0) {
                fourBar.setFourBarPower(fourBarSpeed * -1);//Remember on controller -y is up
            } else {
                fourBar.setFourBarPower(0.0);
            }

            fourBarPotCurrent = fourBar.getFourBarPotentiometerVolts();

            //arm vertical
            armVerticalPositionCurrent = arm.getArmVerticalPosition();
            armUpDownAtarget = arm.getArmVerticalPosition(); // sets this initially
            armRotationPosition = arm.getArmRotationPosition();
            armrotAtarget = arm.getArmRotationPosition(); // sets this initially

            // Flip controls if the arm is rotated forward
            boolean invertVerticalPosition = armRotationPosition < INVERT_ARM_LIMIT;

            armUpButton.whenPressed(
                    new ArmMoveVertical(
                            arm,
                            armVerticalPositionCurrent + (invertVerticalPosition ? -ARM_VERTICAL_MOVE_SPEED : ARM_VERTICAL_MOVE_SPEED)
                    )
            );

            armDownButton.whenPressed(
                    new ArmMoveVertical(
                            arm,
                            armVerticalPositionCurrent + (invertVerticalPosition ? ARM_VERTICAL_MOVE_SPEED : -ARM_VERTICAL_MOVE_SPEED)
                    )
            );

            // arm rotate
            double rotationSpeed = fourBarOp.getRightX();
            if (rotationSpeed > 0 && arm.isArmClearToRotateFree(fourBar, true)) {
                arm.setArmRotationPosition(armRotationPosition + ARM_ROTATION_MOVE_SPEED);
            } else if (rotationSpeed < 0 && arm.isArmClearToRotateFree(fourBar, true)) {
                arm.setArmRotationPosition(armRotationPosition - ARM_ROTATION_MOVE_SPEED);
            }

            // Pickup
            double openPickupSpeed = fourBarOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
            double closePickupSpeed = fourBarOp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
            pickupPositionCurrent = pickup.getServoPosition();
            if (openPickupSpeed > 0) {
                pickup.setServoPosition(pickupPositionCurrent + PICKUP_MOVE_SPEED);
            } else if (closePickupSpeed > 0) {
                pickup.setServoPosition(pickupPositionCurrent - PICKUP_MOVE_SPEED);
            }

            // Extend
            double extendGrabber = chassisOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
            double retractGrabber = chassisOp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
            extendPosCurrent = grabber.getExtendPosition();
            if (extendGrabber > 0) {
                grabber.setExtendPosition(extendPosCurrent + GRABBER_EXTEND_MOVE_SPEED);
            } else if (retractGrabber > 0) {
                grabber.setExtendPosition(extendPosCurrent - GRABBER_EXTEND_MOVE_SPEED);
            }

            //Grab
            boolean closeGrabber = chassisOp.getButton(GamepadKeys.Button.LEFT_BUMPER);
            boolean openGrabber = chassisOp.getButton(GamepadKeys.Button.RIGHT_BUMPER);
            grabPosCurrent = grabber.getGrabPosition();
            if (closeGrabber) {
                grabber.setGrabPosition(grabPosCurrent + GRABBER_GRAB_MOVE_SPEED);
            } else if (openGrabber) {
                grabber.setGrabPosition(grabPosCurrent - GRABBER_GRAB_MOVE_SPEED);
            }

            //Go HOME (Back pickup position between fourbars)
            homeButton.whenPressed(new MoveToHome(fourBar, arm, pickup));
            deliverButton.whenPressed(new MoveToDeliver(fourBar, arm, pickup));

            // End Gamepad 2

            // Telemetry Stuff -
            // need to slow down the logging
            if (i == 10) {
                robotTelemetry.update();
                i = 0;
            } else {
                i++;
            }
        }
    }

    // Threaded Gamepad 1. Everything Gamepad 1 will happen below.
    public void run() {
        // Drive Chassis
        MecanumDrive drive = new MecanumDrive(
                new Motor(hardwareMap, "motorLF"),
                new Motor(hardwareMap, "motorRF"),
                new Motor(hardwareMap, "motorLR"),
                new Motor(hardwareMap, "motorRR")
        );

        try {
            while (opModeIsActive()) {
                // Everything gamepad 1:
                // User controls for the robot speed overall
                // TODO: The else overrides the robot speed
                if (chassisOp.getButton(GamepadKeys.Button.Y)) {
                    robotSpeed = baseSpeed * 1.5;
                } else if (chassisOp.getButton(GamepadKeys.Button.X)) {
                    robotSpeed = baseSpeed * .4;
                } else {
                    robotSpeed = baseSpeed;
                }

                if (chassisOp.getButton(GamepadKeys.Button.A)) {
                    //Button A = unconstrained movement
                    constrainMovement = false;
                }
                if (chassisOp.getButton(GamepadKeys.Button.B)) {
                    //Button B = constrained movement
                    constrainMovement = true;
                    // Flip the boolean to toggle modes for drive constraints
                    //constrainMovement = !constrainMovement;
                }

                double strafeSpeed = chassisOp.getLeftX() * robotSpeed;
                double forwardSpeed = chassisOp.getLeftY() * robotSpeed;
                double turnSpeed = chassisOp.getRightX() * robotSpeed;

                if (constrainMovement) {
                    if (Math.abs(strafeSpeed) > Math.abs(forwardSpeed)) {
                        forwardSpeed = 0;
                    } else {
                        strafeSpeed = 0;
                    }
                }

                drive.driveRobotCentric(
                        strafeSpeed,
                        forwardSpeed,
                        turnSpeed,
                        true
                );
            }
            // End gamepad 1
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void composeTelemetry(boolean imuTelemetry) {
        // Clear previous ouput
        telemetry.clearAll();

        // Add data to telemetry
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
        double fourbarPositiontoRotateHOME = .8;
        telemetry.addData("FourBarPotUnderHome", (fourBarPotCurrent < fourbarPositiontoRotateHOME));
        //telemetry.addData("CurrArmThresh", "%.2f", armCurrentThreshold);
        //telemetry.addData("CurrArmDownThresh", "%.2f", armDownThresh);
        //telemetry.addData("ArmPosition", armPosCurrent);
        //telemetry.addData("armerror", armError);
        telemetry.addData("ArmUpDownPosition", (armVerticalPositionCurrent));
        telemetry.addData("ArmUpDownTarget", (armUpDownAtarget));
        telemetry.addData("ArmRotPosition", armRotationPosition);
        telemetry.addData("ArmRotTarget", armrotAtarget);
        telemetry.addData("PickupPosition", (pickupPositionCurrent));
        telemetry.addData("PickupTarget", (pickupTarget));
        telemetry.addData("ExtendPosition", (extendPosCurrent));
        telemetry.addData("ExtendTarget", (extendAtarget));
        telemetry.addData("GrabPosition", (grabPosCurrent));
        telemetry.addData("GrabTarget", (grabAtarget));
        //telemetry.addData("PickupPosition", pickUpPosCurrent);

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
