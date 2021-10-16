package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
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
    public DistanceSensor elevatordistancesensor;


    public CDHardware (HardwareMap hwMap){
       //Defines Hardware map from Control Hub
        //TODO: Need to double check motor mapping on the driver hub and check orientation
        leftfrontmotor = hwMap.get(DcMotor.class, "motorLF");
        rightfrontmotor = hwMap.get(DcMotor.class, "motorRF");
        leftrearmotor = hwMap.get(DcMotor.class, "motorLR");
        rightrearmotor = hwMap.get(DcMotor.class, "motorRR");
        elevatormotor = hwMap.get(DcMotor.class, "motorElevator");
        duckspinnermotor = hwMap.get(DcMotor.class, "motorDuckSpinner");
        intakemotor = hwMap.get(DcMotor.class, "motorIntake");
        turretmotor = hwMap.get(DcMotor.class, "motorTurret");

        // Unused sensors- Switched to using motor encoder
        elevatordistancesensor = hwMap.get(DistanceSensor.class,"Distance");

    }
}

