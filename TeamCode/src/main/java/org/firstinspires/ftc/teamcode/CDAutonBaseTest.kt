package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled

@Autonomous(name = "CDAutonBaseTest", group = "Linear Opmode")
class CDAutonBaseTest : CDAutonBase() {
    override fun initTokenWeDoNotSee() {
        duckWeDoNotSee = 1
    }

    override fun executeAuton() {
        myChassis.encoderDriveStrafe(CDAutonDriveChassis.DRIVE_SPEED, 2.0, 5.0)
        myChassis.encoderDriveStraight(CDAutonDriveChassis.DRIVE_SPEED, 2.0, 10.0)
        myChassis.encoderDriveStrafe(CDAutonDriveChassis.DRIVE_SPEED, -2.0, 5.0)
        myChassis.encoderDriveStraight(CDAutonDriveChassis.DRIVE_SPEED, 2.0, -10.0)
    }
}