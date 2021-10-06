package org.firstinspires.ftc.team8580;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class CDElevator {

    CDHardware robotHardware;

    public CDElevator(CDHardware theHardware){

        robotHardware = theHardware;



       // robotHardware.elevatorswitchtop;
       // robotHardware.elevatorswitchmiddle;
       // robotHardware.elevatorswitchbottom;
       // robotHardware.elevatorswitchground;
        robotHardware.elevatormotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.elevatormotor.setDirection(DcMotorSimple.Direction.FORWARD);



    }



}
