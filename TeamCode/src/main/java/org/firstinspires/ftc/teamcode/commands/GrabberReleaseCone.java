package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDGrabber;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class GrabberReleaseCone extends CommandBase {
    private final CDGrabber grabber;
    private final CDRuntime runtime = new CDRuntime();
    private double targetTimeMs;

    public GrabberReleaseCone(CDGrabber subsystem) {
        this.grabber = subsystem;
        addRequirements(grabber);
    }

    @Override
    public void initialize() {
        double targetPosition = 1;
        targetTimeMs = grabber.getGrabTimeMs(targetPosition);
        runtime.reset();
        grabber.setGrabPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
