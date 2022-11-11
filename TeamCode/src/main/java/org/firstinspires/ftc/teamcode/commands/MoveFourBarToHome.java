package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

public class MoveFourBarToHome extends CommandBase {
    private final CDArm arm;
    private final CDFourBar fourBar;
    public boolean resetComplete;

    public MoveFourBarToHome(CDArm subsystem, CDFourBar fourBar) {
        this.arm = subsystem;
        this.fourBar = fourBar;
        addRequirements(arm, fourBar);
    }

    @Override
    public void execute() {
        resetComplete = false;
        if (!CDFourBar.fourBarTouchSensor.isPressed()) {
            fourBar.moveDown(0.8);
        } else {
            CDFourBar.LOWER_POSITION_HOME = fourBar.getFourBarPosition();
            resetComplete = true;
        }
    }

    @Override
    public boolean isFinished() {
        return resetComplete;
    }
}
