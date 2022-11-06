package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDGrabber;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class GrabberExtend extends CommandBase {
    private final CDGrabber grabber;
    private final double targetPosition;
    private final CDRuntime runtime = new CDRuntime();
    private double targetTimeMs;

    public GrabberExtend(CDGrabber subsystem, double targetPosition) {
        this.grabber = subsystem;
        this.targetPosition = targetPosition;
        addRequirements(grabber);
    }

    @Override
    public void initialize() {
        targetTimeMs = grabber.getExtendTimeMs(targetPosition);
        runtime.reset();
        grabber.setExtendPosition(targetPosition);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
