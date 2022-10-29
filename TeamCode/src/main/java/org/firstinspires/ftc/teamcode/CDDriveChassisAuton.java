package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class CDDriveChassisAuton {
    org.firstinspires.ftc.teamcode.CDHardware robotHardware;
    private final CDRuntime runtime = new CDRuntime();

    public CDDriveChassisAuton(CDHardware theHardware) {
        robotHardware = theHardware;

        robotHardware.leftFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        robotHardware.leftRearMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        robotHardware.rightFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.rightRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robotHardware.rightRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.rightFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.leftRearMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.leftFrontMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    //Create methods to drive using encoder
    static final double COUNTS_PER_MOTOR_REV = 537.7; //GoBuilda 5203-2402-0019
    static final double DRIVE_GEAR_REDUCTION = 1.0; //This is greater than 1 if geared up
    static final double WHEEL_DIAMETER_INCHES = 4.0; //Used for circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.4;
    static final double TURN_SPEED = 0.3;

    //Method to drive straight move based on encoder counts.
    //Encoders are not reset as the move is based on the current position.
    /*Move will stop if
     * 1. Move gets to desired position
     * 2. Move runs out of time
     * 3. Driver stops the opmode running
     */

    public void encoderDriveStraight(double speed, double straightInches, double straightTimeout) {
        int newStraightTargetRR;
        int newStraightTargetRF;
        int newStraightTargetLR;
        int newStraightTargetLF;

        //Determine new target position and pass to motor controller
        newStraightTargetRR = robotHardware.rightRearMotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);
        newStraightTargetRF = robotHardware.rightFrontMotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);
        newStraightTargetLR = robotHardware.leftRearMotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);
        newStraightTargetLF = robotHardware.leftFrontMotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);

        robotHardware.rightRearMotor.setTargetPosition(newStraightTargetRR);
        robotHardware.rightFrontMotor.setTargetPosition(newStraightTargetRF);
        robotHardware.leftRearMotor.setTargetPosition(newStraightTargetLR);
        robotHardware.leftFrontMotor.setTargetPosition(newStraightTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightRearMotor.setPower(Math.abs(speed));
        robotHardware.rightFrontMotor.setPower(Math.abs(speed));
        robotHardware.leftRearMotor.setPower(Math.abs(speed));
        robotHardware.leftFrontMotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while ((runtime.seconds()<straightTimeout) && (robotHardware.rightRearMotor.isBusy() && robotHardware.rightFrontMotor.isBusy() && robotHardware.leftRearMotor.isBusy() && robotHardware.leftFrontMotor.isBusy())) {
        }

        //Stop all motion
        robotHardware.rightRearMotor.setPower(0);
        robotHardware.rightFrontMotor.setPower(0);
        robotHardware.leftRearMotor.setPower(0);
        robotHardware.leftFrontMotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDriveStrafe(double speed, double strafeInches, double strafeTimeout) {
        int newStrafeTargetRR;
        int newStrafeTargetRF;
        int newStrafeTargetLR;
        int newStrafeTargetLF;
        double strafemult = 1.25;

        //Determine new target position and pass to motor controller
        newStrafeTargetRR = robotHardware.rightRearMotor.getCurrentPosition() + (int)(strafeInches * COUNTS_PER_INCH*strafemult);
        newStrafeTargetRF = robotHardware.rightFrontMotor.getCurrentPosition() - (int)(strafeInches * COUNTS_PER_INCH*strafemult);
        newStrafeTargetLR = robotHardware.leftRearMotor.getCurrentPosition() - (int)(strafeInches * COUNTS_PER_INCH*strafemult);
        newStrafeTargetLF = robotHardware.leftFrontMotor.getCurrentPosition() + (int)(strafeInches * COUNTS_PER_INCH*strafemult);

        robotHardware.rightRearMotor.setTargetPosition(newStrafeTargetRR);
        robotHardware.rightFrontMotor.setTargetPosition(newStrafeTargetRF);
        robotHardware.leftRearMotor.setTargetPosition(newStrafeTargetLR);
        robotHardware.leftFrontMotor.setTargetPosition(newStrafeTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightRearMotor.setPower(Math.abs(speed));
        robotHardware.rightFrontMotor.setPower(Math.abs(speed));
        robotHardware.leftRearMotor.setPower(Math.abs(speed));
        robotHardware.leftFrontMotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while ((runtime.seconds()<strafeTimeout) && ((robotHardware.rightRearMotor.isBusy() & robotHardware.rightFrontMotor.isBusy() && robotHardware.leftRearMotor.isBusy() && robotHardware.leftFrontMotor.isBusy()))) {
        }

        //Stop all motion
        robotHardware.rightRearMotor.setPower(0);
        robotHardware.rightFrontMotor.setPower(0);
        robotHardware.leftRearMotor.setPower(0);
        robotHardware.leftFrontMotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDriveDiag(double speed, double diagInches, double diagTimeout, boolean isLeft) {

        int newStrafeTargetRR;
        int newStrafeTargetRF;
        int newStrafeTargetLR;
        int newStrafeTargetLF;
        double strafemult = 1.25;

        newStrafeTargetRF = robotHardware.rightFrontMotor.getCurrentPosition();
        newStrafeTargetLR = robotHardware.leftRearMotor.getCurrentPosition();
        newStrafeTargetRR = robotHardware.rightRearMotor.getCurrentPosition();
        newStrafeTargetLF = robotHardware.rightRearMotor.getCurrentPosition();
        if (!isLeft && (diagInches > 0.01)) { // Forward Right
            newStrafeTargetLF = robotHardware.leftFrontMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRR = robotHardware.rightRearMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRF = robotHardware.rightFrontMotor.getCurrentPosition();
            newStrafeTargetLR = robotHardware.leftRearMotor.getCurrentPosition();
        } else if (isLeft && (diagInches > 0.01)) { // Forward Left
            newStrafeTargetRR = robotHardware.rightRearMotor.getCurrentPosition();
            newStrafeTargetLF = robotHardware.leftFrontMotor.getCurrentPosition();
            newStrafeTargetRF = robotHardware.rightFrontMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetLR = robotHardware.leftRearMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
        } else if (isLeft && (diagInches < 0.01)) { // Backwards Left
            newStrafeTargetLF = robotHardware.leftFrontMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRR = robotHardware.rightRearMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRF = robotHardware.rightFrontMotor.getCurrentPosition();
            newStrafeTargetLR = robotHardware.leftRearMotor.getCurrentPosition();
        } else if (!isLeft && (diagInches < 0.01)) { // Backwards Right
            newStrafeTargetRR = robotHardware.rightRearMotor.getCurrentPosition();
            newStrafeTargetLF = robotHardware.leftFrontMotor.getCurrentPosition();
            newStrafeTargetRF = robotHardware.rightFrontMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetLR = robotHardware.leftRearMotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
        }


        robotHardware.rightRearMotor.setTargetPosition(newStrafeTargetRR);
        robotHardware.rightFrontMotor.setTargetPosition(newStrafeTargetRF);
        robotHardware.leftRearMotor.setTargetPosition(newStrafeTargetLR);
        robotHardware.leftFrontMotor.setTargetPosition(newStrafeTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightRearMotor.setPower(Math.abs(speed));
        robotHardware.rightFrontMotor.setPower(Math.abs(speed));
        robotHardware.leftRearMotor.setPower(Math.abs(speed));
        robotHardware.leftFrontMotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        if (!isLeft && (diagInches > 0.01)) { // Forward Right
            while ((runtime.seconds()<diagTimeout) && (robotHardware.rightRearMotor.isBusy() && robotHardware.leftFrontMotor.isBusy())) {
            }

        } else if (isLeft && (diagInches > 0.01)) { // Forward Left
           while ((runtime.seconds()<diagTimeout) && (robotHardware.rightFrontMotor.isBusy() && robotHardware.leftRearMotor.isBusy())) {
            }

        } else if (isLeft && (diagInches < 0.01)) { // Backwards Left
            while ((runtime.seconds()<diagTimeout) && (robotHardware.rightRearMotor.isBusy() && robotHardware.leftFrontMotor.isBusy())) {
            }

        } else if (!isLeft && (diagInches < 0.01)) { // Backwards Right
            while ((runtime.seconds()<diagTimeout) && ( robotHardware.rightFrontMotor.isBusy() && robotHardware.leftRearMotor.isBusy())) {
            }
        }


        //Stop all motion
        robotHardware.rightRearMotor.setPower(0);
        robotHardware.rightFrontMotor.setPower(0);
        robotHardware.leftRearMotor.setPower(0);
        robotHardware.leftFrontMotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDriveTurn(double speed, double turnDeg, double strafeTimeout) {
        int newTurnTargetRR;
        int newTurnTargetRF;
        int newTurnTargetLR;
        int newTurnTargetLF;

        //Calculate turn inches with a 9.75" wheel base
        //TODO test accuracy on mats. it is slightly overturning right now.
        double turnInches = turnDeg/360 * (2*3.1415*9);

        //Determine new target position and pass to motor controller
        newTurnTargetRR = robotHardware.rightRearMotor.getCurrentPosition() - (int)(turnInches * COUNTS_PER_INCH);
        newTurnTargetRF = robotHardware.rightFrontMotor.getCurrentPosition() - (int)(turnInches * COUNTS_PER_INCH);
        newTurnTargetLR = robotHardware.leftRearMotor.getCurrentPosition() + (int)(turnInches * COUNTS_PER_INCH);
        newTurnTargetLF = robotHardware.leftFrontMotor.getCurrentPosition() + (int)(turnInches * COUNTS_PER_INCH);

        robotHardware.rightRearMotor.setTargetPosition(newTurnTargetRR);
        robotHardware.rightFrontMotor.setTargetPosition(newTurnTargetRF);
        robotHardware.leftRearMotor.setTargetPosition(newTurnTargetLR);
        robotHardware.leftFrontMotor.setTargetPosition(newTurnTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightRearMotor.setPower(Math.abs(speed));
        robotHardware.rightFrontMotor.setPower(Math.abs(speed));
        robotHardware.leftRearMotor.setPower(Math.abs(speed));
        robotHardware.leftFrontMotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while ((runtime.seconds()<strafeTimeout) && (robotHardware.rightRearMotor.isBusy() && robotHardware.rightFrontMotor.isBusy() && robotHardware.leftRearMotor.isBusy() && robotHardware.leftFrontMotor.isBusy())) {
        }

        //Stop all motion
        robotHardware.rightRearMotor.setPower(0);
        robotHardware.rightFrontMotor.setPower(0);
        robotHardware.leftRearMotor.setPower(0);
        robotHardware.leftFrontMotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}













