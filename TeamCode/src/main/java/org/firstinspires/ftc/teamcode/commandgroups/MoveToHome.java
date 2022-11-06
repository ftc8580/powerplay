package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.ArmRotateHome;
import org.firstinspires.ftc.teamcode.commands.ArmVerticalHome;
import org.firstinspires.ftc.teamcode.commands.FourBarSetPosition;
import org.firstinspires.ftc.teamcode.commands.MoveFourBarToClear;
import org.firstinspires.ftc.teamcode.commands.MoveFourBarToHome;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;

public class MoveToHome extends SequentialCommandGroup {
    public MoveToHome(CDFourBar fourBar, CDArm arm, CDPickup pickup) {
        addCommands(
                new FourBarSetPosition(fourBar, CDFourBar.ARM_CLEARED_POSITION_HOME),
                // new MoveFourBarToClear(arm, fourBar, true, pickup.isPickupClosed),
                new ArmVerticalHome(arm),
                new ArmRotateHome(arm),
                new FourBarSetPosition(fourBar, CDFourBar.LOWER_POSITION_HOME)
                // new MoveFourBarToHome(arm, fourBar)
        );
        addRequirements(fourBar, arm, pickup);
    }
}
