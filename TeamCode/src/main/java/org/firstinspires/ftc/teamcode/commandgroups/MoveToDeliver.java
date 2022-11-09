package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.ArmMoveRotate;
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
                new ArmVerticalHome(arm),
                new FourBarSetPosition(fourBar, CDFourBar.ARM_CLEARED_POSITION_HOME),
                // new MoveFourBarToClear(arm, fourBar, false, pickup.isPickupClosed),
                new ArmMoveRotate(arm, fourBar, CDArm.ARM_ROTATION_POSITION_FRONT),
                new FourBarSetPosition(fourBar, 1) // CDFourBar.MIDDLE_POSITION_HOME
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
