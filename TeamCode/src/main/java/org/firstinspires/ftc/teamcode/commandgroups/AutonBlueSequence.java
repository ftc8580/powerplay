package org.firstinspires.ftc.teamcode.commandgroups;

import com.arcrobotics.ftclib.command.SequentialCommandGroup;
import com.arcrobotics.ftclib.drivebase.MecanumDrive;

import org.firstinspires.ftc.teamcode.subsystems.CDArm;
import org.firstinspires.ftc.teamcode.subsystems.CDFourBar;
import org.firstinspires.ftc.teamcode.subsystems.CDGrabber;
import org.firstinspires.ftc.teamcode.subsystems.CDPickup;

public class AutonBlueSequence extends SequentialCommandGroup {
    public AutonBlueSequence(
            MecanumDrive drive,
            CDFourBar fourBar,
            CDArm arm,
            CDPickup pickup,
            CDGrabber grabber
    ) {
        addCommands(
            // Setup location
            //Scan signal cone.
            //Move forward 2 squares
            //move arm forward
            //Turn 90 degrees left
            //Release cone into medium
            //Move backwards 1 square
            //Lower arm
            //Take cone
            //Move forward 1 square
            //Raise arm
            //Release cone into high
            //Move backwards 1 square
            //Lower arm
            //Pick up cone
            //Raise arm
            //Release cone into low
            //Lower arm
            //Pick up cone
            //move forward 1 square
            //Raise arm
            //Release cone into medium
            //Move backwards 1 square
            //Lower arm
            //Pick up cone
            //Move forward 1 square
            //Raise arm
            //Release cone into high
            //Move backwards 1 square
            //Lower arm
            //Pick up cone
            //Move forward 1 square
            //Raise arm
            //Release cone into medium
            //Park in designated parking spot
        );
        addRequirements(fourBar, arm, pickup, grabber);
    }
}
