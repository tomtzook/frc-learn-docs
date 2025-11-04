package frc.robot;

import edu.wpi.first.wpilibj.PWM;

public class PwmVictorSp {

    private final PWM port;

    public PwmVictorSp(int channel) {
        port = new PWM(channel);
        port.setBoundsMicroseconds(2004, 1520, 1500, 1480, 997);
        port.setPeriodMultiplier(PWM.PeriodMultiplier.k1X);
        port.setSpeed(0);
        port.setZeroLatch();
    }

    public void set(double speed) {
        port.setSpeed(speed);
    }
}
