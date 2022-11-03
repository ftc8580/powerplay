package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.CDPickup;

public class PickupGrabCone extends CommandBase {
    private final CDPickup pickup;

    public PickupGrabCone(CDPickup subsystem) {
        pickup = subsystem;
        addRequirements(pickup);
    }

    @Override
    public void initialize() {
        pickup.setServoPosition(0);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
