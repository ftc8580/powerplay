package org.firstinspires.ftc.team8580;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Hardware;


public class CDSpinner {

    CDHardware robotHardware;

    public  CDSpinner(CDHardware theHardware){

        robotHardware = theHardware;


        robotHardware.spinnermotor.setDirection(DcMotorSimple.Direction.FORWARD);
    }



}
