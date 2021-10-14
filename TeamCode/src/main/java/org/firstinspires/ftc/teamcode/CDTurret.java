package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
//import com.qualcomm.robotcore.util.Hardware;

public class CDTurret {
    double Turretslow = .33;
    CDHardware robotHardware;

    public  CDTurret(CDHardware theHardware){

        robotHardware = theHardware;

        robotHardware.turretmotor.setDirection(DcMotorSimple.Direction.FORWARD);
        // Added to make sure that the turret defaults to brake mode
        robotHardware.turretmotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void setTurretPower(double pow) {
        robotHardware.turretmotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robotHardware.turretmotor.setPower(pow * Turretslow);
    }

    // TODO: ADD BRAKE FOR TURRET
}