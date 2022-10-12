package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CdFourBar {
    double fourbarslow = .7;
    CDHardware robotHardware;
    public boolean fourbarstop;
    public double fourbarposcurrent;
    public double fourbarposlast;
    public double FOURBAR_CURRENT_THRESHOLD;
    public AnalogInput fourbarpot;
    private ElapsedTime fourbartimer = new ElapsedTime();
    private int fourbartimeout = 2; // timeout for fourbar moves

    public CDFourbar(CDHardware theHardware){
        robotHardware = theHardware;
        robotHardware.fourbarmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //Added to make ture that the fourbar defaults to brake mode
        robotHardware.fourbarmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.fourbarmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fourbarpot = robotHardware.fourbarpos;
    }
    public void setfourbarpower(double pow) {
        robotHardware.fourbarmotor.setPower(pow * fourbarslow);
    }
    public double getFourbarPotVolts() {

    }

    // create variable for counts per motor rev for the turret
    //static final double COUNTS_PER_TURRET_MOTOR_REV = 288; //Core Hex Motor
    //static final double DRIVE_GEAR_REDUCTION = .52; //This is greater than 1 if geared up

    public boolean setFourbarDirection(String fourbarlocationtarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        boolean fourbarerror = false;
        if (fourbarlocationtarget == "center") {
            error = setFourbarPosition(1.29, autonMode);
        } else if (fourbarlocationtarget == "left") {
            fourbarerror = setFourbarPosition(.58, autonMode);
        } else if (fourbarlocationtarget == "right") {
            fourbarerror =  setFourbarPosition(2.47, autonMode);
        }
        return fourbarerror;
    }
    public boolean setFourbarPosition(double fourbarpostarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        final double FOURBAR_THRESHOLD_POS = 0.1; // volts
        double turretmult; // to set the  speed of the turret
        // TODO: Need to change from our turretposcurrent to
        turrettimer.reset();
        turretstop = false; // initially we want the turret to move for the while loop
        turretposcurrent = 0; //updates every loop at the end, zero to start while loop for comparison
        while (!turretstop) {
            /* This gets the current turret position and sets it to a variable
             */
            if (turrettimer.seconds() > turrettimeout) {
                return false;
            }
            turretposlast = getTurretPotVolts(); //updates every loop for the position going into the move.
//            if (turretposcurrent == turretposlast) {
//                turretstop = true;
//                return true; // There was an error, the value didn't change.
//            };
            TURRET_CURRENT_THRESHOLD = Math.abs(turretposlast - turretpostarget); // Check the current gap between target and current position
            if (TURRET_CURRENT_THRESHOLD <= .06) { // Stop tolerance
                setTurretPower(0.0); // need to stop the turret before leaving the loop
                fourbarstop = true; // leave the while loop
                return false;
            }
            if (autonMode) {
                turretmult = 1.0; // Run full speed or do the ifs
            } else {
                turretmult = 0.8;
            }
            if (FOURBAR_CURRENT_THRESHOLD <= .03) { // Prepare to slow tolerance
                if (autonMode) {
                    fourbarmult = .8; // Prepare to slow
                } else {
                    fourbarmult = .6; // Prepare to slow
                }
            } else if (TURRET_CURRENT_THRESHOLD <= .01) { // Prepare to stop tolerance
                // Do this before we stop
                if (autonMode) {
                    fourbarmult = .3; // Prepare to stop
                } else {
                    fourbarmult = .3;
                }
            }

            if (fourbarposlast > fourbarpostarget) {
                setFourbarPower(-1 * turretmult);
                fourbarstop = false;
            } else if (fourbarposlast < fourbarpostarget) {
                setFourbarPower(1*turretmult);
                fourbarstop = false;
            }
            fourbarposcurrent = getFourbarPotVolts(); //updates every loop to see where we landed for lockup detection.
        }
        return false; // Returns false if successfully made the moves, no error.
    }
// 2/3 Digital control hub

    public double getFourbarPos () { return robotHardware.fourbarmotor.getCurrentPosition(); }

    public double getFourbarCurrentThreshold () { return this.FOREBAR_CURRENT_THRESHOLD; }

    public void calibrateZeroTurret () {
        // Reset the encoder to zero on init
        robotHardware.fourbarmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}
