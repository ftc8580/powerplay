package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class ArmVerticalForFourBarDown extends CommandBase {
    private final CDArm arm;
    private final CDFourBar fourBar;
    private final CDPickup pickup;
    private final CDRuntime runtime = new CDRuntime();
    private final boolean shouldAdjustArm;

    private double targetTimeMs;

    public ArmVerticalForFourBarDown(CDArm subsystem, CDFourBar fourBar, CDPickup pickup) {
        this.arm = subsystem;
        this.fourBar = fourBar;
        this.pickup = pickup;
        this.shouldAdjustArm = this.arm.isArmBack() || this.arm.isArmFront();
        addRequirements(arm, pickup);
    }

    @Override
    public void initialize() {
        double targetPosition = CDArm.ARM_VERTICAL_POSITION_HOME;

        if (arm.isArmBack()) {
            targetPosition = 0; // arm.getArmVerticalClearToRotatePosition(fourBar, pickup.isPickupClosed);
        } else if (arm.isArmFront()) {
            // TODO: Use actual value
            targetPosition = CDArm.ARM_VERTICAL_PICKUP_LOW_POSITION;
        }

        targetTimeMs = arm.getRotationSweepTimeMs(targetPosition);
        runtime.reset();
        arm.setArmVerticalPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return !shouldAdjustArm || runtime.isTimedOutMs(targetTimeMs);
    }
}
