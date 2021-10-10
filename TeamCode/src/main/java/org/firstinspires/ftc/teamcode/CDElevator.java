package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;

public class CDElevator {

    CDHardware robotHardware;

    public CDElevator(CDHardware theHardware){

        robotHardware = theHardware;

       // robotHardware.elevatorswitchtop;
       // robotHardware.elevatorswitchmiddle;
       // robotHardware.elevatorswitchbottom;
       // robotHardware.elevatorswitchground;

        robotHardware.elevatormotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.elevatormotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setElevatorPower(double pow) {
        robotHardware.elevatormotor.setPower(pow);
    }

}
