package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDGrabber;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name = "CDAutonBase", group = "Linear Opmode")
//@Disabled
public class CDAutonBase extends LinearOpMode {

    // Modern Phones / Control Hub
    private static final String TFOD_MODEL_FILE = "/sdcard/FIRST/tflitemodels/model_20221106_122744.tflite";

    private static final String[] LABELS = {
//            "1 Bolt",
//            "2 Bulb",
//            "3 Panel"
            "1 Pacman",
            "2 Cherry",
            "3 Ghost"
    };
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
    private static final String VUFORIA_KEY =
            "AXXCHEP/////AAABmXaXN8FwREvRjitK+vRPfteLJ/QYssqHKZPw8TG5AIe32q0tfagLtBwY9AVaYhKV6y8cT8QAXoG1wmRjTbMr8SI5SC+zUGqel33vbcZ9t/aL49vYwNip2WpTdueyjRuHGoXjQyeyapmwBLR50nr8+lryLNT0Cp+anlhaVhExK/0Tkzo9BkND71CgEbtQtOfAkPEbg9uUhMb5DeIpjLdjFscgqrfza6b7oztxr5cmiumIKr1pru4GCT0xafq+Tw1BkLoF95QAEa+57DLJJRxPPfw8bUug+1lGTKlLNLPA/waEFtgsGyGDomBxsQgObyGdihPyro0WR6pvM5kvKY2v6mpoBJXQiTpby7CNRiqISXCS";

    /*
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;
    //
//    /**
//     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
//     * Detection engine.
//     */
    private TFObjectDetector tfod;


    // Public variables for use in executeautons method
    public CDHardware myHardware;
    public CDDriveChassisAuton myChassis;
    public CDFourBar myFourbar;
    public CDArm myArm;
    public CDPickup myPickup;
    public CDGrabber myGrabber;

    @Override
    public void runOpMode() {
        ElapsedTime myTimer = new ElapsedTime();
        //double moveBackTimer = -1;
        myHardware = new CDHardware(hardwareMap);
        myChassis = new CDDriveChassisAuton(myHardware);
        myFourbar = new CDFourBar(hardwareMap);
        myArm = new CDArm(myHardware);
        myPickup = new CDPickup(myHardware);
        myGrabber = new CDGrabber(myHardware);

        //Send telemetry to signify robot waiting
        telemetry.addData("Status", "Resetting Encoders");
        telemetry.update();
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
        initVuforia();
        initTfod();

        /**
         * Activate TensorFlow Object Detection before we wait for the start command.
         * Do it here so that the Camera Stream window will have the TensorFlow annotations visible.
         **/
        if (tfod != null) {
            tfod.activate();

            // The TensorFlow software will scale the input images from the camera to a lower resolution.
            // This can result in lower detection accuracy at longer distances (> 55cm or 22").
            // If your target is at distance greater than 50 cm (20") you can adjust the magnification value
            // to artificially zoom in to the center of image.  For best results, the "aspectRatio" argument
            // should be set to the value of the images used to create the TensorFlow Object Detection model
            // (typically 16/9).
            tfod.setZoom(1.0, 16.0 / 9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();
        // Logic below is that we want it to start recognizing when we init but not after we start
        while (!opModeIsActive()) {
            if (tfod != null) {
//                // getUpdatedRecognitions() will return null if no new information is available since
//                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Objects Detected", updatedRecognitions.size());

                    // step through the list of recognitions and display image position/size information for each one
                    // Note: "Image number" refers to the randomized image orientation/number
                    for (Recognition recognition : updatedRecognitions) {
                        double col = (recognition.getLeft() + recognition.getRight()) / 2;
                        double row = (recognition.getTop() + recognition.getBottom()) / 2;
                        double width = Math.abs(recognition.getRight() - recognition.getLeft());
                        double height = Math.abs(recognition.getTop() - recognition.getBottom());

                        telemetry.addData("", " ");
                        telemetry.addData("Image", "%s (%.0f %% Conf.)", recognition.getLabel(), recognition.getConfidence() * 100);
                        telemetry.addData("- Position (Row/Col)", "%.0f / %.0f", row, col);
                        telemetry.addData("- Size (Width/Height)", "%.0f / %.0f", width, height);
                    }
                    telemetry.update();
                }
            }
        }
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

//        public static double getDuckDeliveryLocation ( int duckLocation, CDElevator
//        myElevator){
//            double[] duckDeliveryPositions = new double[]{myElevator.defaultelevatorposition, myElevator.elevatorposbottom, myElevator.elevatorposmiddle, myElevator.elevatorpostop};
//            return duckDeliveryPositions[duckLocation];
//        }


    public void executeAuton() {
        // This will be overriden by the Auton design class
        sleep(2500);
    }

    /**
     * Initialize the Vuforia localization engine.
     */
    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");
        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    //
//    /**
//     * Initialize the TensorFlow Object Detection engine.
//     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.70f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.setClippingMargins(250, 175, 275, 175);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
//        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}

