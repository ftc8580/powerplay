package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.ArmMoveRotate;
import org.firstinspires.ftc.teamcode.commands.ArmMoveVertical;
import org.firstinspires.ftc.teamcode.commands.ArmVerticalHome;
import org.firstinspires.ftc.teamcode.commands.FourBarSetPosition;
import org.firstinspires.ftc.teamcode.commands.MoveFourBarToClear;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;

public class MoveToDeliver extends SequentialCommandGroup {
    public boolean isExecuting;

    public MoveToDeliver(CDFourBar fourBar, CDArm arm, CDPickup pickup) {
        isExecuting = false;

        addCommands(
                new FourBarSetPosition(fourBar, 0.96),
                new ArmMoveVertical(arm, 0.62),
                new ArmMoveRotate(arm, fourBar, 0.84)
        );
        addRequirements(fourBar, arm, pickup);
    }

    @Override
    public void initialize() {
        super.initialize();
        isExecuting = true;
    }

    @Override
    public void end(boolean interrupted) {
        super.end(interrupted);
        isExecuting = false;
    }
}
