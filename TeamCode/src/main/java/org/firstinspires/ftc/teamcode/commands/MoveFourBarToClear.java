package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

public class MoveFourBarToClear extends CommandBase {
    private final CDArm arm;
    private final CDFourBar fourBar;
    private boolean isMotorRunning;
    private final boolean mustBeInside;
    private final boolean conePickedUp;

    public MoveFourBarToClear(CDArm subsystem, CDFourBar fourBar, boolean mustBeInside, boolean conePickedUp) {
        this.arm = subsystem;
        this.fourBar = fourBar;
        this.mustBeInside = mustBeInside;
        this.conePickedUp = conePickedUp;
        addRequirements(arm, fourBar);
    }

    @Override
    public void initialize() {
        isMotorRunning = true;
    }

    @Override
    public void execute() {
        if (
                !arm.isArmClearToMoveFree(fourBar, conePickedUp) &&
                (!mustBeInside || !arm.isArmInsideFourBar()) &&
                fourBar.getFourBarPosition() < CDFourBar.ARM_CLEARED_POSITION_HOME
        ) {
            fourBar.setFourBarPosition(CDFourBar.ARM_CLEARED_POSITION_HOME, false);
            isMotorRunning = false;
        }
    }

    @Override
    public boolean isFinished() {
        return !isMotorRunning;
    }
}
