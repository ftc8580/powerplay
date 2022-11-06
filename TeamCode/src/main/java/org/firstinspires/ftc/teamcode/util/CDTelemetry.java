package org.firstinspires.ftc.teamcode.util;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class CDTelemetry {
    private static CDTelemetry instance;
    private final Telemetry telemetry;

    private CDTelemetry(Telemetry telemetry) {
        this.telemetry = telemetry;
    }

    public static Telemetry getInstance() {
        if (instance == null) {
            throw new RuntimeException("CDTelemetry has not been initialized");
        }
        return instance.telemetry;
    }

    public static Telemetry initialize(Telemetry telemetry) {
        instance = new CDTelemetry(telemetry);
        instance.telemetry.setAutoClear(false);
        return instance.telemetry;
    }
}
