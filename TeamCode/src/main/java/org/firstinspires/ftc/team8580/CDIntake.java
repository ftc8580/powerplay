package org.firstinspires.ftc.team8580;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Hardware;


public class CDIntake {

    CDHardware robotHardware;

    public  CDIntake(CDHardware theHardware){
// define hardware class
        robotHardware = theHardware;

        robotHardware.intakemotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.intakemotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setIntakePower(double pow) {
        robotHardware.intakemotor.setPower(pow);
    }

}
