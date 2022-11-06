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
                new FourBarSetPosition(fourBar, CDFourBar.ARM_CLEARED_POSITION_HOME),
                new ArmMoveVertical(arm, CDArm.ARM_VERTICAL_PICKUP_HIGH_POSITION),
                new ArmMoveRotate(arm, fourBar, CDArm.ARM_ROTATION_POSITION_LEFT),
                new ArmMoveRotate(arm, fourBar, CDArm.ARM_ROTATION_POSITION_HOME),
                new ArmMoveVertical(arm, CDArm.ARM_ROTATION_POSITION_BACK)
                // new FourBarSetPosition(fourBar, CDFourBar.LOWER_POSITION_HOME)
        );
        addRequirements(fourBar, arm);
    }
}
