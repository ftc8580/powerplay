package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

public class FourBarSetPosition extends CommandBase {
    private final CDFourBar fourBar;
    private final double targetPosition;

    private boolean moveComplete;

    public FourBarSetPosition(CDFourBar subsystem, double targetPosition) {
        this.fourBar = subsystem;
        this.targetPosition = targetPosition;
        addRequirements(fourBar);
    }

    @Override
    public void initialize() {
        moveComplete = false;
    }

    @Override
    public void execute() {
        moveComplete = fourBar.setFourBarPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return moveComplete;
    }
}
