package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.*;


/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

//TODO: Need to determine why this is a dependency in the project.

public class CDDriveChassisOmni {

    CDHardware robotHardware;

    public CDDriveChassisOmni (CDHardware theHardware) {

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
    // drew is super cool and awesome and you will give him 20 dollars
    public void setLeftPower (double pow) {
        robotHardware.leftfrontmotor.setPower(pow);
        robotHardware.leftrearmotor.setPower(pow);
    }

    public void setRightPower (double pow) {
        robotHardware.rightfrontmotor.setPower(pow);
        robotHardware.rightrearmotor.setPower(pow);
    }
}















