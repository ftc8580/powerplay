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
    private static final double ARM_ROTATION_RANGE_LEFT_LOW = 0.47; // Use > when evaluating
    private static final double ARM_ROTATION_RANGE_LEFT_HIGH = 0.63; //Use < when evaluating
    private static final double ARM_ROTATION_RANGE_RIGHT_LOW = 0.0; //Use >= when evaluating
    private static final double ARM_ROTATION_RANGE_RIGHT_HIGH = 0.18; //Use < when evaluating
    private static final double ARM_ROTATION_RANGE_DANGER_LOW = 0.18; // Back area is dangerous //Use >= when evaluating
    private static final double ARM_ROTATION_RANGE_DANGER_HIGH = 0.47; // Back area is dangerous //Use <= when evaluating
    private static final double ARM_ROTATION_RANGE_INSIDE_FOURBAR_LOW = 0.333; // Back area is dangerous except this area //Use >= when evaluating
    private static final double ARM_ROTATION_RANGE_INSIDE_FOURBAR_HIGH = 0.353; // Back area is dangerous except this area //Use <= when evaluating
    private static final double ARM_FREELY_ROTATE_VERTICAL_HEIGHT_LOW = 0.0;
    //TODO DO NOT USE BELOW UNTIL CHECK ON ROBOT - arm will likely collide with floor then moved to front. May also hit robot. Will likely need to define safe fourbar position for this.
    private static final double ARM_FREELY_ROTATE_VERTICAL_HEIGHT_HIGH = 0.06; //Notice this is very small range 0-0.06 (Four bar all the way down)

    // Define variables for arm positions
    public static final double ARM_ROTATION_POSITION_FRONT = 0.82;
    public static final double ARM_ROTATION_POSITION_LEFT = 0.56;
    public static final double ARM_ROTATION_POSITION_RIGHT = 0.058; //Notice extra zero
    public static final double ARM_ROTATION_POSITION_BACK = 0.343;
    public static final double ARM_ROTATION_POSITION_HOME = 0.343;
    public static final double ARM_VERTICAL_POSITION_HOME = 0.565;

    // Define scale ranges
    private static final double VERTICAL_SCALE_RANGE_MIN = 0.4;
    private static final double VERTICAL_SCALE_RANGE_MAX = 0.8;
    private static final double ROTATION_SCALE_RANGE_MIN = 0.15;
    private static final double ROTATION_SCALE_RANGE_MAX = 0.37;

    private final Servo verticalServo;
    private final Servo rotationServo;

    public CDArm(CDHardware theHardware){
        verticalServo = theHardware.armVerticalServo;
        rotationServo = theHardware.armRotationServo;

        verticalServo.scaleRange(VERTICAL_SCALE_RANGE_MIN, VERTICAL_SCALE_RANGE_MAX);
        rotationServo.scaleRange(ROTATION_SCALE_RANGE_MIN, ROTATION_SCALE_RANGE_MAX);

        verticalServo.setPosition(0.565);
        rotationServo.setPosition(0.338);
    }

    public double getArmVerticalPosition() {
        return MathUtils.roundDouble(verticalServo.getPosition());
    }

    public double getArmRotationPosition() {
        return MathUtils.roundDouble(rotationServo.getPosition());
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
    }

    public void setArmRotationPosition(double armRotationPositionTarget) {
        rotationServo.setPosition(armRotationPositionTarget);
    }

    public boolean isArmInsideFourBar() {
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

    public boolean isArmClearToMoveFree(CDFourBar fourBar, boolean isPickedUp) {
        return getArmRotationPosition() >= getArmVerticalClearToRotatePosition(fourBar, isPickedUp);
    }

    public boolean isArmVerticalEnough(CDFourBar fourBar, boolean isPickedUp) {
        double precision = getThresholdPrecision();
        double clearToRotatePosition = getArmVerticalClearToRotatePosition(fourBar, isPickedUp);

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

    private double getThresholdPrecision() {
        return isArmInsideFourBar() ? 0.0008 : 0.02;
    }

    private double getArmVerticalPositionMinimum(CDFourBar fourBar) {
        return isArmInsideFourBar() ? 0.38 * fourBar.getFourBarPotentiometerVolts() + 0.52 : 0;
    }

    private double getArmVerticalClearToRotatePosition(CDFourBar fourBar, boolean isPickedUp) {
        return isPickedUp ?
                0.87 * fourBar.getFourBarPotentiometerVolts() - 0.14 :
                0.92 * fourBar.getFourBarPotentiometerVolts() + 0.01;
    }
}
