package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

//Defines the motors and (possible) sensors
public class CDHardware {
    // Wheel motors
    public DcMotor leftfrontmotor;
    public DcMotor rightfrontmotor;
    public DcMotor leftrearmotor;
    public DcMotor rightrearmotor;
    //Fourbar
    public DcMotor fourbarmotor;
    public AnalogInput fourbarpot;
    //Arm
    public Servo armupdownservo;
    public Servo armrotservo;
    public Servo intakeservo;
    //Grabber
    public Servo extendservo;
    public Servo grabservo;



    public CDHardware (HardwareMap hwMap){
        //Defines Hardware map from Control Hub
        //TODO: Need to double check motor mapping on the driver hub and check orientation
        leftfrontmotor = hwMap.get(DcMotor.class, "motorLF");
        rightfrontmotor = hwMap.get(DcMotor.class, "motorRF");
        leftrearmotor = hwMap.get(DcMotor.class, "motorLR");
        rightrearmotor = hwMap.get(DcMotor.class, "motorRR");
        fourbarmotor = hwMap.get(DcMotor.class, "motorFourBar");
        fourbarpot = hwMap.get(AnalogInput.class, "fourBarPos");
        armupdownservo = hwMap.get(Servo.class, "servoUpDownArm");
        armrotservo = hwMap.get(Servo.class, "servoRotArm");
        intakeservo = hwMap.get(Servo.class, "servoIntake");
        extendservo = hwMap.get(Servo.class, "servoExtend");
        grabservo = hwMap.get(Servo.class, "servoGrab");
    }
}

