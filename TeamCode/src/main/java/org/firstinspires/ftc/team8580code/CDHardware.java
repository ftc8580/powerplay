package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Servo;


public class CDHardware {

    public DcMotor leftfrontmotor;
    public DcMotor rightfrontmotor;
    public DcMotor leftrearmotor;
    public DcMotor rightrearmotor;

    public CDHardware (HardwareMap myHardware){
        
        leftfrontmotor = myHardware.get(DcMotor.class, "motorLF"); 
        rightfrontmotor = myHardware.get(DcMotor.class, "motorRF");
        leftrearmotor = myHardware.get(DcMotor.class, "motorLR");
        rightrearmotor = myHardware.get(DcMotor.class, "motorRR");
    }
}
