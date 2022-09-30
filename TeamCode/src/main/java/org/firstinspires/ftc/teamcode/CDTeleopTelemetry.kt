package org.firstinspires.ftc.teamcode

import com.qualcomm.hardware.bosch.BNO055IMU
import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.navigation.*
import org.firstinspires.ftc.teamcode.models.DrivePower
import org.firstinspires.ftc.teamcode.models.GamePadInput
import java.util.*
import kotlin.math.sqrt

class CDTeleopTelemetry(private val telemetry: Telemetry) {
    fun logStarted() {
        telemetry.addData("Status", "Fully Initialized")
        telemetry.update()
    }

    fun composeTelemetry(
        gamePadInput: GamePadInput,
        drivePower: DrivePower,
    ) {
        telemetry.clearAll()

        telemetry.addData("y input", "%.2f", gamePadInput.y)
        telemetry.addData("x input", "%.2f", gamePadInput.x)
        telemetry.addData("rx input", "%.2f", gamePadInput.rx)
        telemetry.addData("motorLF ", "%.2f", drivePower.leftFrontPower)
        telemetry.addData("motorRF ", "%.2f", drivePower.rightFrontPower)
        telemetry.addData("motorLR ", "%.2f", drivePower.leftRearPower)
        telemetry.addData("motorRR ", "%.2f", drivePower.rightRearPower)

        telemetry.update()
    }

    fun composeTelemetry(imu: BNO055IMU) {
        telemetry.clearAll()

        val angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES)
        val gravity = imu.gravity

        // telemetry.addData("heading", heading)
        telemetry.addLine()
            .addData("status", imu.systemStatus.toShortString())
            .addData("calib", imu.calibrationStatus.toString())

        telemetry.addLine()
            .addData("heading", formatAngle(angles.angleUnit, angles.firstAngle))
            .addData("roll", formatAngle(angles.angleUnit, angles.firstAngle))
            .addData("pitch", formatAngle(angles.angleUnit, angles.thirdAngle))



        telemetry.addLine()
            .addData("grvty", gravity.toString())
            .addData("mag", formatMag(gravity))

        telemetry.update()
    }

    private fun formatMag(gravity: Acceleration) {
        String.format(
            Locale.getDefault(),
            "%.3f",
            sqrt(
                gravity.xAccel * gravity.xAccel +
                gravity.yAccel * gravity.yAccel +
                gravity.zAccel * gravity.zAccel
            )
        )
    }

    private fun formatAngle(angleUnit: AngleUnit, angle: Float) {
        formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle.toDouble()))
    }

    private fun formatDegrees(degrees: Double) {
        String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees))
    }
}