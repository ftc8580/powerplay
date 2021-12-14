package org.firstinspires.ftc.teamcode;

import android.text.method.*;

import com.qualcomm.robotcore.hardware.*;
import com.qualcomm.robotcore.util.*;

public class CDElevator {

   // This is where we set the values for our distance sensor
    public double defaultelevatorposition = 26.0;
    public double elevatorposground = 4.0;
    public double elevatorposbottom = 14.0;
    public double elevatorposmiddle = 26.0;
    public double elevatorpostop = 39.5;
    public double wheelheightforelevator = 12;

    private ElapsedTime runtime = new ElapsedTime();

    CDHardware robotHardware;
    CDDistanceSensor myDistanceSensor;
    public boolean elevatorstop;
    public boolean magneticstop;
    public boolean elevatorerror;
    public double ELEVATORCURRENTTHRESHOLD;
    public double elevatorposcurrent;
    public double elevatorlastpos;
    public TouchSensor upelevatormagnetswitch;

    public CDElevator(CDHardware theHardware){

        robotHardware = theHardware;
        myDistanceSensor =new CDDistanceSensor(robotHardware);
        upelevatormagnetswitch = robotHardware.elevatormagneticswitch;

        // robotHardware.elevatorswitchtop;
        // robotHardware.elevatorswitchmiddle;
        // robotHardware.elevatorswitchbottom;
        // robotHardware.elevatorswitchground;

        robotHardware.elevatormotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.elevatormotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robotHardware.elevatormotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public double getElevatorThreshold () { return ELEVATORCURRENTTHRESHOLD; }

    public void setElevatorPower(double pow) {
        robotHardware.elevatormotor.setPower(pow);
    }

    public double getElevatorPosition() { return myDistanceSensor.getElevatorDistance(); }

    public boolean setElevatorPosition(double elevatorpostarget) {
        runtime.reset();
        double elevatorPositionTimeoutSec = 4.0;
        final double THRESHOLD_POS = 1.0; // CM or whatever the Distance sensor is configured
        double elevatormult = 1.0; // to slow down the elevator if needed
        elevatorstop = false; // initially we want the elevator to move for the while loop
        magneticstop = false;
        elevatorerror = false;
        while ((runtime.seconds() < elevatorPositionTimeoutSec) && !elevatorstop && !magneticstop && !elevatorerror) {
            // Simple check to see if the magnetic switch is contacted
            if (upelevatormagnetswitch.isPressed()) {
                magneticstop = true;
            }
//            if (elevatorlastpos == elevatorposcurrent) {
//                elevatorerror = true;
//                return true; // There was an error, the value didn't change.
//            }
            /* This gets the current distance off the floor from the Elevator Distance Sensor
          and sets it to a variable
           */
            // TODO: Need to use the turret potentiometer to determine if we are over the wheels to make sure we don't drop the elevator in auton on wheels and bind.
            elevatorlastpos = myDistanceSensor.getElevatorDistance(); // updates every loop to say where we are in the beginning.
            ELEVATORCURRENTTHRESHOLD = Math.abs(elevatorlastpos - elevatorpostarget);
            if (elevatorlastpos < 10 || elevatorlastpos > 35) {
                elevatormult = .85;
            } else {
                elevatormult = 1.0;
            }
            if (ELEVATORCURRENTTHRESHOLD <= THRESHOLD_POS)  {
                setElevatorPower(0); // need to stop the elevator before leaving the loop
                elevatorstop = true; // leave the while loop
            } else if (elevatorlastpos > elevatorpostarget) {
                setElevatorPower(-1*elevatormult);
            } else if (elevatorlastpos < elevatorpostarget) {
                setElevatorPower(1*elevatormult);
            }
            elevatorposcurrent = myDistanceSensor.getElevatorDistance(); // updates every loop to see where we ended up.
        }
        return false; // Returns false if the elevator succeeded in moving to requested position, no error.
    }
    //
}
