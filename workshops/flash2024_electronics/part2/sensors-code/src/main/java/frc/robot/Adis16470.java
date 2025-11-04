package frc.robot;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.SPI;

public class Adis16470 {

    private static final int X_GYRO_LOW = 0x04; // X-axis gyroscope output, lower word
    private static final int X_GYRO_OUT = 0x06; // X-axis gyroscope output, upper word

    private static final double GYRO_OUT_LSB_TO_DEG_PER_SEC = 0.1;

    private final SPI port;
    private final Counter dataReadyCounter;

    public Adis16470(SPI.Port port, int dataReadyChannel) {
        this.port = new SPI(port);

        dataReadyCounter = new Counter();
        dataReadyCounter.setUpSource(dataReadyChannel);
    }

    public boolean isDataReady() {
        return dataReadyCounter.get() > 0;
    }

    public void resetDataReady() {
        dataReadyCounter.reset();
    }

    public double getGyroXDegPerSec() {
        int low = readRegister(X_GYRO_LOW);
        int high = readRegister(X_GYRO_OUT);

        int data = (low << 16) | high;
        return data * GYRO_OUT_LSB_TO_DEG_PER_SEC;
    }

    private void writeRegister(int register, int value) {
        byte[] buffer = new byte[2];

        // write first byte
        buffer[0] = (byte) (0x80 | (register & 0x7f)); // rw bit HIGH
        buffer[1] = (byte) (value & 0xff);
        port.write(buffer, 2);

        // write second byte
        buffer[0] = (byte) (0x80 | (register + 1)); // rw bit HIGH and register + 1
        buffer[1] = (byte) ((value >> 8) & 0xff);
        port.write(buffer, 2);
    }

    private int readRegister(int register) {
        byte[] buffer = new byte[2];
        buffer[0] = (byte) (register & 0x7f); // rw bit LOW

        port.write(buffer, 2);
        port.read(false, buffer, 2);

        return ((buffer[0] << 8) | buffer[1]);
    }
}
