package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

public class FourBarSetPosition extends CommandBase {
    private final CDFourBar fourBar;
    private final double targetPosition;

    public FourBarSetPosition(CDFourBar subsystem, double targetPosition) {
        this.fourBar = subsystem;
        this.targetPosition = targetPosition;
        addRequirements(fourBar);
    }

    @Override
    public void execute() {
        fourBar.setFourBarPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
