package org.firstinspires.ftc.teamcode;

import android.hardware.Sensor;
import com.qualcomm.hardware.bosch.*;
import com.qualcomm.robotcore.hardware.*;


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
    public DistanceSensor intakedistancesensor;
    public DcMotor duckspinnermotor;
    public DistanceSensor elevatordistancesensor;
    public TouchSensor elevatormagneticswitch;
    public AnalogInput turretpot;
    // IMU sensor
    public BNO055IMU cdimu;

    //    public Sensor elevatorswitchtop;
    //    public Sensor elevatorswitchmiddle;
    //    public Sensor elevatorswitchbottom;
    //    public Sensor elevatorswitchground;

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
        intakedistancesensor = hwMap.get(DistanceSensor.class, "distanceIntake");
        turretmotor = hwMap.get(DcMotor.class, "motorTurret");
        elevatordistancesensor = hwMap.get(DistanceSensor.class, "distanceElev");
        elevatormagneticswitch = hwMap.get(TouchSensor.class,"ElvStop");
        turretpot = hwMap.get(AnalogInput.class, "turretpot");
        // Retrieved above and here we initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        cdimu = hwMap.get(BNO055IMU.class, "imu");

//        // Set up the parameters with which we will use our IMU. Note that integration
//        // algorithm here just reports accelerations to the logcat log; it doesn't actually
//        // provide positional information.
//        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
//        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
//        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
//        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
//        parameters.loggingEnabled      = true;
//        parameters.loggingTag          = "IMU";
//        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
//
//        cdimu.initialize(parameters);
    }
}

