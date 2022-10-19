package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CDArm {

    // This is where we set the values for our distance sensor
    //TODO reset values below to values from armmotor encoder
    public double defaultarmposition = 26.0;
    public double armposground = 4.0;
    public double armposlow = 14.0;
    public double armposmedium = 26.0;
    public double armposhigh = 26.0;
    public double armpostopup = 39.5;
    public double armpostopdown = 0;
    //TODO reset values below to values from armservo
    public double armrotback = 0;
    public double armrotright = .33;
    public double armrotfront = .66;
    public double armrotleft = 1;


    //TODO delete below
    // public double wheelheightforelevator = 12;

    private ElapsedTime runtime = new ElapsedTime();

    CDHardware robotHardware;
    //Arm up down using motor
    public boolean armstop;
    //public boolean magneticstop;
    public boolean armerror;
    public double ARMCURRENTTHRESHOLD;
    public double armposcurrent;
    public double armlastpos;
    //public TouchSensor uparmmagnetswitch;

    //Arm rotation using servo
    public boolean armrotstop;
    public boolean armroterror;
    public double ARMROTCURRENTTHRESHOLD;
    public double armrotposcurrent;
    public double armrotlastpos;

    public CDArm(CDHardware theHardware){

        robotHardware = theHardware;
        // uparmmagnetswitch = robotHardware.armmagneticswitch;

        // robotHardware.armswitchthigh;
        // robotHardware.armswitchmedium;
        // robotHardware.armswitchlow;
        // robotHardware.armswitchground;

        robotHardware.armmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.armmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.armmotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robotHardware.armmotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //robotHardware.armmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public double getArmThreshold () { return ARMCURRENTTHRESHOLD; }
    public double getArmRotThreshold () {return ARMROTCURRENTTHRESHOLD; }

    public void setArmPower(double pow) {
        robotHardware.armmotor.setPower(pow);
    }

    public double getArmPosition() { return robotHardware.armmotor.getCurrentPosition(); } //Position from armmotor encoder
    public double getArmRotPosition() {return robotHardware.armservo.getPosition(); }  //Position from servo

    public boolean setArmPosition(double armpostarget) {
        runtime.reset();
        //TODO redefine timeout if this is too long - needs testing
        double armPositionTimeoutSec = 4.0;
        //TODO determine if threshold_pos is needed when working with servo
        final double THRESHOLD_POS = 1.0; // base on encoder readings from armmotor ArmPosiiton
        double armmult = 1.0; // to slow down the arm if needed
        armstop = false; // initially we want the arm to move for the while loop
        armerror = false;
        //while ((runtime.seconds() < armPositionTimeoutSec) && !armstop && !magneticstop && !armerror) {
        while ((runtime.seconds() < armPositionTimeoutSec) && !armstop && !armerror) {
            // Simple check to see if the magnetic switch is contacted
            // if (upelevatormagnetswitch.isPressed()) {
            //   magneticstop = true;
            //}
//            if (armlastpos == armposcurrent) {
//                elevatorerror = true;
//                return true; // There was an error, the value didn't change.
//            }
            armlastpos = getArmPosition(); // updates every loop to say where we are in the beginning.
            ARMCURRENTTHRESHOLD = Math.abs(armlastpos - armpostarget);
            //TODO update values below to reflect where near end ROM
            if (armlastpos < 10 || armlastpos > 35) {
                armmult = .05;
            } else {
                armmult = 0.10;
            }
            if (ARMCURRENTTHRESHOLD <= THRESHOLD_POS)  {
                setArmPower(0); // need to stop the arm before leaving the loop
                armstop = true; // leave the while loop
            //TODO check if -1 and 1 multipliers below move arm in correct direction - if not then reverse both
            } else if (armlastpos > armpostarget) {
                setArmPower(-1*armmult);
            } else if (armlastpos < armpostarget) {
                setArmPower(1*armmult);
            }
            armposcurrent = robotHardware.armmotor.getCurrentPosition(); // updates every loop to see where we ended up.
        }
        return false; // Returns false if the arm succeeded in moving to requested position, no error.
    }
    public boolean setArmRotPosition(double armrotpostarget) {
        runtime.reset();
        //TODO redefine timeout if this is too long - needs testing
        double armrotPositionTimeoutSec = 4.0;
        //TODO determine if threshold_pos is needed when working with encoder
        final double THRESHOLD_ROTPOS = .005; // base on  readings from armservo
        armrotstop = false; // initially we want the arm to move for the while loop
        armerror = false;
        //while ((runtime.seconds() < armrotPositionTimeoutSec) && !armrotstop && !magneticstop && !armroterror) {
        while ((runtime.seconds() < armrotPositionTimeoutSec) && !armrotstop && !armroterror) {
            // Simple check to see if the magnetic switch is contacted
            // if (upelevatormagnetswitch.isPressed()) {
            //   magneticstop = true;
            //}
//            if (armrotlastpos == armrotposcurrent) {
//                armroterror = true;
//                return true; // There was an error, the value didn't change.
//            }
            armrotlastpos = getArmRotPosition(); // updates every loop to say where we are in the beginning.
            ARMROTCURRENTTHRESHOLD = Math.abs(armrotlastpos - armrotpostarget);
            if (ARMROTCURRENTTHRESHOLD <= THRESHOLD_ROTPOS)  {
                robotHardware.armservo.setPosition(armrotpostarget);
                armrotstop = true; // leave the while loop
            }
            armrotposcurrent = getArmRotPosition();  // updates every loop to see where we ended up.
        }
        return false; // Returns false if the arm succeeded in moving to requested position, no error.
    }
    //
}