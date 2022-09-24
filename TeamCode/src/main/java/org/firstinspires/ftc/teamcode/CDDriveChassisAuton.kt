package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DcMotor
import kotlin.math.abs

class CDDriveChassisAuton(var robotHardware: CDHardware) {
    private val runtime = ElapsedTime()

    init {
        robotHardware.leftFrontMotor.direction = DcMotorSimple.Direction.REVERSE
        robotHardware.leftRearMotor.direction = DcMotorSimple.Direction.REVERSE
        robotHardware.rightFrontMotor.direction = DcMotorSimple.Direction.FORWARD
        robotHardware.rightRearMotor.direction = DcMotorSimple.Direction.FORWARD
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightRearMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robotHardware.rightFrontMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robotHardware.leftRearMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        robotHardware.leftFrontMotor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    //Method to drive straight move based on encoder counts.
    //Encoders are not reset as the move is based on the current position.
    /*Move will stop if
     * 1. Move gets to desired position
     * 2. Move runs out of time
     * 3. Driver stops the opmode running
     */
    fun encoderDriveStraight(speed: Double, straightInches: Double, straightTimeout: Double) {

        //Determine new target position and pass to motor controller
        val newStraightTargetRR: Int = robotHardware.rightRearMotor.currentPosition + (straightInches * COUNTS_PER_INCH).toInt()
        val newStraightTargetRF: Int = robotHardware.rightFrontMotor.currentPosition + (straightInches * COUNTS_PER_INCH).toInt()
        val newStraightTargetLR: Int = robotHardware.leftRearMotor.currentPosition + (straightInches * COUNTS_PER_INCH).toInt()
        val newStraightTargetLF: Int = robotHardware.leftFrontMotor.currentPosition + (straightInches * COUNTS_PER_INCH).toInt()
        robotHardware.rightRearMotor.targetPosition = newStraightTargetRR
        robotHardware.rightFrontMotor.targetPosition = newStraightTargetRF
        robotHardware.leftRearMotor.targetPosition = newStraightTargetLR
        robotHardware.leftFrontMotor.targetPosition = newStraightTargetLF


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION

        //Reset the timeout time and start motion
        runtime.reset()
        robotHardware.rightRearMotor.power = abs(speed)
        robotHardware.rightFrontMotor.power = abs(speed)
        robotHardware.leftRearMotor.power = abs(speed)
        robotHardware.leftFrontMotor.power = abs(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while (runtime.seconds() < straightTimeout && robotHardware.rightRearMotor.isBusy && robotHardware.rightFrontMotor.isBusy && robotHardware.leftRearMotor.isBusy && robotHardware.leftFrontMotor.isBusy) {
            TODO()
        }

        //Stop all motion
        robotHardware.rightRearMotor.power = 0.0
        robotHardware.rightFrontMotor.power = 0.0
        robotHardware.leftRearMotor.power = 0.0
        robotHardware.leftFrontMotor.power = 0.0

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun encoderDriveStrafe(speed: Double, strafeInches: Double, strafeTimeout: Double) {
        val newStrafeTargetRR: Int
        val newStrafeTargetRF: Int
        val newStrafeTargetLR: Int
        val newStrafeTargetLF: Int
        val strafeMultiplier = 1.25

        //Determine new target position and pass to motor controller
        newStrafeTargetRR =
            robotHardware.rightRearMotor.currentPosition + (strafeInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
        newStrafeTargetRF =
            robotHardware.rightFrontMotor.currentPosition - (strafeInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
        newStrafeTargetLR =
            robotHardware.leftRearMotor.currentPosition - (strafeInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
        newStrafeTargetLF =
            robotHardware.leftFrontMotor.currentPosition + (strafeInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
        robotHardware.rightRearMotor.targetPosition = newStrafeTargetRR
        robotHardware.rightFrontMotor.targetPosition = newStrafeTargetRF
        robotHardware.leftRearMotor.targetPosition = newStrafeTargetLR
        robotHardware.leftFrontMotor.targetPosition = newStrafeTargetLF


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION

        //Reset the timeout time and start motion
        runtime.reset()
        robotHardware.rightRearMotor.power = abs(speed)
        robotHardware.rightFrontMotor.power = abs(speed)
        robotHardware.leftRearMotor.power = abs(speed)
        robotHardware.leftFrontMotor.power = abs(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while (runtime.seconds() < strafeTimeout && robotHardware.rightRearMotor.isBusy and robotHardware.rightFrontMotor.isBusy && robotHardware.leftRearMotor.isBusy && robotHardware.leftFrontMotor.isBusy) {
            TODO()
        }

        //Stop all motion
        robotHardware.rightRearMotor.power = 0.0
        robotHardware.rightFrontMotor.power = 0.0
        robotHardware.leftRearMotor.power = 0.0
        robotHardware.leftFrontMotor.power = 0.0

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun encoderDriveDiag(speed: Double, diagInches: Double, diagTimeout: Double, isLeft: Boolean) {
        var newStrafeTargetRR: Int
        var newStrafeTargetRF: Int
        var newStrafeTargetLR: Int
        var newStrafeTargetLF: Int
        val strafeMultiplier = 1.25
        newStrafeTargetRF = robotHardware.rightFrontMotor.currentPosition
        newStrafeTargetLR = robotHardware.leftRearMotor.currentPosition
        newStrafeTargetRR = robotHardware.rightRearMotor.currentPosition
        newStrafeTargetLF = robotHardware.rightRearMotor.currentPosition
        if (!isLeft && diagInches > 0.01) { // Forward Right
            newStrafeTargetLF =
                robotHardware.leftFrontMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
            newStrafeTargetRR =
                robotHardware.rightRearMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
            newStrafeTargetRF = robotHardware.rightFrontMotor.currentPosition
            newStrafeTargetLR = robotHardware.leftRearMotor.currentPosition
        } else if (isLeft && diagInches > 0.01) { // Forward Left
            newStrafeTargetRR = robotHardware.rightRearMotor.currentPosition
            newStrafeTargetLF = robotHardware.leftFrontMotor.currentPosition
            newStrafeTargetRF =
                robotHardware.rightFrontMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
            newStrafeTargetLR =
                robotHardware.leftRearMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
        } else if (isLeft && diagInches < 0.01) { // Backwards Left
            newStrafeTargetLF =
                robotHardware.leftFrontMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
            newStrafeTargetRR =
                robotHardware.rightRearMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
            newStrafeTargetRF = robotHardware.rightFrontMotor.currentPosition
            newStrafeTargetLR = robotHardware.leftRearMotor.currentPosition
        } else if (!isLeft && diagInches < 0.01) { // Backwards Right
            newStrafeTargetRR = robotHardware.rightRearMotor.currentPosition
            newStrafeTargetLF = robotHardware.leftFrontMotor.currentPosition
            newStrafeTargetRF =
                robotHardware.rightFrontMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
            newStrafeTargetLR =
                robotHardware.leftRearMotor.currentPosition + (diagInches * COUNTS_PER_INCH * strafeMultiplier).toInt()
        }
        robotHardware.rightRearMotor.targetPosition = newStrafeTargetRR
        robotHardware.rightFrontMotor.targetPosition = newStrafeTargetRF
        robotHardware.leftRearMotor.targetPosition = newStrafeTargetLR
        robotHardware.leftFrontMotor.targetPosition = newStrafeTargetLF


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION

        //Reset the timeout time and start motion
        runtime.reset()
        robotHardware.rightRearMotor.power = abs(speed)
        robotHardware.rightFrontMotor.power = abs(speed)
        robotHardware.leftRearMotor.power = abs(speed)
        robotHardware.leftFrontMotor.power = abs(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        if (!isLeft && diagInches > 0.01) { // Forward Right
            while (runtime.seconds() < diagTimeout && robotHardware.rightRearMotor.isBusy && robotHardware.leftFrontMotor.isBusy) {
                TODO()
            }
        } else if (isLeft && diagInches > 0.01) { // Forward Left
            while (runtime.seconds() < diagTimeout && robotHardware.rightFrontMotor.isBusy && robotHardware.leftRearMotor.isBusy) {
                TODO()
            }
        } else if (isLeft && diagInches < 0.01) { // Backwards Left
            while (runtime.seconds() < diagTimeout && robotHardware.rightRearMotor.isBusy && robotHardware.leftFrontMotor.isBusy) {
                TODO()
            }
        } else if (!isLeft && diagInches < 0.01) { // Backwards Right
            while (runtime.seconds() < diagTimeout && robotHardware.rightFrontMotor.isBusy && robotHardware.leftRearMotor.isBusy) {
                TODO()
            }
        }


        //Stop all motion
        robotHardware.rightRearMotor.power = 0.0
        robotHardware.rightFrontMotor.power = 0.0
        robotHardware.leftRearMotor.power = 0.0
        robotHardware.leftFrontMotor.power = 0.0

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    fun encoderDriveTurn(speed: Double, turnDeg: Double, strafeTimeout: Double) {
        val newTurnTargetRR: Int
        val newTurnTargetRF: Int
        val newTurnTargetLR: Int
        val newTurnTargetLF: Int

        //Calculate turn inches with a 9.75" wheel base
        //TODO test accuracy on mats. it is slightly overturning right now.
        val turnInches = turnDeg / 360 * (2 * 3.1415 * 9)

        //Determine new target position and pass to motor controller
        newTurnTargetRR =
            robotHardware.rightRearMotor.currentPosition - (turnInches * COUNTS_PER_INCH).toInt()
        newTurnTargetRF =
            robotHardware.rightFrontMotor.currentPosition - (turnInches * COUNTS_PER_INCH).toInt()
        newTurnTargetLR =
            robotHardware.leftRearMotor.currentPosition + (turnInches * COUNTS_PER_INCH).toInt()
        newTurnTargetLF =
            robotHardware.leftFrontMotor.currentPosition + (turnInches * COUNTS_PER_INCH).toInt()
        robotHardware.rightRearMotor.targetPosition = newTurnTargetRR
        robotHardware.rightFrontMotor.targetPosition = newTurnTargetRF
        robotHardware.leftRearMotor.targetPosition = newTurnTargetLR
        robotHardware.leftFrontMotor.targetPosition = newTurnTargetLF


        //Turn On RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_TO_POSITION
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_TO_POSITION

        //Reset the timeout time and start motion
        runtime.reset()
        robotHardware.rightRearMotor.power = abs(speed)
        robotHardware.rightFrontMotor.power = abs(speed)
        robotHardware.leftRearMotor.power = abs(speed)
        robotHardware.leftFrontMotor.power = abs(speed)

        //Loop while motors are active. This uses && which means that if any of the motors hit their target the motion will stop.
        //This is safer to ensure the robot will end motion asap.
        while (runtime.seconds() < strafeTimeout && robotHardware.rightRearMotor.isBusy && robotHardware.rightFrontMotor.isBusy && robotHardware.leftRearMotor.isBusy && robotHardware.leftFrontMotor.isBusy) {
            TODO()
        }

        //Stop all motion
        robotHardware.rightRearMotor.power = 0.0
        robotHardware.rightFrontMotor.power = 0.0
        robotHardware.leftRearMotor.power = 0.0
        robotHardware.leftFrontMotor.power = 0.0

        //Turn off RUN_TO_POSITION
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
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
}