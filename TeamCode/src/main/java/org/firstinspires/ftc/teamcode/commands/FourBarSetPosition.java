package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

import java.util.Objects;

public class FourBarSetPosition extends CommandBase {
    private final CDFourBar fourBar;
    private final double targetPosition;

    public boolean isExecuting;

    public FourBarSetPosition(CDFourBar subsystem, String targetPosition) {
        double target = subsystem.getFourBarPosition();
        if (Objects.equals(targetPosition, "low")) {
            target = .53;
        } else if (Objects.equals(targetPosition, "medium")) {
            target = 0.77;
        } else if (Objects.equals(targetPosition, "high")) {
            target = 1.12;
        }
        this.fourBar = subsystem;
        this.targetPosition = target;
        addRequirements(fourBar);
    }

    public FourBarSetPosition(CDFourBar subsystem, double targetPosition) {
        this.fourBar = subsystem;
        this.targetPosition = targetPosition;
        this.isExecuting = false;
        addRequirements(fourBar);
    }

    @Override
    public void execute() {
        isExecuting = true;
        double currentPosition = fourBar.getFourBarPosition();

        if (currentPosition > targetPosition) {
            fourBar.moveDown(0.7);
        } else if (currentPosition < targetPosition) {
            fourBar.moveUp(0.1);
        }
    }

    @Override
    public boolean isFinished() {
        return fourBar.isArrivedAtTarget(targetPosition);
    }

    @Override
    public void end(boolean interrupted) {
        isExecuting = false;
        fourBar.stop();
    }
}
