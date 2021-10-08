package org.firstinspires.ftc.team8580;

import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Hardware;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.Servo;


public class CDHardware {
//Defines the motors and (possible) sensors

    public DcMotor leftfrontmotor;
    public DcMotor rightfrontmotor;
    public DcMotor leftrearmotor;
    public DcMotor rightrearmotor;
    public DcMotor turretmotor;
    public DcMotor elevatormotor;
    public DcMotor intakemotor;
    public DcMotor duckspinnermotor;
//    public Sensor elevatorswitchtop;
//    public Sensor elevatorswitchmiddle;
//    public Sensor elevatorswitchbottom;
//    public Sensor elevatorswitchground;

    public CDHardware (HardwareMap myHardware){
       //Defines Hardware map from Control Hub
        leftfrontmotor = myHardware.get(DcMotor.class, "motorLF"); 
        rightfrontmotor = myHardware.get(DcMotor.class, "motorRF");
        leftrearmotor = myHardware.get(DcMotor.class, "motorLR");
        rightrearmotor = myHardware.get(DcMotor.class, "motorRR");
        turretmotor = myHardware.get(DcMotor.class, "motorTurret");
        elevatormotor = myHardware.get(DcMotor.class, "motorElevator");
        intakemotor = myHardware.get(DcMotor.class, "motorIntake");
        duckspinnermotor = myHardware.get(DcMotor.class, "motorDuckSpinner");
        // Unused sensors
//        elevatorswitchtop = myHardware.get(Sensor.class,"switchElevatorTop");
//        elevatorswitchmiddle = myHardware.get(Sensor.class,"switchElevatorMidddle");
//        elevatorswitchbottom = myHardware.get(Sensor.class,"switchElevatorBottom");
//        elevatorswitchground = myHardware.get(Sensor.class,"switchElevatorGround");
    }
}

