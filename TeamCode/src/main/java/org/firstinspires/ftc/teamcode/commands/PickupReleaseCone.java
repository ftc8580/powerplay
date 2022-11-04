package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.CDPickup;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class PickupReleaseCone extends CommandBase {
    private final CDPickup pickup;
    private double targetTimeMs;
    private final CDRuntime runtime = new CDRuntime();

    public PickupReleaseCone(CDPickup subsystem) {
        pickup = subsystem;
        addRequirements(pickup);
    }

    @Override
    public void initialize() {
        targetTimeMs = pickup.getSweepTimeMs(1);
        runtime.reset();
        pickup.setServoPosition(1);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
