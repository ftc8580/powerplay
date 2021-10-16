package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;

public class CDElevator {

    CDHardware robotHardware;

    public CDElevator(CDHardware theHardware){

        robotHardware = theHardware;

       // robotHardware.elevatorswitchtop;
       // robotHardware.elevatorswitchmiddle;
       // robotHardware.elevatorswitchbottom;
       // robotHardware.elevatorswitchground;

        robotHardware.elevatormotor.setDirection(DcMotorSimple.Direction.FORWARD);

        robotHardware.elevatormotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setElevatorPower(double pow) {
        robotHardware.elevatormotor.setPower(pow);
    }

    // TODO: Add public method to gotoPosition(Top, Middle, bottom)

    //
    static final double DRIVE_SPEED = 1.0;,
    static final double ELEVATOR_HOLD_VOLTS = 0.1;
    static final double POS_TOLERANCE = 0.05;
    static final double LOW_POSITION_VOLTS = 3.3;
    static final double PICKUP_POSITION_VOLTS = 3.05;
    static final double MIDDLE_POSITION_VOLTS = 1.15;
    static final double HIGH_POSITION_VOLTS = 0.80;
    HardwarePlatter theHardwarePlatter;
    private double driveSpeedSetPoint = 0.0;
    private boolean is_moving = false;
    private double targetPosition;
    public Elevator(HardwarePlatter hwPlatter) {
        theHardwarePlatter = hwPlatter;
    }
    private void gotoPosition() {
        double error = targetPosition - theHardwarePlatter.ElevatorPotentiometer.getVoltage();
        if (Math.abs(error) > POS_TOLERANCE) {
            is_moving = true;
            if (error < 0) {
                driveSpeedSetPoint = INTAKE_DRIVE_SPEED; //up
                driveElevator2();
            } else if (error > 0) {
                driveSpeedSetPoint = -INTAKE_DRIVE_SPEED * 0.5; //down
                driveElevator2();
            }
        } else {
            is_moving = false;
            stop();
        }
    }
    void pickUp() {
        targetPosition = PICKUP_POSITION_VOLTS;
        gotoPosition();
    }
    void middle() {
        targetPosition = MIDDLE_POSITION_VOLTS;
        gotoPosition();
    }
    void low() {
        targetPosition = LOW_POSITION_VOLTS;
        gotoPosition();
    }
    void high() {
        targetPosition = HIGH_POSITION_VOLTS;
        gotoPosition();
    }
    private void driveElevator() {
        theHardwarePlatter.ElevatorDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        theHardwarePlatter.ElevatorDrive.setPower(driveSpeedSetPoint);
        is_moving = false;
    }
    private void driveElevator2() {
        theHardwarePlatter.ElevatorDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        theHardwarePlatter.ElevatorDrive.setPower(driveSpeedSetPoint);
    }
    void move(double speed) {
        driveSpeedSetPoint = Math.pow(speed, 3) * 0.5; //was 0.2
        driveElevator();
    }
    void stop() {
        theHardwarePlatter.ElevatorDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        is_moving = false;
        if (theHardwarePlatter.ElevatorPotentiometer.getVoltage() > DRIVE_POSITION_VOLTS) {
            theHardwarePlatter.ElevatorDrive.setPower(ELEVATOR_HOLD_VOLTS);
        } else {
            theHardwarePlatter.ElevatorDrive.setPower(-ELEVATOR_HOLD_VOLTS);
        }
    }
    boolean moveOrHoldPosition() {
        if (is_moving)
            gotoPosition();
        else
            stop();
        return (is_moving);
    }
    boolean isMoving() {
        return (is_moving);
    }
    void display(Telemetry telemetry) {
        telemetry.addData("pot", theHardwarePlatter.ElevatorPotentiometer.getVoltage());
        telemetry.addData("isMoving", isMoving());
    } }