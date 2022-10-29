package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.util.ElapsedTime;

public class CDRuntime extends ElapsedTime {
    public CDRuntime() {
        super();
    }

    public boolean isTimedOut(double timeoutSeconds) {
        return this.seconds() > timeoutSeconds;
    }
}
