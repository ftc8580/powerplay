package org.firstinspires.ftc.teamcode.subsystems;

import com.arcrobotics.ftclib.command.SubsystemBase;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.CDHardware;
import org.firstinspires.ftc.teamcode.util.MathUtils;
import org.firstinspires.ftc.teamcode.util.ServoUtils;

public class CDArm extends SubsystemBase {
    // Define state limits
    private static final double ARM_ROTATION_RANGE_FRONT_LOW = 0.630; // Use >= when evaluating
    private static final double ARM_ROTATION_RANGE_FRONT_HIGH = 1.0;// Use <= when evaluating
    private static final double ARM_ROTATION_RANGE_LEFT_LOW = 0.471; // Use > when evaluating
    private static final double ARM_ROTATION_RANGE_LEFT_HIGH = 0.629; // Use < when evaluating
    private static final double ARM_ROTATION_RANGE_RIGHT_LOW = 0.0; // Use >= when evaluating
    private static final double ARM_ROTATION_RANGE_RIGHT_HIGH = 0.179; // Use < when evaluating
    private static final double ARM_ROTATION_RANGE_DANGER_LOW = 0.18; // Back area is dangerous //Use >= when evaluating
    private static final double ARM_ROTATION_RANGE_DANGER_HIGH = 0.47; // Back area is dangerous //Use <= when evaluating
    private static final double ARM_ROTATION_RANGE_INSIDE_FOURBAR_LOW = 0.333; // Back area is dangerous except this area //Use >= when evaluating
    private static final double ARM_ROTATION_RANGE_INSIDE_FOURBAR_HIGH = 0.353; // Back area is dangerous except this area //Use <= when evaluating
    private static final double ARM_FREELY_ROTATE_VERTICAL_HEIGHT_LOW = 0.0;
    //TODO DO NOT USE BELOW UNTIL CHECK ON ROBOT - arm will likely collide with floor then moved to front. May also hit robot. Will likely need to define safe fourbar position for this.
    private static final double ARM_FREELY_ROTATE_VERTICAL_HEIGHT_HIGH = 0.06; // Notice this is very small range 0-0.06 (Four bar all the way down)
    private static final double ARM_CLEAR_TO_ROTATE_WITH_CONE_POSITION = .415; // (.87* fourBarPositionCurrent - .14);
    private static final double INITIAL_ARM_ROTATION_POSITION = 0.333;

    // Define variables for rotation positions
    public static final double ARM_ROTATION_POSITION_FRONT = 0.82;
    public static final double ARM_ROTATION_POSITION_LEFT = 0.56;
    public static final double ARM_ROTATION_POSITION_RIGHT = 0.058; //Notice extra zero
    public static final double ARM_ROTATION_POSITION_BACK = 0.333;
    public static final double ARM_ROTATION_POSITION_HOME = ARM_ROTATION_POSITION_BACK;

    public static final double ALLEY_DELIVERY_LEFT_ROTATION = 0.43;
    public static final double ALLEY_DELIVERY_RIGHT_ROTATION = 0.19;

    // Define variables for rotation positions
    public static final double ARM_VERTICAL_POSITION_HOME = 0.415;
    public static final double ARM_VERTICAL_PICKUP_LOW_POSITION = 0.555;
    public static final double ARM_VERTICAL_PICKUP_HIGH_POSITION = ARM_VERTICAL_POSITION_HOME;
    private static final double ARM_ADD_TO_DROP_HEIGHT_CLEAR = 0.05;

    // Define scale ranges
    // TODO: Need to scale differently in different places. Need new variables?
    private static final double VERTICAL_SCALE_RANGE_MIN = 0.4;
    private static final double VERTICAL_SCALE_RANGE_MAX = 0.8;
    private static final double ROTATION_SCALE_RANGE_MIN = 0.15;
    private static final double ROTATION_SCALE_RANGE_MAX = 0.37;

    private final Servo verticalServo;
    private final Servo rotationServo;

    private double verticalPosition;
    private double rotationPosition;

    public CDArm(CDHardware theHardware){
        verticalServo = theHardware.armVerticalServo;
        rotationServo = theHardware.armRotationServo;

        verticalServo.scaleRange(VERTICAL_SCALE_RANGE_MIN, VERTICAL_SCALE_RANGE_MAX);
        rotationServo.scaleRange(ROTATION_SCALE_RANGE_MIN, ROTATION_SCALE_RANGE_MAX);

        verticalServo.setPosition(ARM_CLEAR_TO_ROTATE_WITH_CONE_POSITION);
        verticalPosition = ARM_CLEAR_TO_ROTATE_WITH_CONE_POSITION;
        rotationServo.setPosition(INITIAL_ARM_ROTATION_POSITION);
        rotationPosition = INITIAL_ARM_ROTATION_POSITION;
    }

    public double getArmVerticalPosition() {
        return MathUtils.roundDouble(verticalPosition);
    }

    public double getArmRotationPosition() {
        return MathUtils.roundDouble(rotationPosition);
    }

    public double getVerticalSweepTimeMs(double armVerticalPositionTarget) {
        return ServoUtils.getSweepTimeMs(
                getArmVerticalPosition(),
                armVerticalPositionTarget,
                VERTICAL_SCALE_RANGE_MIN,
                VERTICAL_SCALE_RANGE_MAX
        );
    }

    public double getRotationSweepTimeMs(double armRotationPositionTarget) {
        return ServoUtils.getSweepTimeMs(
                getArmRotationPosition(),
                armRotationPositionTarget,
                ROTATION_SCALE_RANGE_MIN,
                ROTATION_SCALE_RANGE_MAX
        );
    }

    public void setArmVerticalPosition(double armVerticalPositionTarget) {
        verticalServo.setPosition(armVerticalPositionTarget);
        verticalPosition = armVerticalPositionTarget;
    }

    // TODO: Replace all instances of setArmVerticalPosition with this
    public void setArmVerticalPositionSafe(CDFourBar fourBar, double armVerticalPositionTarget) {
        if (isVerticalMoveWithinSafeRange(fourBar, armVerticalPositionTarget)) {
            // Do nothing
        } else if (isArmFront()) {
            // TODO: Figure out what needs to happen here
        } else if (isArmBack()) {
            armVerticalPositionTarget = getArmVerticalPositionMinimum(fourBar);
        } else {
            armVerticalPositionTarget = 0.68;
        }

        setArmVerticalPosition(armVerticalPositionTarget);
    }

    private boolean isVerticalMoveWithinSafeRange(CDFourBar fourBar, double target) {
        if (isArmFront()) {
            // TODO: Determine safe range
            return true;
        } else if (isArmBack()) {
            return target >= getArmVerticalPositionMinimum(fourBar);
        } else {
            return target <= 0.68;
        }
    }

    public void setArmRotationPosition(double armRotationPositionTarget) {
        rotationServo.setPosition(armRotationPositionTarget);
        rotationPosition = armRotationPositionTarget;
    }

    public void setArmDeliveryLeft() {
        setArmRotationPosition(ALLEY_DELIVERY_LEFT_ROTATION);
    }

    public void setArmDeliveryRight() {
        setArmRotationPosition(ALLEY_DELIVERY_RIGHT_ROTATION);
    }

    public boolean isArmRotationInsideFourBar() {
        return MathUtils.isWithinRange(
                ARM_ROTATION_RANGE_INSIDE_FOURBAR_LOW,
                ARM_ROTATION_RANGE_INSIDE_FOURBAR_HIGH,
                getArmRotationPosition()
        );
    }

    public boolean isArmHome() {
        double precision = getThresholdPrecision();

        return MathUtils.isWithinRange(
                ARM_ROTATION_POSITION_HOME - precision,
                ARM_ROTATION_POSITION_HOME + precision,
                getArmRotationPosition()
        ) && MathUtils.isWithinRange(
                ARM_VERTICAL_POSITION_HOME - precision,
                ARM_VERTICAL_POSITION_HOME + precision,
                getArmVerticalPosition()
        );
    }

    public boolean isArmBack() {
        return MathUtils.isWithinRange(
                ARM_ROTATION_RANGE_DANGER_LOW,
                ARM_ROTATION_RANGE_DANGER_HIGH,
                getArmRotationPosition()
        );
    }

    public boolean isArmLeft() {
        return MathUtils.isWithinRange(
                ARM_ROTATION_RANGE_LEFT_LOW,
                ARM_ROTATION_RANGE_LEFT_HIGH,
                getArmRotationPosition()
        );
    }

    public boolean isArmRight() {
        return MathUtils.isWithinRange(
                ARM_ROTATION_RANGE_RIGHT_LOW,
                ARM_ROTATION_RANGE_RIGHT_HIGH,
                getArmRotationPosition()
        );
    }

    public boolean isArmFront() {
        return MathUtils.isWithinRange(
                ARM_ROTATION_RANGE_FRONT_LOW,
                ARM_ROTATION_RANGE_FRONT_HIGH,
                getArmRotationPosition()
        );
    }

    public boolean isArmInDangerZone() {
        return MathUtils.isWithinRange(
                ARM_ROTATION_RANGE_DANGER_LOW,
                ARM_ROTATION_RANGE_DANGER_HIGH,
                getArmRotationPosition()
        );
    }

    public boolean isArmClearToRotateFree(CDFourBar fourBar, boolean isPickupClosed) {
        return getArmVerticalPosition() <= getArmVerticalClearToRotatePosition(fourBar, isPickupClosed);
    }

    public boolean isArmVerticalEnough(CDFourBar fourBar, boolean isPickupClosed) {
        double precision = getThresholdPrecision();
        double clearToRotatePosition = getArmVerticalClearToRotatePosition(fourBar, isPickupClosed);

        return MathUtils.isWithinRange(
                ARM_FREELY_ROTATE_VERTICAL_HEIGHT_LOW,
                ARM_FREELY_ROTATE_VERTICAL_HEIGHT_HIGH,
                getArmVerticalPosition()
        ) || MathUtils.isWithinRange(
                clearToRotatePosition - precision,
                clearToRotatePosition + precision,
                getArmRotationPosition()
        );
    }

    public boolean isArmVerticalWithinFourBar(CDFourBar fourBar, boolean isPickupClosed) {
        return MathUtils.isWithinRange(
                getArmVerticalPositionMinimum(fourBar),
                getArmVerticalClearToRotatePosition(fourBar, isPickupClosed),
                getArmVerticalPosition()
        );
    }

    private double getThresholdPrecision() {
        return isArmRotationInsideFourBar() ? 0.0008 : 0.02;
    }

    private double getArmVerticalPositionMinimum(CDFourBar fourBar) {
        return isArmRotationInsideFourBar() ? 0.38 * fourBar.getFourBarPosition() + 0.52 : 0;
    }

    // TODO: Revert back to private
    public double getArmVerticalClearToRotatePosition(CDFourBar fourBar, boolean isPickupClosed) {
        return isPickupClosed ?
                0.87 * fourBar.getFourBarPosition() - 0.14 :
                0.92 * fourBar.getFourBarPosition() + 0.01;
    }
}
