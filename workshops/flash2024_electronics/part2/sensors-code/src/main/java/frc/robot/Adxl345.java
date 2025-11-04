package frc.robot;

import edu.wpi.first.wpilibj.I2C;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Adxl345 {

    private static final int ADDRESS = 0x1d;
    private static final int REGISTER_DATA_X_START = 0x32;
    private static final double LSB_IN_G = 256;

    private final I2C port;

    public Adxl345() {
        port = new I2C(I2C.Port.kOnboard, ADDRESS);
    }

    public double getXAcceleration() {
        byte[] buffer = new byte[2];
        port.read(REGISTER_DATA_X_START, 2, buffer);

        int lsb = (buffer[0] << 8) | buffer[1];
        return lsb / LSB_IN_G;
    }
}
