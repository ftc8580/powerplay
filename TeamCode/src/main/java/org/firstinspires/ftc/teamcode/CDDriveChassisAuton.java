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
    }

    //Create methods to drive using encoder
    static final double COUNTS_PER_MOTOR_REV = 537.7; //GoBuilda 5203-2402-0019
    static final double DRIVE_GEAR_REDUCTION = 1.0; //This is greater than 1 if geared up
    static final double WHEEL_DIAMETER_INCHES = 4.0; //Used for circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double DRIVE_SPEED = 0.2;
    static final double TURN_SPEED = 0.5;

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
}















