package frc.robot;

import edu.wpi.first.wpilibj.PWM;

public class PwmVictorSpManualDeadband {

    private static final int MIN = 1000;
    private static final int MIN_NEGATIVE = 1480;
    private static final int CENTER = 1500;
    private static final int MIN_POSITIVE = 1520;
    private static final int MAX = 2000;

    private final PWM port;

    public PwmVictorSpManualDeadband(int channel) {
        port = new PWM(channel);
        port.setPeriodMultiplier(PWM.PeriodMultiplier.k1X);
        port.setPulseTimeMicroseconds(0);
        port.setZeroLatch();
    }

    public void set(double speed) {
        if (speed > 0) {
            int max = MAX;
            int min = MIN_POSITIVE;
            int time = (int) (min + speed * (max - min));
            port.setPulseTimeMicroseconds(time);
        } else if (speed < 0) {
            int max = MIN_NEGATIVE;
            int min = MIN;
            int time = (int) (max - Math.abs(speed) * (max - min));
            port.setPulseTimeMicroseconds(time);
        } else {
            port.setPulseTimeMicroseconds(CENTER);
        }
    }
}
