package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

public class CDMagneticSwitchTest {
    @TeleOp(name="CDMagneticSwitchTest", group="Linear Opmode")
    public class TouchTest extends LinearOpMode {
        TouchSensor touch;
        DcMotor motor;
        @Override
        public void runOpMode() {
            touch = hardwareMap.get(TouchSensor.class, "Limit");
            motor = hardwareMap.get(DcMotor.class, "Motor");

            waitForStart();
            while (opModeIsActive()) {
                if (touch.isPressed()) {
                    motor.setPower(0);
                } else {
                    motor.setPower(0.3);
                }
                telemetry.addData("Arm Motor Power:", motor.getPower());
                telemetry.update();
            }
        }
    }}
