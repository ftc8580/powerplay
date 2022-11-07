package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;

import org.firstinspires.ftc.teamcode.commands.ArmPickupHighPosition;
import org.firstinspires.ftc.teamcode.commands.ArmPickupLowPosition;
import org.firstinspires.ftc.teamcode.commands.FourBarSetPosition;
import org.firstinspires.ftc.teamcode.commands.PickupGrabCone;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;

public class PickupCone extends SequentialCommandGroup {
    public PickupCone(CDArm arm, CDPickup pickup, CDFourBar fourBar) {
        addCommands(
                new FourBarSetPosition(fourBar, CDFourBar.LOWER_POSITION_HOME),
                new ArmPickupLowPosition(arm),
                new PickupGrabCone(pickup),
                new ArmPickupHighPosition(arm)
        );
        addRequirements(arm, pickup);
    }
}
