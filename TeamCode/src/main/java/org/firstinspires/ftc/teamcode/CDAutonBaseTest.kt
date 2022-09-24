package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled

@Autonomous(name = "CDAutonBaseTest", group = "Linear Opmode")
@Disabled
class CDAutonBaseTest : CDAutonBase() {
    override fun initTokenWeDoNotSee() {
        duckWeDoNotSee = 1
    }

    override fun executeAuton() {
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, 2.0, 5.0)
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2.0, 10.0)
        myChassis.encoderDriveStrafe(CDDriveChassisAuton.DRIVE_SPEED, -2.0, 5.0)
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2.0, -10.0)
    }
}