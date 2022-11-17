package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class ArmPickupLowPosition extends CommandBase {
    private final CDArm arm;
    private final CDRuntime runtime;

    private double targetTimeMs;

    public ArmPickupLowPosition(CDArm subsystem) {
        this.arm = subsystem;
        this.runtime = new CDRuntime();
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        // TODO: Dial this in so that we're not wasting too much time waiting
        targetTimeMs = 200; // arm.getVerticalSweepTimeMs(CDArm.ARM_VERTICAL_PICKUP_LOW_POSITION);
        runtime.reset();
        arm.setArmVerticalPosition(CDArm.ARM_VERTICAL_PICKUP_LOW_POSITION);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
