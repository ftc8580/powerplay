package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;
//
//import org.firstinspires.ftc.robotcore.external.ClassFactory;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
//import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
//import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
//import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;


@Autonomous(name="CDAutonBase", group="Linear Opmode")
@Disabled
public class CDAutonBase extends LinearOpMode {
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
//    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
//    private static final String[] LABELS = {
//            "Ball",
//            "Cube",
//            "Duck",
//            "Marker"
//    };

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
    //private static final String VUFORIA_KEY =
     //       "AXXCHEP/////AAABmXaXN8FwREvRjitK+vRPfteLJ/QYssqHKZPw8TG5AIe32q0tfagLtBwY9AVaYhKV6y8cT8QAXoG1wmRjTbMr8SI5SC+zUGqel33vbcZ9t/aL49vYwNip2WpTdueyjRuHGoXjQyeyapmwBLR50nr8+lryLNT0Cp+anlhaVhExK/0Tkzo9BkND71CgEbtQtOfAkPEbg9uUhMb5DeIpjLdjFscgqrfza6b7oztxr5cmiumIKr1pru4GCT0xafq+Tw1BkLoF95QAEa+57DLJJRxPPfw8bUug+1lGTKlLNLPA/waEFtgsGyGDomBxsQgObyGdihPyro0WR6pvM5kvKY2v6mpoBJXQiTpby7CNRiqISXCS";

    /*
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
//    private VuforiaLocalizer vuforia;
//
//    /**
//     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
//     * Detection engine.
//     */
//    private TFObjectDetector tfod;
//
//    public static int findClosest(int[] arr, int target, int duckWeDoNotSee) {
//        int idx = 0;
//        int dist = Math.abs(arr[0] - target);
//
//        for (int i = 1; i < arr.length; i++) {
//            int cdist = Math.abs(arr[i] - target);
//
//            if (cdist < dist) {
//                idx = i;
//                dist = cdist;
//            }
//        }
//
////        return arr[idx];// In case we want the value someday
//        if (duckWeDoNotSee == 1) {
//            return idx + 1;
//        } else {
//            return idx;
//        }
//    }

    // Public variables for use in executeautons method
    public CDHardware myHardware;
    public CDDriveChassisAuton myChassis;
    public CDFourBar myFourbar;
    public CDArm myArm;
    public CDPickup myPickup;

    @Override
    public void runOpMode() {
        ElapsedTime myTimer = new ElapsedTime();
        //double moveBackTimer = -1;
        myHardware = new CDHardware(hardwareMap);
        myChassis = new CDDriveChassisAuton(myHardware);
        myFourbar = new CDFourBar(myHardware);
        myArm = new CDArm(myHardware);
        myPickup = new CDPickup(myHardware);

        //Send telemetry to signify robot waiting
        telemetry.addData("Status", "Resetting Encoders");
//        telemetry.update();
        // Setting the Modes, Do not need to change
        myChassis.robotHardware.rightRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.leftRearMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        myTurret.robotHardware.fourbarmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER); \\commented because using pot for position

        myChassis.robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//        myTurret.robotHardware.fourbarmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER); \\commented because using pot for position

//        myArm.robotHardware.armmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//        myArm.robotHardware.armmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER)

        //Send telemetry to indicate successful Encoder reset
        telemetry.addData("MotorStartPos (RR, RF, LR, LF)", " %7d %7d %7d %7d", myChassis.robotHardware.rightRearMotor.getCurrentPosition(), myChassis.robotHardware.rightFrontMotor.getCurrentPosition(), myChassis.robotHardware.leftRearMotor.getCurrentPosition(), myChassis.robotHardware.leftFrontMotor.getCurrentPosition());
        telemetry.addData("Status", "Initialized");

        // This new code is for the object detection
        // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
        // first.
//        initVuforia();
//        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
//        if (tfod != null) {
//            tfod.activate();
//
//            // The TensorFlow software will scale the input images from the camera to a lower resolution.
//            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
//            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
//            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
//            // should be set to the value of the images used to create the TensorFlow Object Detection model
//            // (typically 16/9).
//            tfod.setZoom(1.4, 16.0/9.0);
//        }

        /** Wait for the game to begin */
//        telemetry.addData(">", "Press Play to start op mode");
////        telemetry.update();
//        initTokenWeDoNotSee();
        // CD 8580 Use a variable to catch last location of the duck
//        duckLocation = duckWeDoNotSee; // If we find a duck and relocate it, then it will not be IN THE RIGHT!

//        int zeroThreshold = 0; // Null
//        int leftThreshold = 300; // Setup for only 2 ducks
//        int rightThreshold = 800; // Setup for only 2 ducks
//        int[] arr = new int[] {zeroThreshold, leftThreshold, rightThreshold};
//        // Logic below is that we want it to start recognizing when we init but not after we start
//        while (!opModeIsActive()) {
//            if (tfod != null) {
//                // getUpdatedRecognitions() will return null if no new information is available since
//                // the last time that call was made.
//                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
//                if (updatedRecognitions != null) {
//                    telemetry.addData("# Object Detected", updatedRecognitions.size());
//                    if (updatedRecognitions.size() == 0) {
//                        duckLocation = duckWeDoNotSee;
//                        telemetry.addData("Token Location:", duckLocation);
////                        telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));
//
//                    }
////                    telemetry.addData("Token Location:", duckLocation);
////                    telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));
//                    int i = 0;
//                    // step through the list of recognitions and display boundary info.
//                    for (Recognition recognition : updatedRecognitions) {
//                        telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
//                        telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
//                                recognition.getLeft(), recognition.getTop());
////                        telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
////                                recognition.getRight(), recognition.getBottom());
//                        // CD 8580 Captures a duck for the object, if we train our own, we would relabel it here for capture
//                        if ((recognition.getLabel().equals("Duck")) || (recognition.getLabel().equals("Cube"))) {
//                            duckLocation = findClosest(arr, Math.round(recognition.getLeft()), duckWeDoNotSee);
//                            telemetry.addData("Token Location:", duckLocation);
////                            telemetry.addData("Found A Token For Delivery Location:", getDuckDeliveryLocation(duckLocation, myElevator));
//                        }
//                    }
//                    i++;
//                }
//
//                telemetry.update();
//            }
//        }
        //Wait fo the game to start (driver presses PLAY)
        waitForStart();
        myTimer.reset();
        //Step through each leg of path
        //Note: Reverse movement is obtained by selling a negative distance not speed
        /*Drive code is written using 3 methods
         *     speed should be either DRIVE_SPEED or TURN_SPEED
         * These assumes that each movement is relative to the last stopping place
         * 1. encoderDriveStraight (speed, straightInches, straightTimeout)
         * 2. encoderDriveStrafe (speed, strafeInches, strafeTimeout)
         * 3. encodeDriveTurn (speed, turnDeg, turnTimeout)
         */

        if (opModeIsActive()) {
            executeAuton();
        }
    }

//    public static double getDuckDeliveryLocation(int duckLocation, CDElevator myElevator){
//        double[] duckDeliveryPositions = new double[] {myElevator.defaultelevatorposition, myElevator.elevatorposbottom, myElevator.elevatorposmiddle, myElevator.elevatorpostop};
//        return duckDeliveryPositions[duckLocation];
//    }

//    public void initTokenWeDoNotSee() {
//        // Intends to be overridden
//        duckWeDoNotSee = 1;
//    }
    public void executeAuton() {
        // This will be overriden by the Auton design class
        sleep(2500);
    }
    /**
     * Initialize the Vuforia localization engine.
     */
//    private void initVuforia() {
//        /*
//         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
//         */
//        Parameters parameters = new Parameters();
//
//        parameters.vuforiaLicenseKey = VUFORIA_KEY;
//        parameters.cameraDirection = CameraDirection.BACK;
//
//        //  Instantiate the Vuforia engine
//        vuforia = ClassFactory.getInstance().createVuforia(parameters);
//
//        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
//    }
//
//    /**
//     * Initialize the TensorFlow Object Detection engine.
//     */
//    private void initTfod() {
//        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
//                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
//        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
//        tfodParameters.minResultConfidence = 0.8f;
//        tfodParameters.isModelTensorFlow2 = true;
//        tfodParameters.inputSize = 320;
//        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
//        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
//    }
}

