package frc.robot;

import edu.wpi.first.wpilibj.PWM;

public class PwmVictorSpManual {

    private static final int MIN = 1000;
    private static final int CENTER = 1500;
    private static final int MAX = 2000;


    private final PWM port;

    public PwmVictorSpManual(int channel) {
        port = new PWM(channel);
        port.setPeriodMultiplier(PWM.PeriodMultiplier.k1X);
        port.setPulseTimeMicroseconds(0);
        port.setZeroLatch();
    }

    public void set(double speed) {
        if (speed > 0) {
            int max = MAX;
            int min = CENTER + 1;
            int time = (int) (min + speed * (max - min));
            port.setPulseTimeMicroseconds(time);
        } else if (speed < 0) {
            int max = CENTER - 1;
            int min = MIN;
            int time = (int) (max - Math.abs(speed) * (max - min));
            port.setPulseTimeMicroseconds(time);
        } else {
            port.setPulseTimeMicroseconds(CENTER);
        }
    }
}
