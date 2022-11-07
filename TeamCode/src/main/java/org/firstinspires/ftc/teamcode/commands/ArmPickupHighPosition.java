package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class ArmPickupHighPosition extends CommandBase {
    private final CDArm arm;
    private final CDRuntime runtime;

    private double targetTimeMs;

    public ArmPickupHighPosition(CDArm subsystem) {
        this.arm = subsystem;
        this.runtime = new CDRuntime();
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        targetTimeMs = arm.getVerticalSweepTimeMs(CDArm.ARM_VERTICAL_PICKUP_HIGH_POSITION);
        runtime.reset();
        arm.setArmVerticalPosition(CDArm.ARM_VERTICAL_PICKUP_HIGH_POSITION);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}