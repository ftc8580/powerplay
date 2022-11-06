package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDPickup;
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
        targetTimeMs = pickup.getSweepTimeMs(CDPickup.OPEN_POSITION);
        runtime.reset();
        pickup.release();
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
