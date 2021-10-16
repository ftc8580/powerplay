package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

public class CDMagneticSwitchTest {
    @TeleOp(name="CDMagneticSwitchTest", group="Linear Opmode")
    public class TouchTest extends LinearOpMode {
        TouchSensor touch;
        DcMotor leftfrontmotor;
        @Override
        public void runOpMode() {
            touch = hardwareMap.get(TouchSensor.class, "magnetic");
            leftfrontmotor = hardwareMap.get(DcMotor.class, "motorLF");

            waitForStart();
            while (opModeIsActive()) {
                if (touch.isPressed()) {
                    leftfrontmotor.setPower(0);
                } else {
                    leftfrontmotor.setPower(0.3);
                }
                telemetry.addData("Arm Motor Power:", leftfrontmotor.getPower());
                telemetry.update();
            }
        }
    }}
