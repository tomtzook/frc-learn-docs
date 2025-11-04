package frc.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalOutput;

public class HcSr04 {

    public static final double SPEED_OF_SOUND_CM_PER_SEC = 34300;

    private final DigitalOutput trigger;
    private final Counter echoCounter;

    public HcSr04(int triggerChannel, int echoChannel) {
        trigger = new DigitalOutput(triggerChannel);
        echoCounter = new Counter(echoChannel);
        echoCounter.setSemiPeriodMode(true);
        echoCounter.reset();
    }

    public void ping() {
        echoCounter.reset();
        trigger.pulse(1e-5);
    }

    public double getDistanceCm() {
        if (echoCounter.get() <= 1) {
            return -1;
        }

        return echoCounter.getPeriod() * SPEED_OF_SOUND_CM_PER_SEC;
    }
}
