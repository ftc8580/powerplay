package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import java.lang.Exception
import kotlin.math.abs
import kotlin.math.pow

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

    // Initialize our local variables for use later in telemetry or other methods
    private var y = 0.0

    //For setting elevator position using buttons
    //This is where you can set the values of the positions based off telemetry
    //TODO Check that these values are updated for the latest elevator so that freight can be put in proper level of alliance hub
    private var x = 0.0
    private var rx = 0.0
    private var leftFrontPower = 0.0
    private var leftRearPower = 0.0
    private var rightFrontPower = 0.0
    private var rightRearPower = 0.0
    private var robotSpeed = 0.0
    private var constrainMovement = false
    private lateinit var myHardware: CDHardware

    // Initialize our teleopThread
    private lateinit var teleopGamepad1Thread: Thread

    // State used for updating telemetry
    //    public Orientation angles;
    //    public Acceleration gravity;
    //    public BNO055IMU imu;
    override fun runOpMode() {
        // Initialize our classes to variables
        myHardware = CDHardware(hardwareMap)

        // Configure initial variables
        constrainMovement = false
        //Wait for the driver to press PLAY on the driver station phone
        // make a new thread
        telemetry.addData("Status", "Fully Initialized")
        telemetry.update()
        teleopGamepad1Thread = Thread(this) // Define teleopThread
        waitForStart()
        //Run until the end (Driver presses STOP)
        teleopGamepad1Thread.start() // Start the teleopThread

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
            val myChassis = CDDriveChassis(myHardware)
            while (opModeIsActive()) {
                // Everything gamepad 1:
                // User controls for the robot speed overall
                robotSpeed = if (gamepad1.left_trigger != 0f) {
                    baseSpeed * 1.4
                } else if (gamepad1.right_trigger != 0f) {
                    baseSpeed * .4
                } else {
                    baseSpeed
                }
                if (gamepad1.a) {
                    // Flip the boolean to toggle modes for drive constraints
                    constrainMovement = !constrainMovement
                }
                // We cubed the inputs to make the inputs more responsive
                y = gamepad1.left_stick_y.toDouble().pow(3.0) // Remember, this is reversed!
                x = (gamepad1.left_stick_x * -1.1).pow(3.0) // Counteract imperfect strafing
                rx = gamepad1.right_stick_x.toDouble().pow(3.0) * 0.5 //Reduced turn speed to make it easier to control

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio, but only when
                // at least one is out of the range [-1, 1]
                val denominator = (abs(y) + abs(x) + abs(rx)).coerceAtLeast(1.0)
                if (constrainMovement) {
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
                //move robot - drive chassis
                myChassis.setLeftFrontPower(leftFrontPower * robotSpeed)
                myChassis.setLeftRearPower(leftRearPower * robotSpeed)
                myChassis.setRightFrontPower(rightFrontPower * robotSpeed)
                myChassis.setRightRearPower(rightRearPower * robotSpeed)
            }
            // End gamepad 1
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //----------------------------------------------------------------------------------------------
    // Telemetry Configuration
    //----------------------------------------------------------------------------------------------
    private fun composeTelemetry(imuTelemetry: Boolean) {
        telemetry.clearAll()
        if (imuTelemetry) {
            telemetry.clearAll() // Just doing something to satisfy the placeholder.
            ////             At the beginning of each telemetry update, grab a bunch of data
////             from the IMU that we will then display in separate lines.
////            Comment block disables telemetry reporting!
//            telemetry.addAction(new Runnable() {
//                @Override
//                public void run() {
//                    // Acquiring the angles is relatively expensive; we don't want
//                    // to do that in each of the three items that need that info, as that's
//                    // three times the necessary expense.
//                    angles = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
//                    gravity = imu.getGravity();
//                }
//            });
//            //telemetry.addData("heading ", heading);
//            telemetry.addLine()
//                    .addData("status", new Func<String>() {
//                        @Override public String value() {
//                            return imu.getSystemStatus().toShortString();
//                        }
//                    })
//                    .addData("calib", new Func<String>() {
//                        @Override public String value() {
//                            return imu.getCalibrationStatus().toString();
//                        }
//                    });
//
//            telemetry.addLine()
//                    .addData("heading", new Func<String>() {
//                        @Override public String value() {
//                            return formatAngle(angles.angleUnit, angles.firstAngle);
//                        }
//                    })
//                    .addData("roll", new Func<String>() {
//                        @Override public String value() {
//                            return formatAngle(angles.angleUnit, angles.secondAngle);
//                        }
//                    })
//                    .addData("pitch", new Func<String>() {
//                        @Override public String value() {
//                            return formatAngle(angles.angleUnit, angles.thirdAngle);
//                        }
//                    });
//
//            telemetry.addLine()
//                    .addData("grvty", new Func<String>() {
//                        @Override public String value() {
//                            return gravity.toString();
//                        }
//                    })
//                    .addData("mag", new Func<String>() {
//                        @Override public String value() {
//                            return String.format(Locale.getDefault(), "%.3f",
//                                    Math.sqrt(gravity.xAccel*gravity.xAccel
//                                            + gravity.yAccel*gravity.yAccel
//                                            + gravity.zAccel*gravity.zAccel));
//                        }
//                    });
            // Loop and update the dashboard
        } else {
            telemetry.addData("y input", "%.2f", y)
            telemetry.addData("x input", "%.2f", x)
            telemetry.addData("rx input", "%.2f", rx)
            telemetry.addData("motorLF ", "%.2f", leftFrontPower)
            telemetry.addData("motorRF ", "%.2f", rightFrontPower)
            telemetry.addData("motorLR ", "%.2f", leftRearPower)
            telemetry.addData("motorRR ", "%.2f", rightRearPower)
        }
        // Loop and update the dashboard
        telemetry.update()
    } //----------------------------------------------------------------------------------------------
    // Formatting
    //----------------------------------------------------------------------------------------------
    //    String formatAngle(AngleUnit angleUnit, double angle) {
    //        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    //    }
    //
    //    String formatDegrees(double degrees){
    //        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    //    }
}