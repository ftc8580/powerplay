package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class ArmMoveRotate extends CommandBase {
    private final CDArm arm;
    private final CDFourBar fourBar;
    private double targetPosition;
    private double targetTimeMs;
    private final CDRuntime runtime = new CDRuntime();

    public ArmMoveRotate(CDArm subsystem, CDFourBar fourBar, double targetPosition) {
        this.arm = subsystem;
        this.fourBar = fourBar;
        this.targetPosition = targetPosition;
        addRequirements(arm, fourBar);
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
