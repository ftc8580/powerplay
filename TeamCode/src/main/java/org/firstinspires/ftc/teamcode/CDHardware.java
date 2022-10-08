package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;
import com.qualcomm.hardware.bosch.*;
import com.qualcomm.robotcore.hardware.*;

//Defines the motors and (possible) sensors
public class CDHardware {
    // Wheel motors
    public DcMotor leftfrontmotor;
    public DcMotor rightfrontmotor;
    public DcMotor leftrearmotor;
    public DcMotor rightrearmotor;
    public DcMotor fourbarmotor;
    public DcMotor verticalintakearm;
    public Servo intakearm;
    public Servo intakeservo;
    public AnalogInput fourbarpos;


    public CDHardware (HardwareMap hwMap){
        //Defines Hardware map from Control Hub
        //TODO: Need to double check motor mapping on the driver hub and check orientation
        leftfrontmotor = hwMap.get(DcMotor.class, "motorLF");
        rightfrontmotor = hwMap.get(DcMotor.class, "motorRF");
        leftrearmotor = hwMap.get(DcMotor.class, "motorLR");
        rightrearmotor = hwMap.get(DcMotor.class, "motorRR");
        fourbarmotor = hwMap.get(DcMotor.class, "motorFourBar");
        verticalintakearm = hwMap.get(DcMotor.class, "motorIntakeArm");
        intakearm = hwMap.get(Servo.class, "servoIntakeArm");
        intakeservo = hwMap.get(Servo.class, "servoIntake");
        fourbarpos = hwMap.get(AnalogInput.class, "analogInputFourBar");
    }
}

