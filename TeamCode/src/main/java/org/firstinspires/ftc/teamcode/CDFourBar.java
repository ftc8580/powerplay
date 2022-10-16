package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

public class CDFourBar {
    double fourbarslow = .7; //speed multiplier
    CDHardware robotHardware;
    public boolean fourbarstop;
    public double fourbarposcurrent;
    public double fourbarposlast;
    public double FOURBAR_CURRENT_THRESHOLD;
    public AnalogInput fourbarpot;
    private ElapsedTime fourbartimer = new ElapsedTime();
    //TODO Check if 2 is long enough for timeout here???
    private int fourbartimeout = 2; // timeout for fourbar moves

    public void CDFourbar(CDHardware theHardware){
        robotHardware = theHardware;
        robotHardware.fourbarmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //Added to make ture that the fourbar defaults to brake mode
        robotHardware.fourbarmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.fourbarmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fourbarpot = robotHardware.fourbarpot;
    }
    public void setFourbarPower(double pow) {
        robotHardware.fourbarmotor.setPower(pow * fourbarslow);
    }
    public double getFourbarPotVolts() {
        // Reference https://docs.revrobotics.com/potentiometer/untitled-1#calculating-the-relationship-between-voltage-and-angle
        return (fourbarpot.getVoltage());
    }

    // create variable for counts per motor rev for the fourbar
    //static final double COUNTS_PER_FOURBAR_MOTOR_REV = 288; //Core Hex Motor
    //static final double DRIVE_GEAR_REDUCTION = .52; //This is greater than 1 if geared up

    public boolean setFourbarDirection(String fourbarlocationtarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        boolean fourbarerror = false;
        //TODO  NEED ROBOT: Update fourbarpostarget values below to match readings from robot
        if (fourbarlocationtarget == "ground") {
            boolean error = setFourbarPosition(1.29, autonMode);
        } else if (fourbarlocationtarget == "low") {
            fourbarerror = setFourbarPosition(.58, autonMode);
        } else if (fourbarlocationtarget == "medium") {
            fourbarerror =  setFourbarPosition(2.47, autonMode);
        } else if (fourbarlocationtarget == "high") {
            fourbarerror =  setFourbarPosition(2.87, autonMode);
        }
        return fourbarerror;
    }
    public boolean setFourbarPosition(double fourbarpostarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        //TODO Need to confirm threshold is good
        final double FOURBAR_THRESHOLD_POS = 0.1; // volts
        double fourbarmult; // to set the  speed of the fourbar
        fourbartimer.reset();
        fourbarstop = false; // initially we want the fourbar to move for the while loop
        fourbarposcurrent = 0; //updates every loop at the end, zero to start while loop for comparison
        while (!fourbarstop) {
            //This gets the current fourbar position and sets it to a variable
            if (fourbartimer.seconds() > fourbartimeout) {
                return false;
            }
            fourbarposlast = getFourbarPotVolts(); //updates every loop for the position going into the move.
//            if (fourbarposcurrent == fourbarposlast) {
//                fourbarstop = true;
//                return true; // There was an error, the value didn't change.
//            };
            FOURBAR_CURRENT_THRESHOLD = Math.abs(fourbarposlast - fourbarpostarget); // Check the current gap between target and current position
            //TODO Confirm tolerance of .06 is good. Below uses values of .03 and .01 - Is the slow code ever used?
            if (FOURBAR_CURRENT_THRESHOLD <= .06) { // Stop tolerance
                setFourbarPower(0.0); // need to stop the fourbar before leaving the loop
                fourbarstop = true; // leave the while loop
                return false;
            }
            if (autonMode) {
                fourbarmult = 1.0; // Run full speed or do the ifs
            } else {
                fourbarmult = 0.8;
            }
            if (FOURBAR_CURRENT_THRESHOLD <= .03) { // Prepare to slow tolerance
                if (autonMode) {
                    fourbarmult = .8; // Prepare to slow
                } else {
                    fourbarmult = .6; // Prepare to slow
                }
            } else if (FOURBAR_CURRENT_THRESHOLD <= .01) { // Prepare to stop tolerance
                // Do this before we stop
                if (autonMode) {
                    fourbarmult = .3; // Prepare to stop
                } else {
                    fourbarmult = .3;
                }
            }
            //TODO: Check if -1 or 1 needed here to make it move in the correct direction
            if (fourbarposlast > fourbarpostarget) {
                setFourbarPower(-1 * fourbarmult);
                fourbarstop = false;
            } else if (fourbarposlast < fourbarpostarget) {
                setFourbarPower(1*fourbarmult);
                fourbarstop = false;
            }
            fourbarposcurrent = getFourbarPotVolts(); //updates every loop to see where we landed for lockup detection.
        }
        return false; // Returns false if successfully made the moves, no error.
    }
// 2/3 Digital control hub

    public double getFourbarPos () { return robotHardware.fourbarmotor.getCurrentPosition(); }

    public double getFourbarCurrentThreshold () { return this.FOURBAR_CURRENT_THRESHOLD; }

    public void calibrateZeroFourbar () {
        // Reset the encoder to zero on init
        robotHardware.fourbarmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
