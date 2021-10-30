package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;
//import com.qualcomm.robotcore.util.Hardware;

public class CDTurret {
    double Turretslow = .33;
    CDHardware robotHardware;
    public boolean turretstop;
    public double turretposcurrent;
    public double turretposlast;
    public double TURRET_CURRENT_THRESHOLD;
    public AnalogInput turretpot;
    public  CDTurret(CDHardware theHardware){

        robotHardware = theHardware;

        robotHardware.turretmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        // Added to make sure that the turret defaults to brake mode
        robotHardware.turretmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        turretpot = robotHardware.turretpot;

    }

    public void setTurretPower(double pow) {
        robotHardware.turretmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.turretmotor.setPower(pow * Turretslow);
    }
    public double getTurretPotDegrees() {
        // Reference https://docs.revrobotics.com/potentiometer/untitled-1#calculating-the-relationship-between-voltage-and-angle
        return (turretpot.getVoltage()*81.8);
    }
    // create variable for counts per motor rev for the turret
    static final double COUNTS_PER_TURRET_MOTOR_REV = 288; //Core Hex Motor
    static final double DRIVE_GEAR_REDUCTION = .52; //This is greater than 1 if geared up

    public boolean setTurretPosition(double turretpostargetdeg, String turretstartside) {
        // This method will return true for successful turn or false for an error.
        double turretoffset=0;
        //calculate turret target position
        if (turretstartside == "right" ) {
            turretoffset = 90;
        }else if (turretstartside == "left") {
            turretoffset = -90;
        }else if (turretstartside == "center") {
            turretoffset = 0;
        }

        double turretpostarget = -(turretpostargetdeg+turretoffset)/360 * COUNTS_PER_TURRET_MOTOR_REV / DRIVE_GEAR_REDUCTION;

        final double TURRET_THRESHOLD_POS = 5; // counts
        double turretmult = 0.75; // to slow down the turret if needed
        // TODO: Need to change from our turretposcurrent to
        turretstop = false; // initially we want the turret to move for the while loop
        turretposcurrent = 0; //updates every loop at the end, zero to start while loop for comparison
        turretposlast = robotHardware.turretmotor.getCurrentPosition(); // Turret error detection, last position to compare to current, no change, no move.
        while (!turretstop) {
            /* This gets the current turret position and sets it to a variable
             */
            turretposlast = robotHardware.turretmotor.getCurrentPosition(); //updates every loop for the position going into the move.
            if (turretposlast == turretposlast) {
                turretstop = true;
                return false; // There was an error, the value didn't change.
            };
            TURRET_CURRENT_THRESHOLD = Math.abs(turretposlast - turretpostarget);
            if (TURRET_CURRENT_THRESHOLD < TURRET_THRESHOLD_POS) {
                setTurretPower(0); // need to stop the turret before leaving the loop
                turretstop = true; // leave the while loop
            } else if (turretposlast > turretpostarget) {
                setTurretPower(-1*turretmult);
            } else if (turretposlast < turretpostarget) {
                setTurretPower(1*turretmult);
            }
            turretposcurrent = robotHardware.turretmotor.getCurrentPosition(); //updates every loop to see where we landed for lockup detection.
        }
        return true; // Returns true if successfully made the moves.
    }
// 2/3 Digital control hub

    public double getTurrentPos () { return robotHardware.turretmotor.getCurrentPosition(); }

    public double getTurretCurrentThreshold () { return this.TURRET_CURRENT_THRESHOLD; }

    public void calibrateZeroTurret () {
        // Reset the encoder to zero on init
        robotHardware.turretmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
}