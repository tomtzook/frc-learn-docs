package frc.robot;

import edu.wpi.first.wpilibj.AnalogInput;

public class Adxl193 {

    private static final double VOLTS_0 = 2.5;
    private static final double VOLTS_PER_G = 0.008;

    private final AnalogInput input;

    public Adxl193(int channel) {
        input = new AnalogInput(channel);
    }

    public double getAcceleration() {
        double volts = input.getVoltage();
        return (volts - VOLTS_0) / VOLTS_PER_G;
    }
}
