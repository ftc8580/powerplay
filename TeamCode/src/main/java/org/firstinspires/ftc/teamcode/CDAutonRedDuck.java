package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.*;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
// Vuforia TensorFlow imports
import org.firstinspires.ftc.robotcore.external.*;
import org.firstinspires.ftc.robotcore.external.navigation.*;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.*;
import org.firstinspires.ftc.robotcore.external.tfod.*;
import java.util.*;

//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="CDAutonRedDuck", group="Linear Opmode")
//@Disabled
public class CDAutonRedDuck extends LinearOpMode {
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
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
      "Ball",
      "Cube",
      "Duck",
      "Marker"
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

    /**
     * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
     * localization engine.
     */
    private VuforiaLocalizer vuforia;

    /**
     * {@link #tfod} is the variable we will use to store our instance of the TensorFlow Object
     * Detection engine.
     */
    private TFObjectDetector tfod;

    @Override
    public void runOpMode() {
        ElapsedTime myTimer = new ElapsedTime();
        //double moveBackTimer = -1;

        CDHardware myHardware = new CDHardware(hardwareMap);
        CDDriveChassisAuton myChassis = new CDDriveChassisAuton(myHardware);
        CDDuckSpinner myDuckSpinner = new CDDuckSpinner(myHardware);
        CDElevator myElevator = new CDElevator(myHardware);
        CDIntake myIntake = new CDIntake(myHardware);
        CDTurret myTurret = new CDTurret(myHardware);
        // CDGyroscope myGyro = new CDGyroscope();
        CDDistanceSensor myDistanceSensor = new CDDistanceSensor(myHardware);

        //Send telemetry to signify robot waiting
        telemetry.addData("Status", "Resetting Encoders");
//        telemetry.update();
// Setting the Modes, Do not need to change
        myChassis.robotHardware.rightrearmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.leftrearmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myChassis.robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        myTurret.robotHardware.turretmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        myChassis.robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myChassis.robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        myTurret.robotHardware.turretmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);


        //Send telemetry to indicate successful Encoder reset
        telemetry.addData("MotorStartPos (RR, RF, LR, LF)", " %7d %7d %7d %7d", myChassis.robotHardware.rightrearmotor.getCurrentPosition(), myChassis.robotHardware.rightfrontmotor.getCurrentPosition(), myChassis.robotHardware.leftrearmotor.getCurrentPosition(), myChassis.robotHardware.leftfrontmotor.getCurrentPosition());
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
            tfod.setZoom(2.5, 16.0/9.0);
        }

        /** Wait for the game to begin */
        telemetry.addData(">", "Press Play to start op mode");
//        telemetry.update();


        // Logic below is that we want it to start recognizing when we init but not after we start
           while (!opModeIsActive()) {
                if (tfod != null) {
                    // getUpdatedRecognitions() will return null if no new information is available since
                    // the last time that call was made.
                    List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                    if (updatedRecognitions != null) {
                        telemetry.addData("# Object Detected", updatedRecognitions.size());

                        // step through the list of recognitions and display boundary info.
                        int i = 0;
                        for (Recognition recognition : updatedRecognitions) {
                            telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                            telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                    recognition.getLeft(), recognition.getTop());
                            telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                    recognition.getRight(), recognition.getBottom());
                            i++;
                        }
                        telemetry.update();
                    }
                }
            }
                   //Wait fo the game to start (driver presses PLAY)
            waitForStart();
            myTimer.reset();
        // TODO: Need to use the timer to program the robot in Auton competition

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

            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -3, 5);
            myTurret.setTurretDirection("center");
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90, 10);
            //myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -1, 5);
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -30, 8);
            sleep(200);
            myDuckSpinner.setDuckSpinnerPower(-.6);
            sleep(2500);
            myDuckSpinner.setDuckSpinnerPower(0);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 32, 10.0);
            myElevator.setElevatorPosition(26);
            myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -5, 10.0);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 33, 10.0);
            myTurret.setTurretDirection("center");
            myIntake.setIntakePower(.4);
            sleep(1000);
            myIntake.setIntakePower(0);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -29, 10.0);
            myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 11, 5);
            myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -6, 10.0);
            myElevator.setElevatorPosition(7);


            //myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 30, 20.0); //Move forward 30 inches with 10 second timeout
                //sleep(250); //optional pause after each move in milliseconds
                //myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, -20, 10.0); //Move back 20 inches with 10 second timeout
                //myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 10, 5); //Move right 10 inches with 5 second timeout
                //myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -15, 15); //Move left 15 inches with 15 second timeout
                //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, 90, 10); //Turn right 90 degrees with 10 second timeout
                //myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -180, 20); //Turn left 180 degrees with a 20 second timeout
                //sleep(250); //optional pause after each move in milliseconds
                //myElevator.setElevatorPosition(28); //Move elevator to middle position. Do not set outside of range 2.5-39.
                //myTurret.setTurretDirection("center"); //SSet turret position to center
                //myTurret.setTurretDirection("right");  //Set turret position -90 (right)
                //myTurret.setTurretDirection("left");   //Set turret position 90 (left)

                //  myIntake.setIntakePower(1.0);
                //  myDuckSpinner.setDuckSpinnerPower(.7);

//                myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -4.5, 3);
//                myChassis.encoderDriveTurn(CDDriveChassisAuton.TURN_SPEED, -90.0, 3);
//                myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -34.0, 3);


                //Run until the end of the match (Driver presses STOP)
                //telemetry.addData("MotorCurrentPos (RR, RF, LR, LF)", " %7d %7d %7d %7d", myChassis.robotHardware.rightrearmotor.getCurrentPosition(), myChassis.robotHardware.rightfrontmotor.getCurrentPosition(), myChassis.robotHardware.leftrearmotor.getCurrentPosition(), myChassis.robotHardware.leftfrontmotor.getCurrentPosition());
                //telemetry.update();
                //sleep (5000);
                //TODO figure out how to show target position. 3 lines above will give final position.
                //TODO: Add telemetry for IMU Gyro
        }
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
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the TensorFlow Object Detection engine.
    }

    /**
     * Initialize the TensorFlow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
            "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }
}

