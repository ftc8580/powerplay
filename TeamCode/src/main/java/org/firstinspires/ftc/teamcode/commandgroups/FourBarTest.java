package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.commands.ArmMoveRotate;
import org.firstinspires.ftc.teamcode.commands.ArmMoveVertical;
import org.firstinspires.ftc.teamcode.commands.FourBarSetPosition;

public class FourBarTest extends SequentialCommandGroup {
    public FourBarTest(CDFourBar fourBar, CDArm arm) {
        addCommands(
                new FourBarSetPosition(fourBar, 1),
                new ArmMoveVertical(arm, 0),
                new ArmMoveRotate(arm, 0),
                new ArmMoveRotate(arm, 1),
                new ArmMoveVertical(arm, 1),
                new FourBarSetPosition(fourBar, -1)
        );
        addRequirements(fourBar, arm);
    }
}
