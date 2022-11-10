package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.util.CDTelemetry;

public class MoveFourBarToClear extends CommandBase {
    private final CDArm arm;
    private final CDFourBar fourBar;
    private final Telemetry robotTelemetry;
    private final boolean mustBeInside;
    private final boolean conePickedUp;

    public MoveFourBarToClear(CDArm subsystem, CDFourBar fourBar, boolean mustBeInside, boolean conePickedUp) {
        this.arm = subsystem;
        this.fourBar = fourBar;
        this.mustBeInside = mustBeInside;
        this.conePickedUp = conePickedUp;
        this.robotTelemetry = CDTelemetry.getInstance();
        addRequirements(arm, fourBar);
    }

    @Override
    public void execute() {
        if (
                !arm.isArmClearToRotateFree(fourBar, conePickedUp) && //true
                (!mustBeInside || !arm.isArmRotationInsideFourBar()) && //true
                fourBar.getFourBarPosition() < CDFourBar.ARM_CLEARED_POSITION_HOME //true
        ) {
            robotTelemetry.addLine("Moving to clear!!!");
            robotTelemetry.update();
            fourBar.setFourBarPosition(CDFourBar.ARM_CLEARED_POSITION_HOME);
        }
    }

    @Override
    public boolean isFinished() {
        return true;
    }
}
