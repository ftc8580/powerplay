package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.*;

//Defines the motors and (possible) sensors
public class CDHardware {
    // Wheel motors
    public DcMotor leftFrontMotor;
    public DcMotor rightFrontMotor;
    public DcMotor leftRearMotor;
    public DcMotor rightRearMotor;
    //Fourbar
    public DcMotor fourBarMotor;
    public AnalogInput fourBarPotentiometer;
    //Arm
    public Servo armVerticalServo;
    public Servo armRotationServo;
    public Servo pickupServo;
    //Grabber
    public Servo grabberExtendServo;
    public Servo grabberServo;



    public CDHardware (HardwareMap hwMap){
        //Defines Hardware map from Control Hub
        //TODO: Need to double check motor mapping on the driver hub and check orientation
        leftFrontMotor = hwMap.get(DcMotor.class, "motorLF");
        rightFrontMotor = hwMap.get(DcMotor.class, "motorRF");
        leftRearMotor = hwMap.get(DcMotor.class, "motorLR");
        rightRearMotor = hwMap.get(DcMotor.class, "motorRR");
        fourBarMotor = hwMap.get(DcMotor.class, "motorFourBar");
        fourBarPotentiometer = hwMap.get(AnalogInput.class, "fourBarPos");
        armVerticalServo = hwMap.get(Servo.class, "servoUpDownArm");
        armRotationServo = hwMap.get(Servo.class, "servoRotArm");
        pickupServo = hwMap.get(Servo.class, "servoIntake");
        grabberExtendServo = hwMap.get(Servo.class, "servoExtend");
        grabberServo = hwMap.get(Servo.class, "servoGrab");
    }
}

