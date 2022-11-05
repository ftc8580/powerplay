package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

public class ArmVerticalHome extends CommandBase {
    private final CDArm arm;
    private double targetTimeMs;
    private final CDRuntime runtime = new CDRuntime();

    public ArmVerticalHome(CDArm subsystem) {
        this.arm = subsystem;
        addRequirements(arm);
    }

    @Override
    public void initialize() {
        targetTimeMs = arm.getVerticalSweepTimeMs(CDArm.ARM_ROTATION_POSITION_HOME);
        runtime.reset();
        arm.setArmVerticalPosition(CDArm.ARM_VERTICAL_POSITION_HOME);
    }

    @Override
    public boolean isFinished() {
        return runtime.isTimedOutMs(targetTimeMs);
    }
}
