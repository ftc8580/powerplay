package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@TeleOp(name = "distance")

     public class CDDestanceSensor extends LinearOpMode {
        DistanceSensor distance;
        DcMotor motor;

        @Override
        public void runOpMode() {
            distance = hardwareMap.get(DistanceSensor.class, "distance");
            motor = hardwareMap.get(DcMotor.class, "motorIntake");

            double dist = 0;


            waitForStart();
            while (opModeIsActive()) {
                 dist = distance.getDistance(DistanceUnit.CM);
                if (dist < 10) {
                    motor.setPower(0.3);
                } else {
                    motor.setPower(0);




                }
                telemetry.addData("distance ","dist");
                telemetry.update();
            }
        }
    }

