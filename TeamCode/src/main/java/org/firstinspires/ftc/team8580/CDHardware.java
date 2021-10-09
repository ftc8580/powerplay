package org.firstinspires.ftc.team8580;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
//import com.qualcomm.robotcore.util.Hardware;
//import com.qualcomm.robotcore.hardware.TouchSensor;
//import com.qualcomm.robotcore.hardware.Servo;

//Defines the motors and (possible) sensors
public class CDHardware {

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

    public CDHardware (HardwareMap hwMap){
       //Defines Hardware map from Control Hub
        leftfrontmotor = hwMap.get(DcMotor.class, "motorLF");
        rightfrontmotor = hwMap.get(DcMotor.class, "motorRF");
        leftrearmotor = hwMap.get(DcMotor.class, "motorLR");
        rightrearmotor = hwMap.get(DcMotor.class, "motorRR");
        turretmotor = hwMap.get(DcMotor.class, "motorTurret");
        elevatormotor = hwMap.get(DcMotor.class, "motorElevator");
        intakemotor = hwMap.get(DcMotor.class, "motorIntake");
        duckspinnermotor = hwMap.get(DcMotor.class, "motorDuckSpinner");

        // Unused sensors- Switched to using motor encoder
        //        elevatorswitchtop = hwMap.get(Sensor.class,"switchElevatorTop");
        //        elevatorswitchmiddle = hwMap.get(Sensor.class,"switchElevatorMidddle");
        //        elevatorswitchbottom = hwMap.get(Sensor.class,"switchElevatorBottom");
        //        elevatorswitchground = hwMap.get(Sensor.class,"switchElevatorGround");
    }
}

