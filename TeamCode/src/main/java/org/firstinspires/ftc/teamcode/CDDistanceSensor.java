package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.samples.CDHardware;

public class CDDistanceSensor {

    org.firstinspires.ftc.teamcode.CDHardware robotHardware;

    public CDDistanceSensor(CDHardware theHardware) {

        robotHardware = theHardware;
    }
    public double getElevatorDistance() {
      return robotHardware.elevatordistancesensor.getDistance(DistanceUnit.CM);
    }
    public double getIntakeDistance() {
        return robotHardware.intakedistancesensor.getDistance(DistanceUnit.CM);
    }
}





