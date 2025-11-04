package frc.robot;

import edu.wpi.first.wpilibj.Counter;

public class ThroughBore_Quadrature {

    private static final int PPR = 2048;

    private final Counter counter;

    public ThroughBore_Quadrature(int upChannel, int downChannel) {
        counter = new Counter();
        counter.setUpSource(upChannel);
        counter.setDownSource(downChannel);
        counter.reset();
    }

    public void reset() {
        counter.reset();
    }

    public int get() {
        return counter.get();
    }

    public double getAngle() {
        return (double) counter.get() / PPR * 360;
    }

    public double getRate() {
        return (360.0 / PPR) / counter.getPeriod();
    }

    public boolean getDirection() {
        return counter.getDirection();
    }
}
