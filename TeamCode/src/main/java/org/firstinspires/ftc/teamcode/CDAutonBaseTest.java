package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.teamcode.CDDriveChassisAuton;

@Autonomous(name="CDAutonBaseTest", group="Linear Opmode")
@Disabled
public class CDAutonBaseTest extends CDAutonBase {
//    @Override
//    public void initTokenWeDoNotSee() {
//        duckWeDoNotSee = 1;
//    }

    @Override
    public void executeAuton() {
        myChassis.encoderDriveStrafe(org.firstinspires.ftc.teamcode.CDDriveChassisAuton.DRIVE_SPEED, 2, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, 10.0);
        myChassis.encoderDriveStrafe(org.firstinspires.ftc.teamcode.CDDriveChassisAuton.DRIVE_SPEED, -2, 5);
        myChassis.encoderDriveStraight(CDDriveChassisAuton.DRIVE_SPEED, 2, -10.0);
    }

}

