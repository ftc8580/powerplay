package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class CDFourBar {
    double fourBarSlowSpeedMultiplier = .7;
    public CDHardware robotHardware;
    public boolean fourBarStop;
    public double fourBarPositionCurrent;
    public double fourBarPositionLast;
    public double FOURBAR_CURRENT_THRESHOLD;
    public AnalogInput fourBarPotentiometer;
    public boolean autonMode;
    private CDRuntime runtime = new CDRuntime();

    //TODO Check if 2 is long enough for timeout here???
    private int fourBarTimeout = 2; // timeout for fourbar moves

    public CDFourBar(CDHardware theHardware) {
        autonMode = false;
        robotHardware = theHardware;
        robotHardware.fourBarMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //Added to make ture that the fourbar defaults to brake mode
        robotHardware.fourBarMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.fourBarMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fourBarPotentiometer = robotHardware.fourBarPotentiometer;
        double fourBarPositionDown = .230;
        setFourbarPosition(fourBarPositionDown);
    }

    public synchronized void setFourBarPower(double pow) {
        robotHardware.fourBarMotor.setPower(pow * fourBarSlowSpeedMultiplier);
    }

    public double getFourBarPotentiometerVolts() {
        // Reference https://docs.revrobotics.com/potentiometer/untitled-1#calculating-the-relationship-between-voltage-and-angle
        return fourBarPotentiometer.getVoltage();
    }

    // create variable for counts per motor rev for the fourbar
    //static final double COUNTS_PER_FOURBAR_MOTOR_REV = 288; //Core Hex Motor
    //static final double DRIVE_GEAR_REDUCTION = .52; //This is greater than 1 if geared up

    public synchronized boolean setFourbarDirection(String fourBarLocationTarget) {
        // This method will return false for successful turn or true for an error.
        boolean fourBarError = false;
        //TODO  NEED ROBOT: Update fourbarpostarget values below to match readings from robot
        if (fourBarLocationTarget == "ground") {
            boolean error = setFourbarPosition(.29);
        } else if (fourBarLocationTarget == "low") {
            fourBarError = setFourbarPosition(.58);
        } else if (fourBarLocationTarget == "medium") {
            fourBarError = setFourbarPosition(2.47);
        } else if (fourBarLocationTarget == "high") {
            fourBarError = setFourbarPosition(2.87);
        }
        return fourBarError;
    }

    public synchronized boolean setFourbarPosition(double fourBarPositionTarget) {
        // This method will return false for successful turn or true for an error.
        //TODO Need to confirm threshold is good
        final double FOURBAR_THRESHOLD_POS = 0.01; // volts
        double fourBarSpeedMultiple; // to set the  speed of the fourbar
        runtime.reset();
        fourBarStop = false; // initially we want the fourbar to move for the while loop
        fourBarPositionCurrent = 0; //updates every loop at the end, zero to start while loop for comparison
        while (!fourBarStop) {
            //This gets the current fourbar position and sets it to a variable
            if (runtime.seconds() > fourBarTimeout) {
                return false;
            }
            fourBarPositionLast = getFourBarPotentiometerVolts(); //updates every loop for the position going into the move.
//            if (fourbarposcurrent == fourbarposlast) {
//                fourbarstop = true;
//                return true; // There was an error, the value didn't change.
//            };
            FOURBAR_CURRENT_THRESHOLD = Math.abs(fourBarPositionLast - fourBarPositionTarget); // Check the current gap between target and current position
            //TODO Confirm tolerance of .06 is good. Below uses values of .03 and .01 - Is the slow code ever used?
            if (FOURBAR_CURRENT_THRESHOLD <= .06) { // Stop tolerance
                setFourBarPower(0.0); // need to stop the fourbar before leaving the loop
                fourBarStop = true; // leave the while loop
                return false;
            }
            if (autonMode) {
                fourBarSpeedMultiple = 1.0; // Run full speed or do the ifs
            } else {
                fourBarSpeedMultiple = 0.8;
            }
            if (FOURBAR_CURRENT_THRESHOLD <= .03) { // Prepare to slow tolerance
                if (autonMode) {
                    fourBarSpeedMultiple = .8; // Prepare to slow
                } else {
                    fourBarSpeedMultiple = .6; // Prepare to slow
                }
            } else if (FOURBAR_CURRENT_THRESHOLD <= .01) { // Prepare to stop tolerance
                // Do this before we stop
                if (autonMode) {
                    fourBarSpeedMultiple = .3; // Prepare to stop
                } else {
                    fourBarSpeedMultiple = .3;
                }
            }
            //TODO: Check if -1 or 1 needed here to make it move in the correct direction
            if (fourBarPositionLast > fourBarPositionTarget) {
                setFourBarPower(-1 * fourBarSpeedMultiple);
                fourBarStop = false;
            } else if (fourBarPositionLast < fourBarPositionTarget) {
                setFourBarPower(1 * fourBarSpeedMultiple);
                fourBarStop = false;
            }
            fourBarPositionCurrent = getFourBarPotentiometerVolts(); //updates every loop to see where we landed for lockup detection.
        }
        return false; // Returns false if successfully made the moves, no error.
    }
// 2/3 Digital control hub

    public double getFourBarPosition() {
        return robotHardware.fourBarMotor.getCurrentPosition();
    }

    public double getFourBarCurrentThreshold() {
        return this.FOURBAR_CURRENT_THRESHOLD;
    }

    public void calibrateZeroFourBar() {
        // Reset the encoder to zero on init
        robotHardware.fourBarMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
