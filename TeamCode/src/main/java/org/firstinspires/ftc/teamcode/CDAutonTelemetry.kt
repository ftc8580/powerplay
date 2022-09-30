package org.firstinspires.ftc.teamcode

import org.firstinspires.ftc.robotcore.external.Telemetry
import org.firstinspires.ftc.robotcore.external.tfod.Recognition
import org.firstinspires.ftc.teamcode.models.MotorPositions

class CDAutonTelemetry(private val telemetry: Telemetry) {
    fun logInitialized() {
        telemetry.addData("Status", "Initialized")
    }

    fun logResettingEncoders() {
        telemetry.addData("Status", "Resetting Encoders")
    }

    fun logStartPrompt() {
        telemetry.addData(">", "Press Play to start op mode")
    }

    fun logMotorPositions(motorPositions: MotorPositions) {
        telemetry.addData(
            "MotorStartPos (RR, RF, LR, LF)",
            " %7d %7d %7d %7d",
            motorPositions.rightRear,
            motorPositions.rightFront,
            motorPositions.leftRear,
            motorPositions.leftFront
        )
    }

    fun logRecognitions(recognitions: List<Recognition>) {
        telemetry.addData("# Object Detected", recognitions.size)

        if (recognitions.isEmpty()) {
            // duckLocation = duckWeDoNotSee
            // telemetry.addData("Token Location:", duckLocation)
            // telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));
        }

        // telemetry.addData("Token Location:", duckLocation);
        // telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));

        // Step through the list of recognitions and display boundary info
        for ((i, recognition) in recognitions.withIndex()) {
            telemetry.addData(String.format("label (%d)", i), recognition.label)
            telemetry.addData(
                String.format("  left,top (%d)", i),
                "%.03f , %.03f",
                recognition.left,
                recognition.top
            )
            // telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
            // recognition.getRight(), recognition.getBottom());
        }

        telemetry.update()
    }
}