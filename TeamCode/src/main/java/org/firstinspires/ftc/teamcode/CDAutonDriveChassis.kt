package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.models.MotorPositions
import kotlin.math.abs

class CDAutonDriveChassis(var robotHardware: CDHardware) {
    private val runtime = ElapsedTime()

    init {
        setMotorDirections()
        runUsingEncoder()
        setZeroPowerBehavior()
    }

    //Method to drive straight move based on encoder counts.
    //Encoders are not reset as the move is based on the current position.
    /*Move will stop if
     * 1. Move gets to desired position
     * 2. Move runs out of time
     * 3. Driver stops the opmode running
     */
    fun encoderDriveStraight(speed: Double, straightInches: Double, straightTimeout: Double) {
        val adjustment = getAdjustment(straightInches)
        val motorPositions = robotHardware.getPositions()

        //Determine new target position and pass to motor controller
        val newPositions = MotorPositions(
            leftFront = motorPositions.leftFront + adjustment,
            leftRear = motorPositions.leftRear + adjustment,
            rightFront = motorPositions.rightFront + adjustment,
            rightRear = motorPositions.rightRear + adjustment,
        )
        robotHardware.setTargetPositions(newPositions)

        //Turn On RUN_TO_POSITION
        runToPosition()

        //Reset the timeout time and start motion
        runtime.reset()
        setPower(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while (shouldContinueLooping(straightTimeout)) {
            // Do something?
        }

        //Stop all motion
        stopAllMotion()

        //Turn off RUN_TO_POSITION
        runUsingEncoder()
    }

    fun encoderDriveStrafe(speed: Double, strafeInches: Double, strafeTimeout: Double) {
        val strafeMultiplier = 1.25
        val adjustment = getAdjustment(strafeInches, strafeMultiplier)
        val motorPositions = robotHardware.getPositions()

        val newPositions = MotorPositions(
            leftFront = motorPositions.leftFront + adjustment,
            leftRear = motorPositions.leftRear - adjustment,
            rightFront = motorPositions.rightFront - adjustment,
            rightRear = motorPositions.rightRear + adjustment,
        )
        robotHardware.setTargetPositions(newPositions)

        //Turn On RUN_TO_POSITION
        runToPosition()

        //Reset the timeout time and start motion
        runtime.reset()
        setPower(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while (shouldContinueLooping(strafeTimeout)) {
            // Do something?
        }

        //Stop all motion
        stopAllMotion()

        //Turn off RUN_TO_POSITION
        runUsingEncoder()
    }

    fun encoderDriveDiag(speed: Double, diagInches: Double, diagTimeout: Double, isLeft: Boolean) {
        val strafeMultiplier = 1.25
        val adjustment = getAdjustment(diagInches, strafeMultiplier)
        val motorPositions = robotHardware.getPositions()

        val newPositions = if (
            (!isLeft && diagInches > 0.01) || (isLeft && diagInches < 0.01)
        ) { // Forward Right
            MotorPositions(
                leftFront = motorPositions.leftFront + adjustment,
                leftRear = motorPositions.leftRear,
                rightFront = motorPositions.rightFront,
                rightRear = motorPositions.rightRear + adjustment,
            )
        } else if (
            (isLeft && diagInches > 0.01) || (!isLeft && diagInches < 0.01)
        ) { // Forward Left
            MotorPositions(
                leftFront = motorPositions.leftFront,
                leftRear = motorPositions.leftRear + adjustment,
                rightFront = motorPositions.rightFront + adjustment,
                rightRear = motorPositions.rightRear,
            )
        } else { // No change (error condition)
            motorPositions
        }

        robotHardware.setTargetPositions(newPositions)

        //Turn On RUN_TO_POSITION
        runToPosition()

        //Reset the timeout time and start motion
        runtime.reset()
        setPower(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        if (
            (!isLeft && diagInches > 0.01) || (isLeft && diagInches < 0.01)
        ) { // Forward Right
            while (
                runtime.seconds() < diagTimeout &&
                robotHardware.rightRearMotor.isBusy &&
                robotHardware.leftFrontMotor.isBusy
            ) {
                // Do something?
            }
        } else if (
            (isLeft && diagInches > 0.01) || (!isLeft && diagInches < 0.01)
        ) { // Forward Left
            while (
                runtime.seconds() < diagTimeout &&
                robotHardware.rightFrontMotor.isBusy &&
                robotHardware.leftRearMotor.isBusy
            ) {
                // Do something?
            }
        }

        //Stop all motion
        stopAllMotion()

        //Turn off RUN_TO_POSITION
        runUsingEncoder()
    }

    fun encoderDriveTurn(speed: Double, turnDeg: Double, strafeTimeout: Double) {
        //Calculate turn inches with a 9.75" wheel base
        //TODO test accuracy on mats. it is slightly overturning right now.
        val turnInches = turnDeg / 360 * (2 * 3.1415 * 9)
        val adjustment = getAdjustment(turnInches)
        val motorPositions = robotHardware.getPositions()

        val newPositions = MotorPositions(
            leftFront = motorPositions.leftFront + adjustment,
            leftRear = motorPositions.leftRear + adjustment,
            rightFront = motorPositions.rightFront - adjustment,
            rightRear = motorPositions.rightRear - adjustment,
        )
        robotHardware.setTargetPositions(newPositions)

        //Turn On RUN_TO_POSITION
        runToPosition()

        //Reset the timeout time and start motion
        runtime.reset()
        setPower(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while (shouldContinueLooping(strafeTimeout)) {
            // Do something?
        }

        //Stop all motion
        stopAllMotion()

        //Turn off RUN_TO_POSITION
        runUsingEncoder()
    }

    companion object {
        //Create methods to drive using encoder
        private const val COUNTS_PER_MOTOR_REV = 537.7 //GoBuilda 5203-2402-0019
        private const val DRIVE_GEAR_REDUCTION = 1.0 //This is greater than 1 if geared up
        private const val WHEEL_DIAMETER_INCHES = 4.0 //Used for circumference
        const val COUNTS_PER_INCH =
            COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION / (WHEEL_DIAMETER_INCHES * 3.1415)
        const val DRIVE_SPEED = 0.4
        const val TURN_SPEED = 0.3
    }

    fun stopAndResetEncoder() {
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        // robotHardware.turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    fun runUsingEncoder() {
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        // robotHardware.turretMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    private fun shouldContinueLooping(timeout: Double): Boolean {
        return runtime.seconds() < timeout &&
                robotHardware.rightRearMotor.isBusy &&
                robotHardware.rightFrontMotor.isBusy &&
                robotHardware.leftRearMotor.isBusy &&
                robotHardware.leftFrontMotor.isBusy
    }

    private fun setMotorDirections() {
        robotHardware.leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
        robotHardware.leftRearMotor.direction = DcMotorSimple.Direction.REVERSE
        robotHardware.rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
        robotHardware.rightRearMotor.direction = DcMotorSimple.Direction.FORWARD
    }

    private fun runToPosition() {
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
    }

    private fun setZeroPowerBehavior() {
        robotHardware.rightRearMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robotHardware.rightFrontMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robotHardware.leftRearMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robotHardware.leftFrontMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    private fun setPower(speed: Double) {
        robotHardware.rightRearMotor.power = abs(speed)
        robotHardware.rightFrontMotor.power = abs(speed)
        robotHardware.leftRearMotor.power = abs(speed)
        robotHardware.leftFrontMotor.power = abs(speed)
    }

    private fun stopAllMotion() {
        robotHardware.rightRearMotor.power = 0.0
        robotHardware.rightFrontMotor.power = 0.0
        robotHardware.leftRearMotor.power = 0.0
        robotHardware.leftFrontMotor.power = 0.0
    }

    private fun getAdjustment(inches: Double, multiplier: Double = 1.0): Int {
        return (inches * COUNTS_PER_INCH * multiplier).toInt()
    }
}