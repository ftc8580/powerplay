package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CDHardware;
import org.firstinspires.ftc.teamcode.util.CDRuntime;
import org.firstinspires.ftc.teamcode.util.CDTelemetry;
import org.firstinspires.ftc.teamcode.util.MathUtils;

import java.util.Objects;

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
    private final static double TIMEOUT_MS = 2000;

    private final AnalogInput fourBarPotentiometer;
    private final CDRuntime runtime = new CDRuntime();
    private final DcMotor fourBarMotor;
    private final Telemetry robotTelemetry;

    public boolean autonMode;
    public double fourBarPositionLast;

    public CDFourBar(CDHardware theHardware) {
        autonMode = false;

        fourBarMotor = theHardware.fourBarMotor;
        fourBarPotentiometer = theHardware.fourBarPotentiometer;
        robotTelemetry = CDTelemetry.getInstance();

        fourBarMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        fourBarMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        fourBarMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        setFourBarPosition(LOWER_POSITION_HOME);
    }

    public void setFourBarPower(double pow) {
        robotTelemetry.addData("fb in range", isInRange());
        robotTelemetry.addData("fb power", "%.2f", pow);
        robotTelemetry.update();
        if (!isInRange() && pow != 0) {
            pow = 0;
        }

        if (isInSlowRange()) {
            pow = pow * 0.5;
        }

        fourBarMotor.setPower(pow * FOUR_BAR_SLOW_SPEED_MULTIPLIER);
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

    public boolean setFourBarPosition(double fourBarPositionTarget) {
        // This method will return true for successful turn or false for an error.
        // TODO: Need to confirm threshold is good
        double FOURBAR_THRESHOLD_DELTA = 0.01;

        double fourBarSpeedMultiple; // to set the  speed of the fourbar
        double fourBarPositionDelta;
        runtime.reset();
        while (true) {
            //TODO Check if 2000 is long enough for timeout here???
            if (runtime.isTimedOutMs(TIMEOUT_MS)) {
                return true;
            }
            fourBarPositionDelta = Math.abs(getFourBarPosition() - fourBarPositionTarget); // Check the current gap between target and current position
            robotTelemetry.addData("position", "%.2f", getFourBarPosition());
            robotTelemetry.addData("target", "%.2f", fourBarPositionTarget);
            robotTelemetry.addData("delta", "%.2f", fourBarPositionDelta);
            robotTelemetry.update();
            if (fourBarPositionDelta <= FOURBAR_THRESHOLD_DELTA) { // Stop tolerance
                setFourBarPower(0.0); // need to stop the fourbar before leaving the loop
                return true;
            }

            if (fourBarPositionDelta <= 0.03) {
                fourBarSpeedMultiple = autonMode ? 0.8 : 0.6;
            } else if (fourBarPositionDelta <= 0.01) {
                fourBarSpeedMultiple = autonMode ? 0.3 : 0.2;
            } else {
                fourBarSpeedMultiple = autonMode ? 1 : 0.8;
            }

            //TODO: Check if -1 or 1 needed here to make it move in the correct direction
            if (fourBarPositionLast > fourBarPositionTarget) {
                setFourBarPower(-1 * fourBarSpeedMultiple);
            } else if (fourBarPositionLast < fourBarPositionTarget) {
                setFourBarPower(1 * fourBarSpeedMultiple);
            }
        }
    }

    public boolean isFourbarHome() {
        return MathUtils.isWithinRange(
                LOWER_POSITION_HOME - POTENTIOMETER_THRESHOLD_PRECISION,
                LOWER_POSITION_HOME + POTENTIOMETER_THRESHOLD_PRECISION,
                getFourBarPosition()
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
