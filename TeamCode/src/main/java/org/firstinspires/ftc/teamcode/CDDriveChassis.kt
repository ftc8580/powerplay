package org.firstinspires.ftc.teamcode

// import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DcMotor
// import com.qualcomm.robotcore.eventloop.opmode.Disabled;

class CDDriveChassis(var robotHardware: CDHardware) {
    // private val runtime = ElapsedTime()

    init {
        robotHardware.leftFrontMotor.direction = DcMotorSimple.Direction.FORWARD
        robotHardware.leftRearMotor.direction = DcMotorSimple.Direction.FORWARD
        robotHardware.rightFrontMotor.direction = DcMotorSimple.Direction.REVERSE
        robotHardware.rightRearMotor.direction = DcMotorSimple.Direction.REVERSE
        robotHardware.leftFrontMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        robotHardware.leftRearMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        robotHardware.rightFrontMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
        robotHardware.rightRearMotor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    fun setLeftFrontPower(pow: Double) {
        robotHardware.leftFrontMotor.power = pow
    }

    fun setLeftRearPower(pow: Double) {
        robotHardware.leftRearMotor.power = pow
    }

    fun setRightFrontPower(pow: Double) {
        robotHardware.rightFrontMotor.power = pow
    }

    fun setRightRearPower(pow: Double) {
        robotHardware.rightRearMotor.power = pow
    }
}