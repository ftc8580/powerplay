package org.firstinspires.ftc.teamcode;

import com.arcrobotics.ftclib.command.CommandOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import org.firstinspires.ftc.teamcode.commandgroups.FourBarTest;

@Autonomous
public class CDAutonFourBarTest extends CommandOpMode {
    private CDArm arm;
    private CDFourBar fourBar;
    private FourBarTest testCommandGroup;

    public void initialize() {
        CDHardware hardware = new CDHardware(hardwareMap);
        arm = new CDArm(hardware);
        fourBar = new CDFourBar(hardware);
        testCommandGroup = new FourBarTest(fourBar, arm);

        schedule(testCommandGroup);
        register(arm, fourBar);
    }
}
