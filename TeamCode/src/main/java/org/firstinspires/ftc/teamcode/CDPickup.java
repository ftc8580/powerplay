package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

public class CDPickup {
    //TODO reset calues below to values from Pickup servo
    public double pickupopen = .75;
    public double pickupclosed = .25;


    private ElapsedTime runtime = new ElapsedTime();

    CDHardware robotHardware;
    public boolean pickupstop;
    public boolean pickuperror;
    public double PICKUPCURRENTTHRESHOLD;
    public double pickupposcurrent;
    public double pickuplastpos;

    public CDPickup(CDHardware theHardware) {
        robotHardware = theHardware;
    }

    public double getPickupPosition() {return robotHardware.intakeservo.getPosition(); }  //Position from servo

    public boolean setPickupPosition(double pickuptarget) {
        runtime.reset();
        //TODO redefine timeout if this is too long - needs testing
        double pickupTimeoutSec = 4.0;
        //TODO determine if threshold_pos is needed when working with servo
        final double THRESHOLD_PUPOS = .005; // base on readings from intakeservo
        pickupstop = false; // initially we want the pickup to move for the while loop
        pickuperror = false;
        //while ((runtime.seconds() < armrotPositionTimeoutSec) && !armrotstop && !magneticstop && !armroterror) {
        while ((runtime.seconds() < pickupTimeoutSec) && !pickupstop && !pickuperror) {
            pickuplastpos = getPickupPosition(); // updates every loop to say where we are in the beginning.
            PICKUPCURRENTTHRESHOLD = Math.abs(pickuplastpos - pickuptarget);
            if (PICKUPCURRENTTHRESHOLD <= THRESHOLD_PUPOS) {
                robotHardware.intakeservo.setPosition(pickuptarget);
                pickupstop = true; // leave the while loop
            }
            pickupposcurrent = getPickupPosition();  // updates every loop to see where we ended up.
        }
        return false; // Returns false if the arm succeeded in moving to requested position, no error.
    }

}
