package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.CDArm;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class ArmMoveRotate extends CommandBase {
    private final CDArm arm;
    private double targetPosition;
    private double targetTimeMs;
    private final CDRuntime runtime = new CDRuntime();

    public ArmMoveRotate(CDArm subsystem, double targetPosition) {
        this.arm = subsystem;
        this.targetPosition = targetPosition;
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        targetTimeMs = arm.getRotationSweepTimeMs(targetPosition);
        runtime.reset();
        arm.setArmRotationPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
