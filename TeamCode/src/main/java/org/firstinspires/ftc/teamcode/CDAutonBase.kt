package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.ClassFactory
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector
import kotlin.math.abs
import kotlin.math.roundToInt

@Autonomous(name = "CDAutonBase", group = "Linear Opmode")
@Disabled
open class CDAutonBase : LinearOpMode() {
    // Public variables for use in executeAuton method
    private lateinit var myHardware: CDHardware
    lateinit var myChassis: CDDriveChassisAuton
    var duckLocation = 0

    @JvmField
    var duckWeDoNotSee = 0

    /**
     * [.vuforia] is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private var vuforia: VuforiaLocalizer? = null

    /**
     * [.detector] is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private lateinit var detector: TFObjectDetector
    override fun runOpMode() {
        val myTimer = ElapsedTime()
        //double moveBackTimer = -1;
        myHardware = CDHardware(hardwareMap)
        myChassis = CDDriveChassisAuton(myHardware)

        //Send telemetry to signify robot waiting
        telemetry.addData("Status", "Resetting Encoders")
        //        telemetry.update();
        // Setting the Modes, Do not need to change
        myChassis.robotHardware.rightRearMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        myChassis.robotHardware.rightFrontMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        myChassis.robotHardware.leftRearMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        myChassis.robotHardware.leftFrontMotor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        //        myTurret.robotHardware.turretmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        myChassis.robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        myChassis.robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        myChassis.robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_USING_ENCODER
        //        myTurret.robotHardware.turretmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //Send telemetry to indicate successful Encoder reset
        telemetry.addData(
            "MotorStartPos (RR, RF, LR, LF)",
            " %7d %7d %7d %7d",
            myChassis.robotHardware.rightRearMotor.currentPosition,
            myChassis.robotHardware.rightFrontMotor.currentPosition,
            myChassis.robotHardware.leftRearMotor.currentPosition,
            myChassis.robotHardware.leftFrontMotor.currentPosition
        )
        telemetry.addData("Status", "Initialized")

        // This new code is for the object detection
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
        initVuforia()
        initDetector()
        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         */
        detector.activate()

        // The TensorFlow software will scale the input images from the camera to a lower resolution.
        // This can result in lower detection accuracy at longer distances (> 55cm or 22").
        // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
        // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
        // should be set to the value of the images used to create the TensorFlow Object Detection model
        // (typically 16/9).
        detector.setZoom(1.4, 16.0 / 9.0)

        /** Wait for the game to begin  */
        telemetry.addData(">", "Press Play to start op mode")
        //        telemetry.update();
        initTokenWeDoNotSee()
        // CD 8580 Use a variable to catch last location of the duck
        duckLocation =
            duckWeDoNotSee // If we find a duck and relocate it, then it will not be IN THE RIGHT!
        val zeroThreshold = 0 // Null
        val leftThreshold = 300 // Setup for only 2 ducks
        val rightThreshold = 800 // Setup for only 2 ducks
        val arr = intArrayOf(zeroThreshold, leftThreshold, rightThreshold)
        // Logic below is that we want it to start recognizing when we init but not after we start
        while (!opModeIsActive()) {
            // getUpdatedRecognitions() will return null if no new information is available since
            // the last time that call was made.
            val updatedRecognitions = detector.updatedRecognitions
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size)
                if (updatedRecognitions.size == 0) {
                    duckLocation = duckWeDoNotSee
                    telemetry.addData("Token Location:", duckLocation)
                    //                        telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));
                }
                //                    telemetry.addData("Token Location:", duckLocation);
//                    telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));
                // step through the list of recognitions and display boundary info.
                for ((i, recognition) in updatedRecognitions.withIndex()) {
                    telemetry.addData(String.format("label (%d)", i), recognition.label)
                    telemetry.addData(
                        String.format("  left,top (%d)", i), "%.03f , %.03f",
                        recognition.left, recognition.top
                    )
                    //                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
//                                recognition.getRight(), recognition.getBottom());
                    // CD 8580 Captures a duck for the object, if we train our own, we would relabel it here for capture
                    if (recognition.label == "Duck" || recognition.label == "Cube") {
                        duckLocation =
                            findClosest(arr, recognition.left.roundToInt(), duckWeDoNotSee)
                        telemetry.addData("Token Location:", duckLocation)
                        //                            telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));
                    }
                }
            }
            telemetry.update()
        }
        //Wait fo the game to start (driver presses PLAY)
        waitForStart()
        myTimer.reset()
        //Step through each leg of path
        //Note: Reverse movement is obtained by selling a negative distance not speed
        /*Drive code is written using 3 methods
         *     speed should be either DRIVE_SPEED or TURN_SPEED
         * These assumes that each movement is relative to the last stopping place
         * 1. encoderDriveStraight (speed, straightInches, straightTimeout)
         * 2. encoderDriveStrafe (speed, strafeInches, strafeTimeout)
         * 3. encodeDriveTurn (speed, turnDeg, turnTimeout)
         */if (opModeIsActive()) {
            executeAuton()
        }
    }

    //    public static double getDuckDeliveryLocation(int duckLocation, CDElevator myElevator){
    //        double[] duckDeliveryPositions = new double[] {myElevator.defaultelevatorposition, myElevator.elevatorposbottom, myElevator.elevatorposmiddle, myElevator.elevatorpostop};
    //        return duckDeliveryPositions[duckLocation];
    //    }
    open fun initTokenWeDoNotSee() {
        // Intends to be overridden
        duckWeDoNotSee = 1
    }

    open fun executeAuton() {
        // This will be overriden by the Auton design class
        sleep(2500)
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private fun initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        val parameters = VuforiaLocalizer.Parameters()
        parameters.vuforiaLicenseKey = VUFORIA_KEY
        parameters.cameraDirection = CameraDirection.BACK

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters)

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private fun initDetector() {
        val detectorMonitorViewId = hardwareMap.appContext.resources.getIdentifier(
            "detectorMonitorViewId", "id", hardwareMap.appContext.packageName
        )
        val detectorParameters = TFObjectDetector.Parameters(detectorMonitorViewId)
        detectorParameters.minResultConfidence = 0.8f
        detectorParameters.isModelTensorFlow2 = true
        detectorParameters.inputSize = 320
        detector = ClassFactory.getInstance().createTFObjectDetector(detectorParameters, vuforia)
        detector.loadModelFromAsset(TFOD_MODEL_ASSET, *LABELS)
    }

    companion object {
        /* Note: This sample uses the all-objects Tensor Flow model (FreightFrenzy_BCDM.tflite), which contains
     * the following 4 detectable objects
     *  0: Ball,
     *  1: Cube,
     *  2: Duck,
     *  3: Marker (duck location tape marker)
     *
     *  Two additional model assets are available which only contain a subset of the objects:
     *  FreightFrenzy_BC.tflite  0: Ball,  1: Cube
     *  FreightFrenzy_DM.tflite  0: Duck,  1: Marker
     */
        private const val TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite"
        private val LABELS = arrayOf(
            "Ball",
            "Cube",
            "Duck",
            "Marker"
        )

        /*
     * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
     * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
     * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
     * web site at https://developer.vuforia.com/license-manager.
     *
     * Vuforia license keys are always 380 characters long, and look as if they contain mostly
     * random data. As an example, here is a example of a fragment of a valid key:
     *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
     * Once you've obtained a license key, copy the string from the Vuforia web site
     * and paste it in to your code on the next line, between the double quotes.
     */
        private const val VUFORIA_KEY =
            "AXXCHEP/////AAABmXaXN8FwREvRjitK+vRPfteLJ/QYssqHKZPw8TG5AIe32q0tfagLtBwY9AVaYhKV6y8cT8QAXoG1wmRjTbMr8SI5SC+zUGqel33vbcZ9t/aL49vYwNip2WpTdueyjRuHGoXjQyeyapmwBLR50nr8+lryLNT0Cp+anlhaVhExK/0Tkzo9BkND71CgEbtQtOfAkPEbg9uUhMb5DeIpjLdjFscgqrfza6b7oztxr5cmiumIKr1pru4GCT0xafq+Tw1BkLoF95QAEa+57DLJJRxPPfw8bUug+1lGTKlLNLPA/waEFtgsGyGDomBxsQgObyGdihPyro0WR6pvM5kvKY2v6mpoBJXQiTpby7CNRiqISXCS"

        fun findClosest(arr: IntArray, target: Int, duckWeDoNotSee: Int): Int {
            var idx = 0
            var dist = abs(arr[0] - target)
            for (i in 1 until arr.size) {
                val cdist = abs(arr[i] - target)
                if (cdist < dist) {
                    idx = i
                    dist = cdist
                }
            }

//        return arr[idx];// In case we want the value someday
            return if (duckWeDoNotSee == 1) {
                idx + 1
            } else {
                idx
            }
        }
    }
}