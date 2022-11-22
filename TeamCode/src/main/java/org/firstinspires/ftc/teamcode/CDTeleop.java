package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandScheduler;
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
import org.firstinspires.ftc.teamcode.commandgroups.PickupCone;
import org.firstinspires.ftc.teamcode.commands.FourBarSetPosition;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDGrabber;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;
import org.firstinspires.ftc.teamcode.util.CDTelemetry;
import org.firstinspires.ftc.teamcode.util.MathUtils;

@TeleOp(name = "CDTeleop", group = "Linear Opmode")
public class CDTeleop extends LinearOpMode implements Runnable {
    // Static variables for tuning
    private static final double INVERT_ARM_LIMIT = 0.63;
    private static final double ARM_ROTATION_MOVE_SPEED = 0.006;
    private static final double ARM_VERTICAL_MOVE_SPEED = 0.016;
    private static final double GRABBER_EXTEND_MOVE_SPEED = 0.25;
    private static final double GRABBER_GRAB_MOVE_SPEED = 0.30;

    // Initialize our teleopThread
    private Thread teleopGamepad1Thread;
    // Initialize our local variables with values
    // The baseSpeed "slow" variable is used to control the overall speed of the robot
    public double baseSpeed = 0.75;

    // Initialize our local variables for use later in telemetry or other methods
    //Drive variables
    public double forwardSpeed;
    public double strafeSpeed;
    public double turnSpeed;
    public double robotSpeed;
    public boolean constrainMovement;
    //Fourbar variables
    public double fourBarPosition;
    //Arm UpDown variables
    public double armVerticalPosition;
    public double armVerticalTarget;

    //ArmRot variables
    public double armRotationPosition;
    public double armRotationTarget;

    // Pickup variables
    public double pickupPosition;
    public double pickupTarget;
    // Extend variables
    public double extendPosition;
    public double extendTarget;
    // Grab variables
    public double grabPosition;
    public double grabTarget;

    public Telemetry robotTelemetry;

    public CDHardware robotHardware;
    public CDFourBar fourBar;
    public CDArm arm;
    public CDPickup pickup;
    public CDGrabber grabber;

    private GamepadEx chassisOp;
    private GamepadEx fourBarOp;

    // Chassis Buttons
    GamepadButton highSpeedButton;
    GamepadButton lowSpeedButton;
    GamepadButton disablePacmanButton;
    GamepadButton enablePacmanButton;

    // Four Bar Buttons
    GamepadButton armUpButton;
    GamepadButton armDownButton;
    GamepadButton homeButton;
    GamepadButton deliverButton;
    GamepadButton deliveryArmLeftButton;
    GamepadButton deliveryArmRightButton;
    GamepadButton fourBarLowButton;
    GamepadButton fourBarMediumButton;
    GamepadButton fourBarHighButton;

    @Override
    public void runOpMode() {
        CommandScheduler.getInstance().reset();

        // Set up hardware
        // Initialize telemetry
        robotTelemetry = CDTelemetry.initialize(telemetry);

        // Initialize controllers
        chassisOp = new GamepadEx(gamepad1);
        fourBarOp = new GamepadEx(gamepad2);

        // Chassis Buttons
        highSpeedButton = chassisOp.getGamepadButton(GamepadKeys.Button.Y);
        lowSpeedButton = chassisOp.getGamepadButton(GamepadKeys.Button.X);
        disablePacmanButton = chassisOp.getGamepadButton(GamepadKeys.Button.A);
        enablePacmanButton = chassisOp.getGamepadButton(GamepadKeys.Button.B);

        // Four Bar Buttons
        armUpButton = fourBarOp.getGamepadButton(GamepadKeys.Button.DPAD_UP);
        armDownButton = fourBarOp.getGamepadButton(GamepadKeys.Button.DPAD_DOWN);
        deliveryArmLeftButton = fourBarOp.getGamepadButton(GamepadKeys.Button.LEFT_BUMPER);
        deliveryArmRightButton = fourBarOp.getGamepadButton(GamepadKeys.Button.RIGHT_BUMPER);
        homeButton = fourBarOp.getGamepadButton(GamepadKeys.Button.A);
        fourBarLowButton = fourBarOp.getGamepadButton(GamepadKeys.Button.X);
        fourBarMediumButton = fourBarOp.getGamepadButton(GamepadKeys.Button.B);
        fourBarHighButton = fourBarOp.getGamepadButton(GamepadKeys.Button.Y);
        // TODO: Make sure these stick buttons are correct
        deliverButton = fourBarOp.getGamepadButton(GamepadKeys.Button.RIGHT_STICK_BUTTON);

        // Initialize our classes to variables
        robotHardware = new CDHardware(hardwareMap);
        grabber = new CDGrabber(robotHardware);
        arm = new CDArm(robotHardware);
        pickup = new CDPickup(robotHardware);
        fourBar = new CDFourBar(hardwareMap);

        // Commands
        FourBarSetPosition fourBarSetLowCommand = new FourBarSetPosition(fourBar, "low");
        FourBarSetPosition fourBarSetMediumCommand = new FourBarSetPosition(fourBar, "medium");
        FourBarSetPosition fourBarSetHighCommand = new FourBarSetPosition(fourBar, "high");
        MoveToHome homeCommand = new MoveToHome(fourBar, arm, pickup);
        MoveToDeliver unicornCommand = new MoveToDeliver(fourBar, arm, pickup);

        // Configure initial variables
        // TODO: if we want pacman model to be default this should be set to true
        constrainMovement = true;

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

        CommandScheduler.getInstance().enable();

        while (opModeIsActive()) {
            /******************************
             * GAMEPAD 2 CODE
             ******************************/

            // FOURBAR CODE
            fourBarPosition = fourBar.getFourBarPosition();

            double fourBarSpeed = fourBarOp.getLeftY();
            // TODO: Try scheduling arm adjustment only if in unsafe range, otherwise just set power
            if ((fourBarSpeed <= -0.1) || (fourBarSpeed >= 0.1)) {
                fourBar.setFourBarPower(fourBarSpeed);//Remember on controller -y is up
            } else if (
                    !fourBarSetLowCommand.isExecuting &&
                    !fourBarSetMediumCommand.isExecuting &&
                    !fourBarSetHighCommand.isExecuting &&
                    !homeCommand.isExecuting &&
                    !unicornCommand.isExecuting
            )
            {
                fourBar.setFourBarPower(0.0);
            }
//            if (fourBar.isOutOfRange()) {
//                fourBar.setFourBarPosition(MathUtils.clampDouble(CDFourBar.ABSOLUTE_LOWER_BOUND_VOLTS, CDFourBar.ABSOLUTE_UPPER_BOUND_VOLTS, fourBar.getFourBarPosition()));
//            }

            // TODO: Arm vertical & rotation positions
            // B: Set four bar high, arm vertical to 0.31
            // X: Set four bar back home
            // Y: Set four bar medium, arm vertical to 0.415 (home)
            // A: Set four bar low, arm vertical to 0.415 (home)
            // (Y stick) Unicorn: arm rotation 0.84, arm vertical 0.62, four bar 0.96

            fourBarLowButton.whenPressed(fourBarSetLowCommand);
            fourBarMediumButton.whenPressed(fourBarSetMediumCommand);
            fourBarHighButton.whenPressed(fourBarSetHighCommand);

            //arm vertical
            armVerticalPosition = arm.getArmVerticalPosition();
            armVerticalTarget = arm.getArmVerticalPosition(); // sets this initially
            armRotationPosition = arm.getArmRotationPosition();
            armRotationTarget = arm.getArmRotationPosition(); // sets this initially

            // Flip controls if the arm is rotated forward
            boolean invertVerticalPosition = armRotationPosition < INVERT_ARM_LIMIT;

            if (armUpButton.get()) {
                arm.setArmVerticalPosition(
                        armVerticalPosition + (invertVerticalPosition ? -ARM_VERTICAL_MOVE_SPEED : ARM_VERTICAL_MOVE_SPEED)
                );
            } else if (armDownButton.get()) {
                arm.setArmVerticalPosition(
                        armVerticalPosition + (invertVerticalPosition ? ARM_VERTICAL_MOVE_SPEED : -ARM_VERTICAL_MOVE_SPEED)
                );
            }

            // arm rotate
            double rotationSpeed = fourBarOp.getRightX();
            boolean armClearToRotate = arm.isArmClearToRotateFree(fourBar, pickup.isPickupClosed);
            if (rotationSpeed > 0.2 && armClearToRotate) {
                arm.setArmRotationPosition(armRotationPosition + ARM_ROTATION_MOVE_SPEED);
            } else if (rotationSpeed < -0.2 && armClearToRotate) {
                arm.setArmRotationPosition(armRotationPosition - ARM_ROTATION_MOVE_SPEED);
            }

            if (deliveryArmLeftButton.get()) {
                arm.setArmDeliveryLeft();
            } else if (deliveryArmRightButton.get()) {
                arm.setArmDeliveryRight();
            }

            // Pickup
            double openPickupSpeed = fourBarOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
            double closePickupSpeed = fourBarOp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
            pickupPosition = pickup.getServoPosition();
            if (openPickupSpeed > 0.2) {
                pickupTarget = CDPickup.OPEN_POSITION;
                pickup.release();
            } else if (closePickupSpeed > 0.2) {
                if (!pickup.isPickupClosed) {
                    pickupTarget = CDPickup.CLOSED_POSITION;
                    new PickupCone(arm, pickup, fourBar).schedule();
                }
            }

            // Extend
            double extendGrabber = chassisOp.getTrigger(GamepadKeys.Trigger.LEFT_TRIGGER);
            double retractGrabber = chassisOp.getTrigger(GamepadKeys.Trigger.RIGHT_TRIGGER);
            extendPosition = grabber.getExtendPosition();
            if (extendGrabber > 0) {
                extendTarget = extendPosition + GRABBER_EXTEND_MOVE_SPEED;
                grabber.setExtendPosition(extendTarget);
            } else if (retractGrabber > 0) {
                extendTarget = extendPosition - GRABBER_EXTEND_MOVE_SPEED;
                grabber.setExtendPosition(extendTarget);
            }

            //Grab
            boolean closeGrabber = chassisOp.getButton(GamepadKeys.Button.RIGHT_BUMPER);
            boolean openGrabber = chassisOp.getButton(GamepadKeys.Button.LEFT_BUMPER);
            grabPosition = grabber.getGrabPosition();
            if (closeGrabber) {
                grabTarget = grabPosition + GRABBER_GRAB_MOVE_SPEED;
                grabber.setGrabPosition(grabTarget);
            } else if (openGrabber) {
                grabTarget = grabPosition - GRABBER_GRAB_MOVE_SPEED;
                grabber.setGrabPosition(grabTarget);
            }

            //Go HOME (Back pickup position between fourbars)
            homeButton.whenPressed(homeCommand);
            deliverButton.whenPressed(unicornCommand);

            // To handle whileHeld and whenPressed conditions
            CommandScheduler.getInstance().run();

            // End Gamepad 2

            // Telemetry Stuff -
            // need to slow down the logging
            if (i == 10) {
                composeTelemetry();
                i = 0;
            } else {
                i++;
            }
        }

        CommandScheduler.getInstance().reset();
        CommandScheduler.getInstance().disable();
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
                if (highSpeedButton.get()) {
                    robotSpeed = baseSpeed * 1.1;
                } else if (lowSpeedButton.get()) {
                    robotSpeed = baseSpeed * .7;
                } else {
                    robotSpeed = baseSpeed;
                }

                if (disablePacmanButton.get()) {
                    //Button A = unconstrained movement
                    constrainMovement = false;
                } else if (enablePacmanButton.get()) {
                    //Button B = constrained movement
                    constrainMovement = true;
                    // Flip the boolean to toggle modes for drive constraints
                    //constrainMovement = !constrainMovement;
                }


                strafeSpeed = chassisOp.getLeftX() * -1 * robotSpeed;
                forwardSpeed = chassisOp.getLeftY() * -1 * robotSpeed;
                turnSpeed = chassisOp.getRightX() * -1 * robotSpeed;

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

    void composeTelemetry() {
        // Clear previous ouput
        robotTelemetry.clearAll();

        // Add data to telemetry
        robotTelemetry.addData("y input", "%.2f", forwardSpeed);
        robotTelemetry.addData("x input", "%.2f", strafeSpeed);
        robotTelemetry.addData("rx input", "%.2f", turnSpeed);
        // TODO: Do we need motor speeds? Drive is handled by MecanumDrive class now
        // telemetry.addData("motorLF ", "%.2f", leftFrontPower);
        // telemetry.addData("motorRF ", "%.2f", rightFrontPower);
        // telemetry.addData("motorLR ", "%.2f", leftRearPower);
        // telemetry.addData("motorRR ", "%.2f", rightRearPower);
        robotTelemetry.addData("fourbar position", "%.2f", fourBarPosition);
        robotTelemetry.addData("fourbar home", "%.2f", CDFourBar.LOWER_POSITION_HOME);
        robotTelemetry.addData("FourBarPotUnderHome", fourBarPosition < CDFourBar.ARM_CLEARED_POSITION_HOME);
        robotTelemetry.addData("ArmUpDownPosition", armVerticalPosition);
        robotTelemetry.addData("ArmUpDownTarget", armVerticalTarget);
        robotTelemetry.addData("ArmRotPosition", armRotationPosition);
        robotTelemetry.addData("ArmRotTarget", armRotationTarget);
        robotTelemetry.addData("PickupPosition", pickupPosition);
        robotTelemetry.addData("PickupTarget", pickupTarget);
        robotTelemetry.addData("ExtendPosition", extendPosition);
        robotTelemetry.addData("ExtendTarget", extendTarget);
        robotTelemetry.addData("GrabPosition", grabPosition);
        robotTelemetry.addData("GrabTarget", grabTarget);

        // Loop and update the dashboard
        robotTelemetry.update();
    }
}
