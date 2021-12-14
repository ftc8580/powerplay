package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
// import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// import com.qualcomm.robotcore.util.Range;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

public class CDDriveChassisAuton {

    CDHardware robotHardware;
    private ElapsedTime runtime = new ElapsedTime();

    public CDDriveChassisAuton(CDHardware theHardware) {

        robotHardware = theHardware;

        robotHardware.leftfrontmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        robotHardware.leftrearmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        robotHardware.rightfrontmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.rightrearmotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        robotHardware.rightrearmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.rightfrontmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.leftrearmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.leftfrontmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
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
        newStraightTargetRR = robotHardware.rightrearmotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);
        newStraightTargetRF = robotHardware.rightfrontmotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);
        newStraightTargetLR = robotHardware.leftrearmotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);
        newStraightTargetLF = robotHardware.leftfrontmotor.getCurrentPosition() + (int)(straightInches * COUNTS_PER_INCH);

        robotHardware.rightrearmotor.setTargetPosition(newStraightTargetRR);
        robotHardware.rightfrontmotor.setTargetPosition(newStraightTargetRF);
        robotHardware.leftrearmotor.setTargetPosition(newStraightTargetLR);
        robotHardware.leftfrontmotor.setTargetPosition(newStraightTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightrearmotor.setPower(Math.abs(speed));
        robotHardware.rightfrontmotor.setPower(Math.abs(speed));
        robotHardware.leftrearmotor.setPower(Math.abs(speed));
        robotHardware.leftfrontmotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while ((runtime.seconds()<straightTimeout) && (robotHardware.rightrearmotor.isBusy() && robotHardware.rightfrontmotor.isBusy() && robotHardware.leftrearmotor.isBusy() && robotHardware.leftfrontmotor.isBusy())) {
        }

        //Stop all motion
        robotHardware.rightrearmotor.setPower(0);
        robotHardware.rightfrontmotor.setPower(0);
        robotHardware.leftrearmotor.setPower(0);
        robotHardware.leftfrontmotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDriveStrafe(double speed, double strafeInches, double strafeTimeout) {
        int newStrafeTargetRR;
        int newStrafeTargetRF;
        int newStrafeTargetLR;
        int newStrafeTargetLF;
        double strafemult = 1.25;

        //Determine new target position and pass to motor controller
        newStrafeTargetRR = robotHardware.rightrearmotor.getCurrentPosition() + (int)(strafeInches * COUNTS_PER_INCH*strafemult);
        newStrafeTargetRF = robotHardware.rightfrontmotor.getCurrentPosition() - (int)(strafeInches * COUNTS_PER_INCH*strafemult);
        newStrafeTargetLR = robotHardware.leftrearmotor.getCurrentPosition() - (int)(strafeInches * COUNTS_PER_INCH*strafemult);
        newStrafeTargetLF = robotHardware.leftfrontmotor.getCurrentPosition() + (int)(strafeInches * COUNTS_PER_INCH*strafemult);

        robotHardware.rightrearmotor.setTargetPosition(newStrafeTargetRR);
        robotHardware.rightfrontmotor.setTargetPosition(newStrafeTargetRF);
        robotHardware.leftrearmotor.setTargetPosition(newStrafeTargetLR);
        robotHardware.leftfrontmotor.setTargetPosition(newStrafeTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightrearmotor.setPower(Math.abs(speed));
        robotHardware.rightfrontmotor.setPower(Math.abs(speed));
        robotHardware.leftrearmotor.setPower(Math.abs(speed));
        robotHardware.leftfrontmotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while ((runtime.seconds()<strafeTimeout) && ((robotHardware.rightrearmotor.isBusy() & robotHardware.rightfrontmotor.isBusy() && robotHardware.leftrearmotor.isBusy() && robotHardware.leftfrontmotor.isBusy()))) {
        }

        //Stop all motion
        robotHardware.rightrearmotor.setPower(0);
        robotHardware.rightfrontmotor.setPower(0);
        robotHardware.leftrearmotor.setPower(0);
        robotHardware.leftfrontmotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void encoderDriveDiag(double speed, double diagInches, double diagTimeout, boolean isLeft) {

        int newStrafeTargetRR;
        int newStrafeTargetRF;
        int newStrafeTargetLR;
        int newStrafeTargetLF;
        double strafemult = 1.25;

        newStrafeTargetRF = robotHardware.rightfrontmotor.getCurrentPosition();
        newStrafeTargetLR = robotHardware.leftrearmotor.getCurrentPosition();
        newStrafeTargetRR = robotHardware.rightrearmotor.getCurrentPosition();
        newStrafeTargetLF = robotHardware.rightrearmotor.getCurrentPosition();
        if (!isLeft && (diagInches > 0.01)) { // Forward Right
            newStrafeTargetLF = robotHardware.leftfrontmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRR = robotHardware.rightrearmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRF = robotHardware.rightfrontmotor.getCurrentPosition();
            newStrafeTargetLR = robotHardware.leftrearmotor.getCurrentPosition();
        } else if (isLeft && (diagInches > 0.01)) { // Forward Left
            newStrafeTargetRR = robotHardware.rightrearmotor.getCurrentPosition();
            newStrafeTargetLF = robotHardware.leftfrontmotor.getCurrentPosition();
            newStrafeTargetRF = robotHardware.rightfrontmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetLR = robotHardware.leftrearmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
        } else if (!isLeft && (diagInches < 0.01)) { // Backwards Right
            newStrafeTargetLF = robotHardware.leftfrontmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRR = robotHardware.rightrearmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetRF = robotHardware.rightfrontmotor.getCurrentPosition();
            newStrafeTargetLR = robotHardware.leftrearmotor.getCurrentPosition();
        } else if (isLeft && (diagInches < 0.01)) { // Backwards Left
            newStrafeTargetRR = robotHardware.rightrearmotor.getCurrentPosition();
            newStrafeTargetLF = robotHardware.leftfrontmotor.getCurrentPosition();
            newStrafeTargetRF = robotHardware.rightfrontmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
            newStrafeTargetLR = robotHardware.leftrearmotor.getCurrentPosition() + (int)(diagInches * COUNTS_PER_INCH * strafemult);
        }


        robotHardware.rightrearmotor.setTargetPosition(newStrafeTargetRR);
        robotHardware.rightfrontmotor.setTargetPosition(newStrafeTargetRF);
        robotHardware.leftrearmotor.setTargetPosition(newStrafeTargetLR);
        robotHardware.leftfrontmotor.setTargetPosition(newStrafeTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightrearmotor.setPower(Math.abs(speed));
        robotHardware.rightfrontmotor.setPower(Math.abs(speed));
        robotHardware.leftrearmotor.setPower(Math.abs(speed));
        robotHardware.leftfrontmotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        if (!isLeft && (diagInches > 0.01)) { // Forward Right
            while ((runtime.seconds()<diagTimeout) && (robotHardware.rightrearmotor.isBusy() && robotHardware.leftfrontmotor.isBusy())) {
            }

        } else if (isLeft && (diagInches > 0.01)) { // Forward Left
           while ((runtime.seconds()<diagTimeout) && (robotHardware.rightfrontmotor.isBusy() && robotHardware.leftrearmotor.isBusy())) {
            }

        } else if (!isLeft && (diagInches < 0.01)) { // Backwards Right
            while ((runtime.seconds()<diagTimeout) && (robotHardware.rightrearmotor.isBusy() && robotHardware.leftfrontmotor.isBusy())) {
            }

        } else if (isLeft && (diagInches < 0.01)) { // Backwards Left
            while ((runtime.seconds()<diagTimeout) && ( robotHardware.rightfrontmotor.isBusy() && robotHardware.leftrearmotor.isBusy())) {
            }
        }


        //Stop all motion
        robotHardware.rightrearmotor.setPower(0);
        robotHardware.rightfrontmotor.setPower(0);
        robotHardware.leftrearmotor.setPower(0);
        robotHardware.leftfrontmotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
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
        newTurnTargetRR = robotHardware.rightrearmotor.getCurrentPosition() - (int)(turnInches * COUNTS_PER_INCH);
        newTurnTargetRF = robotHardware.rightfrontmotor.getCurrentPosition() - (int)(turnInches * COUNTS_PER_INCH);
        newTurnTargetLR = robotHardware.leftrearmotor.getCurrentPosition() + (int)(turnInches * COUNTS_PER_INCH);
        newTurnTargetLF = robotHardware.leftfrontmotor.getCurrentPosition() + (int)(turnInches * COUNTS_PER_INCH);

        robotHardware.rightrearmotor.setTargetPosition(newTurnTargetRR);
        robotHardware.rightfrontmotor.setTargetPosition(newTurnTargetRF);
        robotHardware.leftrearmotor.setTargetPosition(newTurnTargetLR);
        robotHardware.leftfrontmotor.setTargetPosition(newTurnTargetLF);


        //Turn On RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //Reset the timeout time and start motion
        runtime.reset();
        robotHardware.rightrearmotor.setPower(Math.abs(speed));
        robotHardware.rightfrontmotor.setPower(Math.abs(speed));
        robotHardware.leftrearmotor.setPower(Math.abs(speed));
        robotHardware.leftfrontmotor.setPower(Math.abs(speed));

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while ((runtime.seconds()<strafeTimeout) && (robotHardware.rightrearmotor.isBusy() && robotHardware.rightfrontmotor.isBusy() && robotHardware.leftrearmotor.isBusy() && robotHardware.leftfrontmotor.isBusy())) {
        }

        //Stop all motion
        robotHardware.rightrearmotor.setPower(0);
        robotHardware.rightfrontmotor.setPower(0);
        robotHardware.leftrearmotor.setPower(0);
        robotHardware.leftfrontmotor.setPower(0);

        //Turn off RUN_TO_POSITION
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
}













