package org.firstinspires.ftc.teamcode.models

import com.qualcomm.robotcore.hardware.Gamepad
import kotlin.math.pow

class GamePadInput(
    var x: Double = 0.0,
    var y: Double = 0.0,
    var rx: Double = 0.0,
) {
    fun refreshGamePadInput(gamepad: Gamepad) {
        y = gamepad.left_stick_y.toDouble().pow(3.0) // Remember, this is reversed!
        x = (gamepad.left_stick_x * -1.1).pow(3.0) // Counteract imperfect strafing
        rx = gamepad.right_stick_x.toDouble().pow(3.0) * 0.5 //Reduced turn speed to make it easier to control
    }
}
