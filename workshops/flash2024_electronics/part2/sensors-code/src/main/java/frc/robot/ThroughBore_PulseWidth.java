package frc.robot;

import edu.wpi.first.wpilibj.Counter;

public class ThroughBore_PulseWidth {

    private final Counter counter;

    public ThroughBore_PulseWidth(int channel) {
        counter = new Counter();
        counter.setUpSource(channel);
        counter.setSemiPeriodMode(true);
        counter.reset();
    }

    public double getAngle() {
        return counter.getPeriod() * 10e6 / 1024 * 360;
    }
}
