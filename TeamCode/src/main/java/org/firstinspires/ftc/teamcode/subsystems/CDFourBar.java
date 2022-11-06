package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.arcrobotics.ftclib.hardware.motors.Motor;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.util.CDRuntime;
import org.firstinspires.ftc.teamcode.util.MathUtils;

public class CDFourBar extends SubsystemBase {
    private final static double FOUR_BAR_SLOW_SPEED_MULTIPLIER = .7;
    private final static double ABSOLUTE_UPPER_BOUND_VOLTS = 1.18;
    private final static double ABSOLUTE_LOWER_BOUND_VOLTS = 0.23;
    private final static double LOW_SPEED_UPPER_BOUND_VOLTS = 1.08;
    private final static double LOW_SPEED_LOWER_BOUND_VOLTS = 0.34;

    // Define variables for Home Positions. HOME is back pickup position between fourbars.
    public final static double LOWER_POSITION_HOME = ABSOLUTE_LOWER_BOUND_VOLTS;
    public final static double MIDDLE_POSITION_HOME = 0.8;
    public final static double ARM_CLEARED_POSITION_HOME = 0.8; // 0.6 when loaded
    private final static double POTENTIOMETER_THRESHOLD_PRECISION = 0.05;
    public final static double TIMEOUT_MS = 2000;

    private final AnalogInput fourBarPotentiometer;
    private final CDRuntime runtime = new CDRuntime();
    private final Motor fourBarMotor;

    public boolean autonMode;

    public CDFourBar(HardwareMap hardwareMap) {
        autonMode = false;

        fourBarMotor = new Motor(hardwareMap, "motorFourBar");
        fourBarPotentiometer = hardwareMap.get(AnalogInput.class, "fourBarPos");

        fourBarMotor.setRunMode(Motor.RunMode.RawPower);
        fourBarMotor.setZeroPowerBehavior(Motor.ZeroPowerBehavior.BRAKE);

        setFourBarPosition(LOWER_POSITION_HOME);
    }

    public void moveUp() {
        setFourBarPower(-1);
    }

    public void moveDown() {
        setFourBarPower(1);
    }

    public void stop() {
        fourBarMotor.stopMotor();
    }

    public void setFourBarPower(double pow) {
        if (!isInRange() && pow != 0) {
            return;
        }

        if (isInSlowRange()) {
            pow = pow * 0.5;
        }

        fourBarMotor.set(pow * FOUR_BAR_SLOW_SPEED_MULTIPLIER);
    }

    public double getFourBarPosition() {
        // Reference https://docs.revrobotics.com/potentiometer/untitled-1#calculating-the-relationship-between-voltage-and-angle
        return fourBarPotentiometer.getVoltage();
    }

    /* public boolean setFourBarDirection(String fourBarLocationTarget) {
        // This method will return false for successful turn or true for an error.
        boolean fourBarError = false;
        //TODO  NEED ROBOT: Update fourbarpostarget values below to match readings from robot
        if (Objects.equals(fourBarLocationTarget, "ground")) {
            fourBarError = setFourBarPosition(.29);
        } else if (Objects.equals(fourBarLocationTarget, "low")) {
            fourBarError = setFourBarPosition(.58);
        } else if (Objects.equals(fourBarLocationTarget, "medium")) {
            fourBarError = setFourBarPosition(2.47);
        } else if (Objects.equals(fourBarLocationTarget, "high")) {
            fourBarError = setFourBarPosition(2.87);
        }
        return fourBarError;
    } */

    public boolean setFourBarPosition(double positionTarget) {
        // This method will return true for successful turn or false for an error.

        while (!isArrivedAtTarget(positionTarget) && !runtime.isTimedOutMs(TIMEOUT_MS)) {
            if (getFourBarPosition() > positionTarget) {
                moveUp();
            } else if (getFourBarPosition() < positionTarget) {
                moveDown();
            }
        }
        stop();
        return true;
    }

    public boolean isFourbarHome() {
        return MathUtils.isWithinRange(
                LOWER_POSITION_HOME - POTENTIOMETER_THRESHOLD_PRECISION,
                LOWER_POSITION_HOME + POTENTIOMETER_THRESHOLD_PRECISION,
                getFourBarPosition()
        );
    }

    private boolean isArrivedAtTarget(double target) {
        // TODO: Need to confirm threshold is good
        double FOURBAR_THRESHOLD_DELTA = 0.01;

        return MathUtils.isWithinRange(
                -FOURBAR_THRESHOLD_DELTA,
                FOURBAR_THRESHOLD_DELTA,
                Math.abs(getFourBarPosition() - target)
        );
    }

    private boolean isInRange() {
        return MathUtils.isWithinRange(
                ABSOLUTE_LOWER_BOUND_VOLTS,
                ABSOLUTE_UPPER_BOUND_VOLTS,
                getFourBarPosition()
        );
    }

    private boolean isInSlowRange() {
        double currentPosition = getFourBarPosition();
        return currentPosition >= LOW_SPEED_UPPER_BOUND_VOLTS || currentPosition <= LOW_SPEED_LOWER_BOUND_VOLTS;
    }
}
