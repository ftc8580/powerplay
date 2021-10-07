package org.firstinspires.ftc.team8580;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Hardware;


public class CDDuckSpinner {

    CDHardware robotHardware;

    public  CDDuckSpinner(CDHardware theHardware){

        robotHardware = theHardware;


        robotHardware.duckspinnermotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }



}
