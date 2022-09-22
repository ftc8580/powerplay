package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

// import com.qualcomm.robotcore.eventloop.opmode.Disabled;

public class CDDriveChassis {

    org.firstinspires.ftc.teamcode.CDHardware robotHardware;
    private ElapsedTime runtime=new ElapsedTime();

    public CDDriveChassis(CDHardware theHardware) {
        
        robotHardware = theHardware;
        
        robotHardware.leftfrontmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.leftrearmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        robotHardware.rightfrontmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        robotHardware.rightrearmotor.setDirection(DcMotorSimple.Direction.REVERSE);
        
        robotHardware.leftfrontmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.leftrearmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.rightfrontmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.rightrearmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setLeftFrontPower (double pow) {
        robotHardware.leftfrontmotor.setPower(pow);
    }
    public void setLeftRearPower (double pow) {
        robotHardware.leftrearmotor.setPower(pow);
    }
    public void setRightFrontPower (double pow) {
        robotHardware.rightfrontmotor.setPower(pow);
    }
    public void setRightRearPower (double pow) {
        robotHardware.rightrearmotor.setPower(pow);
    }

}















