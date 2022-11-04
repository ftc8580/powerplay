package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDPickup;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class PickupGrabCone extends CommandBase {
    private final CDPickup pickup;
    private double targetTimeMs;
    private final CDRuntime runtime = new CDRuntime();

    public PickupGrabCone(CDPickup subsystem) {
        pickup = subsystem;
        addRequirements(pickup);
    }

    @Override
    public void initialize() {
        targetTimeMs = pickup.getSweepTimeMs(0);
        runtime.reset();
        pickup.setServoPosition(0);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
