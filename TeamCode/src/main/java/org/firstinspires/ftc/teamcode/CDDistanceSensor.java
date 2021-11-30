package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import com.qualcomm.robotcore.hardware.DistanceSensor;

public class CDDistanceSensor {

    CDHardware robotHardware;

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





