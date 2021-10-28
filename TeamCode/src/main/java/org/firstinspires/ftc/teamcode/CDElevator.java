package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;

public class CDElevator {

    CDHardware robotHardware;
    CDDistanceSensor myDistanceSensor;
    public boolean elevatorstop;
    public double ELEVATORCURRENTTHRESHOLD;
    public double elevatorposcurrent;

    public CDElevator(CDHardware theHardware){

        robotHardware = theHardware;
        myDistanceSensor =new CDDistanceSensor(robotHardware);

        // robotHardware.elevatorswitchtop;
        // robotHardware.elevatorswitchmiddle;
        // robotHardware.elevatorswitchbottom;
        // robotHardware.elevatorswitchground;

        robotHardware.elevatormotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.elevatormotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public double getElevatorThreshold () { return this.ELEVATORCURRENTTHRESHOLD; }

    public void setElevatorPower(double pow) {
        robotHardware.elevatormotor.setPower(pow);
    }

    public double getElevatorPosition() { return this.elevatorposcurrent; }

    // TODO: Add public method to gotoPosition(Top, Middle, bottom)
    public void setElevatorPosition(double elevatorpostarget) {
        final double THRESHOLD_POS = 3; // CM or whatever the Distance sensor is configured
        double elevatormult = 0.75; // to slow down the elevator if needed

        elevatorstop = false; // initially we want the elevator to move for the while loop
        while (!elevatorstop) {
            /* This gets the current distance off the floor from the Elevator Distance Sensor
          and sets it to a variable
           */
//            double elevatorpriorpos = myDistanceSensor.getElevatorDistance();
            elevatorposcurrent = myDistanceSensor.getElevatorDistance(); // updates every loop
            ELEVATORCURRENTTHRESHOLD = Math.abs(elevatorposcurrent - elevatorpostarget);

            if (ELEVATORCURRENTTHRESHOLD < THRESHOLD_POS)  {
                setElevatorPower(0); // need to stop the elevator before leaving the loop
                elevatorstop = true; // leave the while loop
            } else if (elevatorposcurrent > elevatorpostarget) {
                setElevatorPower(-1*elevatormult);
            } else if (elevatorposcurrent < elevatorpostarget) {
                setElevatorPower(1*elevatormult);
            } else if (elevatorposcurrent <= 42.0) {
                setElevatorPower(0); // need to stop the elevator before leaving the loop
                elevatorstop = true; // leave the while loop
            }

        }
    }
    //
}
