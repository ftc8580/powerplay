package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CdFourBar {
    double fourbarslow = .7;
    CDHardware robotHardware;
    public boolean fourbarstop;
    public double fourbarposcurrent;
    public double fourbarposlast;
    public double FOURBAR_CURRENT_THRESHOLD;
    public AnalogInput fourbarpot;
    private ElapsedTime fourbartimer = new ElapsedTime();
    private int fourbartimeout = 2; // timeout for fourbar moves

    public CDFourbar(CDHardware theHardware){
        robotHardware = theHardware;
        robotHardware.fourbarmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        //Added to make ture that the fourbar defaults to brake mode
        robotHardware.fourbarmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.fourbarmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fourbarpot = robotHardware.fourbarpos;
    }
    public void setfourbarpower(double pow) {
        robotHardware.fourbarmotor.setPower(pow * fourbarslow);
    }
    public double getFourbarPotVolts() {

    }

    // create variable for counts per motor rev for the turret
}
