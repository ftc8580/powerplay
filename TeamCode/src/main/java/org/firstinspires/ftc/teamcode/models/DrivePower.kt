package org.firstinspires.ftc.teamcode.models

import kotlin.math.abs

class DrivePower(
    var leftFrontPower: Double = 0.0,
    var leftRearPower: Double = 0.0,
    var rightFrontPower: Double = 0.0,
    var rightRearPower: Double = 0.0,
) {
    fun refreshDrivePower(gamePadInput: GamePadInput, pacmanMode: Boolean = false) {
        // This isn't totally necessary, but it allows us to not repeat `gamePadInput` repeatedly
        // throughout this method.
        val x = gamePadInput.x
        val y = gamePadInput.y
        val rx = gamePadInput.rx

        // Denominator is the largest motor power (absolute value) or 1
        // This ensures all the powers maintain the same ratio, but only when
        // at least one is out of the range [-1, 1]
        val denominator = (abs(gamePadInput.y) + abs(gamePadInput.x) + abs(gamePadInput.rx))
            .coerceAtLeast(1.0)

        if (pacmanMode) {
            leftFrontPower = (y + x) / denominator
            leftRearPower = (y - x) / denominator
            rightFrontPower = (y - x) / denominator
            rightRearPower = (y + x) / denominator
        } else {
            leftFrontPower = (y + x - rx) / denominator
            leftRearPower = (y - x - rx) / denominator
            rightFrontPower = (y - x + rx) / denominator
            rightRearPower = (y + x + rx) / denominator
        }
    }
}
