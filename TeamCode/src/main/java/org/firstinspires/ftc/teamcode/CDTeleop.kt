package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.models.DrivePower
import org.firstinspires.ftc.teamcode.models.GamePadInput
import java.lang.Exception

// There are work in progress / untested IMU elements in this code which we may want to use so they are preserved.
//import com.qualcomm.hardware.bosch.BNO055IMU;
// Telemetry
//import org.firstinspires.ftc.robotcore.external.navigation.Acceleration;
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
//import java.util.Locale;
@TeleOp(name = "CDTeleop", group = "Linear Opmode")
class CDTeleop : LinearOpMode(), Runnable {
    // Initialize our local variables with values
    // These "slow" variable is used to control the overall speed of the robot
    // TODO: Work with Drive Team to determine
    private var baseSpeed = 0.70
    private var imuTelemetry = false

    //For setting elevator position using buttons
    //This is where you can set the values of the positions based off telemetry
    //TODO Check that these values are updated for the latest elevator so that freight can be put in proper level of alliance hub
    private var gamePadInput = GamePadInput()
    private var drivePower = DrivePower()
    private var robotSpeed = 0.0
    private var pacmanMode = false
    private lateinit var myHardware: CDHardware

    // Initialize telemetry service
    private lateinit var telemetryService: CDTeleopTelemetry

    // Initialize our teleopThread
    private lateinit var teleopGamepad1Thread: Thread

    // State used for updating telemetry
    //    public Orientation angles;
    //    public Acceleration gravity;
    //    public BNO055IMU imu;
    override fun runOpMode() {
        // Initialize our classes to variables
        myHardware = CDHardware(hardwareMap)
        telemetryService = CDTeleopTelemetry(telemetry)

        // Configure initial variables
        pacmanMode = false

        // Log that the op mode has started
        telemetryService.logStarted()

        // Define teleopThread
        teleopGamepad1Thread = Thread(this)

        //Wait for the driver to press PLAY on the driver station phone
        waitForStart()

        // Start the teleopThread
        teleopGamepad1Thread.start()

        // Polling rate for logging gets set to zero before the while loop
        var i = 0
        while (opModeIsActive()) {

            // Everything Gamepad 2 will be handled between these two comments
            //
            // GAMEPAD 2 Code!
            //
            // End Gamepad 2

            // Telemetry Stuff -
            // need to slow down the logging
            if (i == 10) {
                composeTelemetry(imuTelemetry)
                i = 0
            } else {
                i++
            }
        }
    }

    // Threaded Gamepad 1. Everything Gamepad 1 will happen below.
    override fun run() {
        try {
            val myChassis = CDTeleopDriveChassis(myHardware)

            while (opModeIsActive()) {
                // Everything gamepad 1:

                // User controls for the robot speed overall
                setRobotSpeed(gamepad1)

                // Toggle Pacman Mode
                setPacmanMode(gamepad1)

                // Set x, y, rx
                gamePadInput.refreshGamePadInput(gamepad1)

                // Update drive power to each motor
                drivePower.refreshDrivePower(gamePadInput, pacmanMode)

                // Move robot - drive chassis
                myChassis.setDrivePower(drivePower, robotSpeed)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setRobotSpeed(gamepad: Gamepad) {
        robotSpeed = if (gamepad.left_trigger != 0f) {
            baseSpeed * 1.4
        } else if (gamepad.right_trigger != 0f) {
            baseSpeed * .4
        } else {
            baseSpeed
        }
    }

    private fun setPacmanMode(gamepad: Gamepad) {
        if (gamepad.a) {
            // Flip the boolean to toggle modes for drive constraints
            pacmanMode = !pacmanMode
        }
    }

    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------
    private fun composeTelemetry(imuTelemetry: Boolean) {
        if (imuTelemetry) {
            // TODO
        } else {
            telemetryService.composeTelemetry(gamePadInput, drivePower)
        }
    }
}