package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.ArmVerticalForFourBarDown;
import org.firstinspires.ftc.teamcode.commands.FourBarSetPosition;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;

public class MoveFourBarDown extends SequentialCommandGroup {
    public MoveFourBarDown(CDFourBar fourBar, CDArm arm, CDPickup pickup, double targetPosition) {
        addCommands(
                new ArmVerticalForFourBarDown(arm, fourBar, pickup),
                new FourBarSetPosition(fourBar, targetPosition)
        );
        addRequirements(fourBar, arm);
    }
}
