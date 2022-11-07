package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class ArmHomeIfInsideFourBar extends CommandBase {
    private final CDArm arm;
    private final CDRuntime runtime = new CDRuntime();
    private final boolean armInsideFourBar;

    private double targetTimeMs;

    public ArmHomeIfInsideFourBar(CDArm subsystem, CDFourBar fourBar, CDPickup pickup) {
        this.arm = subsystem;
        armInsideFourBar =
                this.arm.isArmRotationInsideFourBar() &&
                this.arm.isArmVerticalWithinFourBar(fourBar, pickup.isPickupClosed);
        addRequirements(arm, fourBar);
    }

    @Override
    public void initialize() {
        if (armInsideFourBar) {
            targetTimeMs = arm.getRotationSweepTimeMs(CDArm.ARM_ROTATION_POSITION_HOME);
            runtime.reset();
            arm.setArmRotationPosition(CDArm.ARM_ROTATION_POSITION_HOME);
            arm.setArmVerticalPosition(CDArm.ARM_VERTICAL_POSITION_HOME);
        }
    }

    @Override
    public boolean isFinished() {
        return !armInsideFourBar || runtime.isTimedOutMs(targetTimeMs);
    }
}
