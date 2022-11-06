package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.ArmHomeIfInsideFourBar;
import org.firstinspires.ftc.teamcode.commands.FourBarSetPosition;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;

public class MoveFourBarUp extends SequentialCommandGroup {
    public MoveFourBarUp(CDFourBar fourBar, CDArm arm, CDPickup pickup, double targetPosition) {
        addCommands(
                new ArmHomeIfInsideFourBar(arm, fourBar, pickup),
                new FourBarSetPosition(fourBar, targetPosition)
        );
        addRequirements(fourBar, arm);
    }
}
