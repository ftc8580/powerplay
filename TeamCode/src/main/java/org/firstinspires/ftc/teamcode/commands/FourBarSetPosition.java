package org.firstinspires.ftc.teamcode.commands;

import com.arcrobotics.ftclib.command.CommandBase;

import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.util.CDRuntime;

import java.util.Objects;

public class FourBarSetPosition extends CommandBase {
    private CDFourBar fourBar;
    private double targetPosition;
    private double currentPosition;
    private boolean moveUp;
    private boolean moveDown;
    private boolean precision;

    public boolean isExecuting;

    public FourBarSetPosition(CDFourBar subsystem, String targetPosition) {
        double target = subsystem.getFourBarPosition();
        if (Objects.equals(targetPosition, "low")) {
            target = CDFourBar.LOWER_POSITION_HOME + 0.31;//.53;
        } else if (Objects.equals(targetPosition, "medium")) {
            target = CDFourBar.LOWER_POSITION_HOME + 0.55;//0.77;
        } else if (Objects.equals(targetPosition, "high")) {
            target = CDFourBar.LOWER_POSITION_HOME + 0.9 ;//1.12;
        }
        this.precision = false;
        initializeLocals(subsystem, target);
        addRequirements(fourBar);
    }

    public FourBarSetPosition(CDFourBar subsystem, double targetPosition) {
        this.precision = false;
        initializeLocals(subsystem, targetPosition);
        addRequirements(fourBar);
    }

    public FourBarSetPosition(CDFourBar subsystem, double targetPosition, boolean precision) {
        this.precision = precision;
        initializeLocals(subsystem, targetPosition);
        addRequirements(fourBar);
    }

    private void initializeLocals(CDFourBar fourBar, double targetPosition) {
        this.fourBar = fourBar;
        this.targetPosition = targetPosition;
        this.isExecuting = false;
        this.currentPosition = this.fourBar.getFourBarPosition();
        this.moveUp = false;
        this.moveDown = false;
    }

    @Override
    public void execute() {
        isExecuting = true;
        currentPosition = fourBar.getFourBarPosition();

        if (!moveUp && !moveDown) {
            moveUp = this.currentPosition < this.targetPosition;
            moveDown = !moveUp;
        }

        // double fourBarSpeed = fourBar.calculateFourBarSpeedLinear(targetPosition, currentPosition, 1.5); // avg speed home -> unicorn: 0.62, med: 0.33, low: 0.10
        // double fourBarSpeed = fourBar.calculateFourBarSpeedLinear(targetPosition, currentPosition, 3); // avg speed home -> unicorn: 0.71, med: 0.49, low: 0.14
        // double fourBarSpeed = fourBar.calculateFourBarSpeedExponential(targetPosition, currentPosition, 2); // avg speed home -> unicorn: 0.58, med: 0.29, low: 0.09
        // double fourBarSpeed = fourBar.calculateFourBarSpeedExponential(targetPosition, currentPosition, 5); // avg speed home -> unicorn: 0.70, med: 0.48, low: 0.13
        // double fourBarSpeed = fourBar.calculateFourBarSpeedExponential(targetPosition, currentPosition, 7); // avg speed home -> unicorn: 0.73, med: 0.53, low: 0.15

        if (this.currentPosition > this.targetPosition && moveDown) {
            fourBar.moveDown(precision ? 0.4 : 1);
        } else if (this.currentPosition < this.targetPosition && moveDown) {
            moveDown = false;
            moveUp = true;
            precision = true;
        } else if (this.currentPosition < this.targetPosition && moveUp) {
            fourBar.moveUp(precision ? 0.4 : 1);
        } else if (this.currentPosition > this.targetPosition && moveUp) {
            moveUp = false;
            moveDown = true;
            precision = true;
        }
    }

    @Override
    public boolean isFinished() {
        return fourBar.isArrivedAtTarget(targetPosition, currentPosition);
        /* return (moveDown && currentPosition < targetPosition) ||
                (moveUp && currentPosition > targetPosition) ||
                fourBar.isArrivedAtTarget(targetPosition, currentPosition); */
    }

    @Override
    public void end(boolean interrupted) {
        isExecuting = false;
        moveDown = false;
        moveUp = false;
        precision = false;
        fourBar.stop();
    }
}
