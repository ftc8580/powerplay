package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class FourBarSetPosition extends CommandBase {
    private final CDFourBar fourBar;
    private final double targetPosition;
    private boolean fourBarRunning;
    private CDRuntime runtime = new CDRuntime();

    public FourBarSetPosition(CDFourBar subsystem, double targetPosition) {
        this.fourBar = subsystem;
        this.targetPosition = targetPosition;
        addRequirements(fourBar);
    }

    @Override
    public void initialize() {
        fourBarRunning = true;
    }

    @Override
    public void execute() {
        runtime.reset();
        while (runtime.seconds() < 2) {
            fourBar.setFourBarPower(targetPosition > 0 ? 1 : -1);
        }
        fourBar.setFourBarPower(0);
        fourBarRunning = false;
    }

    @Override
    public boolean isFinished() {
        return !fourBarRunning;
    }
}
