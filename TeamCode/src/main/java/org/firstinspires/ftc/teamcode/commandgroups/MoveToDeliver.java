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
    public MoveToDeliver(CDFourBar fourBar, CDArm arm, CDPickup pickup) {
        addCommands(
                new ArmVerticalHome(arm),
                new FourBarSetPosition(fourBar, 0.8)
                // new MoveFourBarToClear(arm, fourBar, false, pickup.isPickupClosed),
                // new ArmMoveRotate(arm, fourBar, CDArm.ARM_ROTATION_POSITION_FRONT),
                // new FourBarSetPosition(fourBar, CDFourBar.MIDDLE_POSITION_HOME)
        );
        addRequirements(fourBar, arm, pickup);
    }
}
