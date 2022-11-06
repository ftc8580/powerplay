package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.util.CDRuntime;
import org.firstinspires.ftc.teamcode.util.CDTelemetry;

public class ArmMoveVertical extends CommandBase {
    private final CDArm arm;
    private final double targetPosition;
    private double targetTimeMs;
    private final CDRuntime runtime = new CDRuntime();
    private final Telemetry robotTelemetry;

    public ArmMoveVertical(CDArm subsystem, double targetPosition) {
        this.arm = subsystem;
        this.targetPosition = targetPosition;
        this.robotTelemetry = CDTelemetry.getInstance();
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        targetTimeMs = arm.getVerticalSweepTimeMs(targetPosition);
        runtime.reset();
        arm.setArmVerticalPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
