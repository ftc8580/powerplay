package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.commandgroups.FourBarTest;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

@Autonomous
@Disabled
public class CDAutonFourBarTest extends CommandOpMode {
    private CDArm arm;
    private CDFourBar fourBar;
    private FourBarTest testCommandGroup;

    public void initialize() {
        CDHardware hardware = new CDHardware(hardwareMap);
        arm = new CDArm(hardware);
        fourBar = new CDFourBar(hardwareMap);
        testCommandGroup = new FourBarTest(fourBar, arm);

        schedule(testCommandGroup);
        register(arm, fourBar);
    }
}
