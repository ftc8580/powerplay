package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;
//import com.qualcomm.robotcore.util.Hardware;

public class CDTurret {
    double Turretslow = .70;
    CDHardware robotHardware;
    public boolean turretstop;
    public double turretposcurrent;
    public double turretposlast;
    public double TURRET_CURRENT_THRESHOLD;
    public AnalogInput turretpot;
    private ElapsedTime turrettimer = new ElapsedTime();
    private int turrettimeout = 2; // timeout for turret moves


    public  CDTurret(CDHardware theHardware){
        robotHardware = theHardware;
        robotHardware.turretmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        // Added to make sure that the turret defaults to brake mode
        robotHardware.turretmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.turretmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        turretpot = robotHardware.turretpot;
    }

    public void setTurretPower(double pow) {
        robotHardware.turretmotor.setPower(pow * Turretslow);
    }
    public double getTurretPotVolts() {
        // Reference https://docs.revrobotics.com/potentiometer/untitled-1#calculating-the-relationship-between-voltage-and-angle
        return (turretpot.getVoltage());
    }

    // create variable for counts per motor rev for the turret
    //static final double COUNTS_PER_TURRET_MOTOR_REV = 288; //Core Hex Motor
    //static final double DRIVE_GEAR_REDUCTION = .52; //This is greater than 1 if geared up

    public boolean setTurretDirection(String turretlocationtarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        boolean turreterror = false;
        if (turretlocationtarget == "center") {
            turreterror = setTurretPosition(1.27, autonMode);
        } else if (turretlocationtarget == "left") {
            turreterror = setTurretPosition(.58, autonMode);
        } else if (turretlocationtarget == "right") {
            turreterror =  setTurretPosition(2.46, autonMode);
        }
        return turreterror;
    }
    public boolean setTurretPosition(double turretpostarget, boolean autonMode) {
        // This method will return false for successful turn or true for an error.
        final double TURRET_THRESHOLD_POS = 0.1; // volts
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
                turretstop = true; // leave the while loop
                return false;
            }
            if (autonMode) {
                turretmult = 1.0; // Run full speed or do the ifs
            } else {
                turretmult = 0.8;
            }
            if (TURRET_CURRENT_THRESHOLD <= .03) { // Prepare to slow tolerance
                if (autonMode) {
                    turretmult = .8; // Prepare to slow
                } else {
                    turretmult = .6; // Prepare to slow
                }
            } else if (TURRET_CURRENT_THRESHOLD <= .01) { // Prepare to stop tolerance
                // Do this before we stop
                if (autonMode) {
                    turretmult = .3; // Prepare to stop
                } else {
                    turretmult = .3;
                }
            }

            if (turretposlast > turretpostarget) {
                setTurretPower(-1 * turretmult);
                turretstop = false;
            } else if (turretposlast < turretpostarget) {
                setTurretPower(1*turretmult);
                turretstop = false;
            }
            turretposcurrent = getTurretPotVolts(); //updates every loop to see where we landed for lockup detection.
        }
        return false; // Returns false if successfully made the moves, no error.
    }
// 2/3 Digital control hub

    public double getTurrentPos () { return robotHardware.turretmotor.getCurrentPosition(); }

    public double getTurretCurrentThreshold () { return this.TURRET_CURRENT_THRESHOLD; }

    public void calibrateZeroTurret () {
        // Reset the encoder to zero on init
        robotHardware.turretmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}