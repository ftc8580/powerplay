package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class CDFourBar extends SubsystemBase {
    private final static double fourBarSlowSpeedMultiplier = .7;

    private final AnalogInput fourBarPotentiometer;
    private final CDArm arm;
    private final CDRuntime runtime = new CDRuntime();
    private final DcMotor fourBarMotor;

    public boolean fourBarStop;
    public double fourBarPositionCurrent;
    public double fourBarPositionLast;
    public double FOURBAR_CURRENT_THRESHOLD;

    public CDFourBar(CDHardware theHardware) {
        fourBarMotor = theHardware.fourBarMotor;
        fourBarPotentiometer = theHardware.fourBarPotentiometer;
        arm = new CDArm(theHardware);

        fourBarMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //Added to make sure that the fourBar defaults to brake mode
        fourBarMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fourBarMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setFourBarPower(double pow) {
        fourBarMotor.setPower(pow * fourBarSlowSpeedMultiplier);
    }

    public double getFourBarPotentiometerVolts() {
        // Reference https://docs.revrobotics.com/potentiometer/untitled-1#calculating-the-relationship-between-voltage-and-angle
        return fourBarPotentiometer.getVoltage();
    }

    // create variable for counts per motor rev for the fourbar
    //static final double COUNTS_PER_FOURBAR_MOTOR_REV = 288; //Core Hex Motor
    //static final double DRIVE_GEAR_REDUCTION = .52; //This is greater than 1 if geared up

    public boolean setFourBarDirection(String fourBarLocationTarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        boolean fourBarError = false;
        //TODO  NEED ROBOT: Update fourbarpostarget values below to match readings from robot
        if (fourBarLocationTarget == "ground") {
            fourBarError = setFourBarPosition(.29, autonMode);
        } else if (fourBarLocationTarget == "low") {
            fourBarError = setFourBarPosition(.58, autonMode);
        } else if (fourBarLocationTarget == "medium") {
            fourBarError = setFourBarPosition(2.47, autonMode);
        } else if (fourBarLocationTarget == "high") {
            fourBarError = setFourBarPosition(2.87, autonMode);
        }
        return fourBarError;
    }

    public synchronized boolean setFourBarPosition(double fourBarPositionTarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        //TODO Need to confirm threshold is good
        final double FOURBAR_THRESHOLD_POS = 0.1; // volts
        double fourBarSpeedMultiple; // to set the  speed of the fourbar
        runtime.reset();
        fourBarStop = false; // initially we want the fourbar to move for the while loop
        fourBarPositionCurrent = 0; //updates every loop at the end, zero to start while loop for comparison
        while (!fourBarStop) {
            //This gets the current fourbar position and sets it to a variable
            //TODO Check if 2 is long enough for timeout here???
            int fourBarTimeout = 2;
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

    public double getFourBarPosition() {
        return fourBarMotor.getCurrentPosition();
    }

    public double getFourBarCurrentThreshold() {
        return this.FOURBAR_CURRENT_THRESHOLD;
    }

    public void calibrateZeroFourBar() {
        // Reset the encoder to zero on init
        fourBarMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    // Arm Controls

    public double getArmVerticalPosition() {
        return arm.getArmVerticalPosition();
    }

    public double getArmRotationPosition() {
        return arm.getArmRotationPosition();
    }

    public void setArmVerticalPosition(double armVerticalPositionTarget) {
       arm.setArmVerticalPosition(armVerticalPositionTarget);
    }

    public void setArmRotationPosition(double armRotationPositionTarget) {
        arm.setArmRotationPosition(armRotationPositionTarget);
    }

    public void openPickup() {
        arm.openPickup();
    }

    public void closePickup() {
        arm.closePickup();
    }
}
