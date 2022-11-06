package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;

public class MoveFourBarToHome extends CommandBase {
    private final CDArm arm;
    private final CDFourBar fourBar;

    public MoveFourBarToHome(CDArm subsystem, CDFourBar fourBar) {
        this.arm = subsystem;
        this.fourBar = fourBar;
        addRequirements(arm, fourBar);
    }

    @Override
    public void execute() {
        fourBar.setFourBarPosition(CDFourBar.LOWER_POSITION_HOME);
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
