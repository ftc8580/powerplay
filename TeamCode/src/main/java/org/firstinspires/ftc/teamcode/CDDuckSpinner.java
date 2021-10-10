package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class CDDuckSpinner {

    CDHardware robotHardware;

    public  CDDuckSpinner(CDHardware theHardware){

        robotHardware = theHardware;

        robotHardware.duckspinnermotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.duckspinnermotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setDuckSpinnerPower(double pow) {
        robotHardware.duckspinnermotor.setPower(pow);
    }

}
