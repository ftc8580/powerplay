package org.firstinspires.ftc.teamcode

// import com.qualcomm.robotcore.eventloop.opmode.Disabled;
// import com.qualcomm.robotcore.util.ElapsedTime
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.DcMotor
import org.firstinspires.ftc.teamcode.models.DrivePower

class CDTeleopDriveChassis(var robotHardware: CDHardware) {
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

    fun setDrivePower(drivePower: DrivePower, robotSpeed: Double = 1.0) {
        robotHardware.leftFrontMotor.power = drivePower.leftFrontPower * robotSpeed
        robotHardware.leftRearMotor.power = drivePower.leftRearPower * robotSpeed
        robotHardware.rightFrontMotor.power = drivePower.rightFrontPower * robotSpeed
        robotHardware.rightRearMotor.power = drivePower.rightRearPower * robotSpeed
    }
}