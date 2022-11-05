package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


public class CDDriveChassis {
    CDHardware robotHardware;

    public CDDriveChassis(CDHardware theHardware) {
        robotHardware = theHardware;
        
        robotHardware.leftFrontMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.leftRearMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.rightFrontMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        robotHardware.rightRearMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        
        robotHardware.leftFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.leftRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.rightFrontMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.rightRearMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setLeftFrontPower (double pow) {
        robotHardware.leftFrontMotor.setPower(pow);
    }
    public void setLeftRearPower (double pow) {
        robotHardware.leftRearMotor.setPower(pow);
    }
    public void setRightFrontPower (double pow) {
        robotHardware.rightFrontMotor.setPower(pow);
    }
    public void setRightRearPower (double pow) {
        robotHardware.rightRearMotor.setPower(pow);
    }

}















