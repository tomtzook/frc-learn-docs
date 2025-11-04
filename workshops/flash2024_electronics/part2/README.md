In 28.4.2024 We met for the second part of the Electronics Workshop. This part takes the theory we saw in the first part and shows the practical usage of it with robot code and actual devices.

Presentations used:
- [Tom's Slideshow](https://docs.google.com/presentation/d/1-kQRPxuOjtqOZDmWHgj9rgo71iQC9VG4_2SRn7aQ0mo/edit?usp=sharing) - From Slide 66 until the end

Now that we've seen the theory of how electricty works and how it may be used to communicate, let's see how to write code with RoboRIO to actually make it happen.

## RoboRIO I/O Ports

Since the RoboRIO was made specifically for the FRC, it comes with a variety of useful ports for controlling and communicating with different devices.

<img width="500" height="351" alt="RoboRIO Ports" src="https://github.com/user-attachments/assets/2b75b304-696a-4c27-b8f9-c060f5f6ff75" />

We'll go over these ports one by one and see how we can use them in code.

### Digital I/O

The Digital Input/Output (DIO) ports are a row of 10 seperate ports in the side of the RoboRIO. They are used for generating and reading digital signals. Each of these ports is numbered (0-9), and this number will be used in our code to access the specific port.

<img width="512" height="502" alt="DIO Ports Row" src="https://github.com/user-attachments/assets/623d1794-13e5-43be-9a7d-a9085cce02a8" />

Each port in the row is made up of 3 seperate pins: 
- The signal pin (top) for the digital signal 
- The VCC pin (second) for 5V power connection
- The GND pin (bottom) for Ground connection

<img width="69" height="78" alt="Port pins" src="https://github.com/user-attachments/assets/b4a0e67c-39f4-4b87-8252-52c0a4724f81" />

Normally the signal pin is enough, since all we care about is the signal. But the VCC and GND pins allows us to provide power to the devices connected to the signal pin. We'll see more about it later. The VCC and GND ports are all the same and are connected amongst each other throughout all the ports. Only the signal pins are seperate.

Each of the pins can be configured to either output **or** input. So it can be used to either transmit a signal or receive a signal, but not both. In output mode, it will transmit a `5V` for HIGH and less than `0.8V` for LOW. In input mode, it expects a voltage of `2V` and above for HIGH and `0.8V` and below for LOW. This is called TTL level. It does matter, as some devices can only receive a HIGH voltage of up to `3.3V`, so connecting it to the RoboRIO pin may cause it damage. Same goes the other way around, if the device sends a `2V` and higher for HIGH, it will be read fine. But if it transmits `1.5V` for HIGH, the RoboRIO won't be able to recognize it as HIGH.

Each of the signal pins have what is called a _Pull-Up Resistor_. Such resistors are _very high resistance_ resistors which are connected in a circuit between the pin and GND, thus creating a circuit with high resistance. When nothing else is connected to the pin, this circuit remains the only possible path and thus voltage goes through it, because of this, the voltage measure on the pin measures as HIGH. So when nothing is connected to the pin, its state will be read as HIGH. This is important to remember. 

You may ask as to why do this? It is actually quite ingineous. Consider: you have a switch connected to the pin. When the switch is pressed, it transmits a HIGH signal. This switch is important, as it indicates that our, say, arm as reached its limit. Now, what happens when the wire accidentally gets cut (which does happen). Well, in that case we no longer have the safety sensor for our arm. But we won't actually know that, because there is no real indication. But because of the pull-up resistor, the value of the pin is now back to HIGH. So, we read that it is HIGH and stop the motor because we think the arm is at its limit. This protects our arm now that the sensor is inoperable. Some boards have also pull-down resistor, and one can switch between it and the pull-up resistor.

Accessing the pins via code is pretty straight-forward. However, note that a port can only be configured once for a specific use. 

```java
public class Robot extends TimedRobot {
  
  private DigitalInput input;

  @Override
  public void robotInit() {
    // this configures the port to input functionality
    // 1 refers to port number 1
    input = new DigitalInput(1);
  }
  ...
  @Override
  public void teleopPeriodic() {
    boolean isHigh = input.get();
    if (isHigh) {
      // the voltage level is HIGH on the pin
    } else {
      // the voltage level is LOW on the pin
    }
  }
  ...
}
```

```java
public class Robot extends TimedRobot {
  
  private DigitalOutput output;

  @Override
  public void robotInit() {
    // this configures the port to output functionality
    // 1 refers to port number 1
    output = new DigitalOutput(1);
  }
  ...
  @Override
  public void teleopPeriodic() {
    // this set the output to LOW voltage level. "true" will be to set it to HIGH voltage level
    output.set(false);

    // this sends a pulse of length of 10^-5 seconds (10 microseconds). So the pin will be HIGH for 10 microseconds and then back to LOW
    output.pulse(1e-5);
  }
  ...
}
```

#### Example - Limit Switch

<img width="512" height="376" alt="Microswitch Wiring" src="https://github.com/user-attachments/assets/41337c7c-bcb1-4f60-bb45-d995a041d794" />

Consider, a simple microswitch connected to one of our DIO ports. A basic microswitch has two configurations:
- Normally Open: in normally open mode, when the switch is not pressed, the circuit is open. And due to the pull-up resistor, the voltage will be HIGH on the pin. When it is pressed, the circuit is closed with resistance and thus the voltage level becomes LOW.
- Normally Closed: in normally closed mode, when the switch is not pressed, the circuit is closed with resistance so the voltage is LOW. When it is pressed, the circuit is open and voltage becomes high.

In the image above, we see a normally-open configuration. The code below uses this switch as a limit to a motor.

```java
public class Robot extends TimedRobot {
  
  private DigitalInput input;
  private WPI_TalonSRX motor;

  @Override
  public void robotInit() {
    input = new DigitalInput(1);
    motor = new WPI_TalonSRX(0);
  }
  ...
  @Override
  public void teleopPeriodic() {
    boolean isHigh = input.get();
    if (isHigh) {
      // the voltage level is HIGH on the pin
      // so in normally-open, this means the switch isn't pressed
      // so run the motor
      motor.set(0.5);
    } else {
      // the voltage level is LOW on the pin
      // so in normally-open, this means the switch is pressed.
      // so stop the motor
      motor.set(0);
    }
  }
  ...
}
```

#### Example - Led Blinking

The following code blinks a led connected to DIO port 0 every 1 second. This is done by changing the output from LOW to HIGH and then from HIGH to LOW.

```java
public class Robot extends TimedRobot {
  
  private DigitalOutput output;
  private double startTimeSeconds;

  @Override
  public void robotInit() {
    output = new DigitalOutput(0);
  }
  ...
  @Override
  public void teleopInit() {
    // start with LOW output (led off)
    output.set(false);
    startTimeSeconds = Timer.getFPGATimestamp();
  }

  @Override
  public void teleopPeriodic() {
    double now = Timer.getFPGATimestamp();
    if (now - startTimeSeconds >= 1) {
      // 1 second has passed since last change of voltage

      // get the current state of the output and reverse it.
      // so if it was HIGH, we now switch to LOW or vice-versa.
      boolean isHigh = output.get();
      output.set(!isHigh);

      // store the time of the change
      startTimeSeconds = now;
    }
  }
  ...
}
```

#### Digital Input Interrupts

Normally processors execute one set of instructions, one instruction at at time. However, sometimes certain situations occur which the processor must handle immediatly, like certain errors or problems in execution, or devices which require special attention. This is what interrupts are for. They are basically special requests to the processor to stop the current code execution and execute a specialized code which will handle the unique situations. At any given moment we may request the processor to do this by sending it an interrupt signal. And so interrupts are the corner-stone for many functionalities handled by modern computers. Of course, interrupts have their limitations and should be used for everything as it will cause the computer to freeze. But when used wisely, they are quite important.

One possible use is to send an interrupt when a certain digital signal is received over a digital port. This will allow us to quickly handle this signal as soon as it arrives. If we didn't use interrupts, we would've had to wait for the processor to finish what's its doing. Consider: `teleopPeriodic` is executed every 20ms by the processor. Without interrupts we would have to wait up to 20ms to handle a digital signal, so when it comes to time-sensitive tasks, we have a problems. But interrupts solve this as we are now able to react to changes in the digital signals almost immediatly.

The processor can be configured for two types of interrupts from digital signals:
- RISING interrupts occur when the digital signals changes from LOW to HIGH
- FALLING interrupts occur when digital signals changes from HIGH to LOW

We won't be writing interrupt handlers ourselves for the RoboRIO, as this functionality is hidden from users. But we will be using code which depends on them.

#### Digital Input Counters

Counters are specialized code devices which use digital input interrupts. They are quite versitile and are the basis for interacting with many devices. They allow us to count and measure digital pulses over the digital pins of the RoboRIO. Because they use interrupt handlers they can react to short and quick changes. For example, they can measure digital pulses in microsecond accuracy.

Counters have multiple operation modes, each can be used to working with different devices
- Two pulse mode: counts edges on two input ports, +1 for edge from one port and -1 for edge from the second port.
- Semi period mode: used to measure length of a pulse on a single input port.
- Pulse length mode: used to count based on the length of the pulse received +1 or -1 depending on the length.
- External direction mode: basically two pulse mode + an external port which indicates which channel should be +1 and which should be -1.

We will look at examples for two of these modes.

A two pulse mode counter will count the edges (RISING/FALLING) on two digital ports. It has an up port, whose edges are counted by adding +1 to the count and a down port, whose edges are counted by adding -1 to the count. This mode is useful when working with quadrature-encoded signals, like relative encoders.
```java
public class Robot extends TimedRobot {
  
  private DigitalInput inputUp;
  private DigitalInput inputDown;
  private Counter counter;

  @Override
  public void robotInit() {
    // create the ports
    inputUp = new DigitalInput(0);
    inputDown = new DigitalInput(1);

    counter = new Counter(Counter.Mode.kTwoPulse);
    counter.setUpSource(inputUp);
    counter.setDownSource(inputDown);
    counter.setUpSourceEdge(/*should count on rising edge*/ true, /*should count on falling edge*/ false);
    counter.setDownSourceEdge(/*should count on rising edge*/ true, /*should count on falling edge*/ false);
  }
  ...
  @Override
  public void teleopPeriodic() {
    // gets the edge counts
    // basically, based on our configuration, the count actual to amount of rising edges on channel 0 - amount of rising edges on channel 1.
    int count = counter.get();
  }
  ...
}
```

A semi period mode counter will count both the rising and falling edges on a single input port, as well as measure the length of this pulse. It can be used to read pulse-width modulated singles.
```java
public class Robot extends TimedRobot {
  
  private DigitalInput input;
  private Counter counter;

  @Override
  public void robotInit() {
    // create the ports
    input = new DigitalInput(0);

    counter = new Counter(Counter.Mode.kSemiperiod);
    counter.setUpSource(input);
    counter.setSemiPeriodMode(true);
  }
  ...
  @Override
  public void teleopPeriodic() {
    // returns the length of the last received pulse in seconds.
    double lengthSeconds = counter.getPeriod();
  }
  ...
}
```

We'll soon see examples of how we can use this.

#### Example - HC-SR04 Ultrasonic

The _HC-SR04_ is a cheap and simple Ultrasonic sensor. Like every device, it has a [datasheet](https://cdn.sparkfun.com/datasheets/Sensors/Proximity/HCSR04.pdf) which describes how it works. 

Like all ultrasonics, it trasmits a sound wave, which then impacts the object infront of it and returns back to the sensor. That way, the sensor can measure the distance to the object by measuring the time it took the sound wave to return ($distance = {speed of sound \over time it took the wave to return}$).

<img width="512" height="309" alt="HC-SRO4 sending sound waves" src="https://github.com/user-attachments/assets/00a5e13f-da6d-4826-a6d4-b7afa49391c6" />

The sensor has 4 pins:
- VCC: connection for power supply for the sensor. It requires 5V voltage and 15 mA current. Can be provided from the VCC connection on the DIO port.
- GND: ground connection of the power supply. Can be provided from the GND connection on the DIO port.
- TRIG: trigger signal line. This line is used to tell the sensor when to measure the distance. Requires a signal pin on a DIO port.
- ECHO: echo signal line. This line is used to tell us about the measurement of distance. Requires a signal pin on a DIO port.

Because the VCC and GND pins are all connected, all we need is to connect to one of those. Despite us using two different signal pins.

<img width="490" height="512" alt="HC-SR04 Pinout" src="https://github.com/user-attachments/assets/da380f17-a200-4bba-a13d-0f2a0cc46015" />

To take a measurement, the sensor expects to receive a 10us (10 microsecond) pulse via the TRIG pin. This tells it to send the sound wave.

Once measurement is started, the echo pin is changed to HIGH. This is left HIGH until the sound wave returns to the sensor. When it does, the pin is changed back to LOW. This effectively creates a pulse which starts when the sound wave is sent and ends when the sound wave returns. So its length will be equal to the time it took the sound wave to go from the sensor to the object and then back. So to calculate the distance, we need to measure the length of the pulse and then calculate the distance via the following formula: $distance \ meters = {speed \ of \ sound * pulse \ length \ seconds \over 2} = {340 \ meters \ per \ second \ * pulse \ length \ seconds \over 2}$. To measure the pulse length with can use a counter in semi period mode.

The gif below is what's called the timing diagram. It shows the timing of voltages on the trigger and echo lines.

![HC-SR04 timing](resources/hcsr04-timing.gif)

The following code interfaces with this sensor:
```java

  private DigitalOutput trigPort;
  private DigitalInput echoPort;
  private Counter counter;

  private boolean isPulseSent;

  @Override
  public void robotInit() {
    trigPort = new DigitalOutput(0);
    echoPort = new DigitalInput(1);

    counter = new Counter(Counter.Mode.kSemiperiod);
    counter.setUpSource(echoPort);
    counter.setSemiPeriodMode(true);

    isPulseSent = false;
  }
  ...
  @Override
  public void teleopPeriodic() {
    if (isPulseSent) {
      // send a pulse to start measurement. reset the counter.
      isPulseSent = true;
      counter.reset();
      trigPort.pulse(1e-5);
    } else {
      // pulse was sent, wait for it to return
      if (counter.get() >= 2) {
        // if the counter has counted two edges, than we know it counted both the rising edge and falling edge of the pulse.
        // that means that a full pulse has been received, so we can read its length 
        double lengthSeconds = counter.getPeriod();
        double distanceM = 340.0 * lengthSeconds / 2;

        // prepare for another ping
        isPulseSent = false;
      }
    }
  }
  ...
}
```

### Analog I/O

The Analog Input ports are a row of 4 seperate ports in the side of the RoboRIO. They are used for reading analog signals. Each of these ports is numbered (0-3), and this number will be used in our code to access the specific port. This ports are all just for analog input. There are analog output pins on the MXP, but we will not discuss them.

<img width="800" height="785" alt="Analog Input Row" src="https://github.com/user-attachments/assets/8d5e11ec-381a-4d2d-8b24-794e72690a8d" />

Like with the DIO ports, each port in the row is made up of 3 seperate pins: 
- The signal pin (top) for the analog signal 
- The VCC pin (second) for 5V power connection
- The GND pin (bottom) for Ground connection

<img width="69" height="78" alt="Port pins" src="https://github.com/user-attachments/assets/0eb69bdb-077e-4f58-8c9b-8f0349f8c715" />

Each of the pins is connected to and ADC (Analog to Digital Converter) with a 12 bit resolution. The voltage range is 0V to 5V so this maps to a range of 0 to 4095 values from the ADC. So each 1 value means a change of 1.2 mV, so 1 = 1.2 mV, 2 = 2.4 mV and so on. Though the effective resolution of the port is 50 mV, meaning we will only see a change of 50 mV. So after 0, the next value we can expect is 42. This effects our ability to measure data from sensors. Calculating the voltage from the ADC is a simple matter: $voltage = {value \over 4096} * 5$.

The ADC has a 500 KS/s sampling rate. That is 500,000 samples per second. That is 2 samples per 1us, so if the voltage changes, it will take 500ns to see that change.

Accessing the data is as straight-forward as digital input is:
```java
public class Robot extends TimedRobot {
  
  private AnalogInput input;

  @Override
  public void robotInit() {
    // 1 refers to port number 1
    input = new AnalogInput(1);
  }
  ...
  @Override
  public void teleopPeriodic() {
    int value = input.getValue();
    // or we can use getVoltage which converts the value to voltage
    double volts = input.getVoltage();
  }
  ...
}
```

#### Example - Analog Acceleremoter

The ADXL193 is a single-axis accelerometer which uses an analog output to report on its measurements. [Datasheet](https://www.analog.com/media/en/technical-documentation/data-sheets/ADXL193.pdf). It is intended for detection of collision, which involves high acceleration values, with a range of up to 250G acceleration. Because the acceleration is measured on an axis, the acceleration can either be negative or positive, depending on the direction of the acceleration.

It requires 2-5V and up to 2mA power supply, so it can be powered by the port's VCC and GND pins.

<img width="767" height="614" alt="ADXL193 wiring" src="https://github.com/user-attachments/assets/7c5f9e88-ff25-4416-8c7a-f525d65ed5a5" />

Its output for 0 acceleration is 2.5V on the analog pin. This is the zero point to diffrentiate between negative or positive acceleration. That leaves a range of 2.5V for positive and negative acceleration. Each G of acceleration is a change of 8mV on the line. So for +1G acceleration, the voltage on the line will be 2.508V. However, we do have a problem, because the accuracy of our ports is 50mV, meaning we can only see a change of 6.25G from the input. This is a problem between the sensitivity of the sensor and the capabilities of the device. But it is also somewhat a non-issue, because the sensor is meant for high-acceleration detection, which means that its use is for high changes in acceleration, which we will be able to detect. That is why it is so sensitive. But we need to understand this when working with the sensor so we don't use it in ways its not meant to be used.

```java
public class Robot extends TimedRobot {
  
  private AnalogInput input;

  @Override
  public void robotInit() {
    input = new AnalogInput(0);
  }
  ...
  @Override
  public void teleopPeriodic() {
    double volts = input.getVoltage();
    double accelerationG = (volts - 2.5) / 0.008;
  }
  ...
}
```

### PWM Output

The Pulse-Width Modulation (PWM) output ports are a row of 10 seperate ports in the side of the RoboRIO (opposite to the DIO ports). They are used for generating PWM signals. Each of these ports is numbered (0-9), and this number will be used in our code to access the specific port. Note that these ports are only for output. For input of PWM, simply use a normal Digital input port.

Like the DIO and Analog Input ports, these ports are composed of 3 pin for Signal, VCC and GND.

All the PWM pins are managed by a dedicated clock which is used to properly manage the pulse widths. This allows us to output PWM signals in differing lengths with microsecond precision. The standard period for the clock is 5.05 ms (so the PWM frequency is 198.019 Hz). It is also possible to configure the clock for a 2 times period (10.1ms) or 4 times period (20.2ms).

```java
public class Robot extends TimedRobot {
  
  private PWM port;

  @Override
  public void robotInit() {
    port = new PWM(0);
    port.setPeriodMultiplier(PWM.PeriodMultiplier.k1X); // for 5.05ms period
  }
  ...
  @Override
  public void teleopPeriodic() {
    // this sets the PWM to a duty cycle of 50% (5.05*0.5 = 2.525ms = 2525us)
    port.setPulseTimeMicroseconds(2525);
  }
  ...
}
```
#### PWM Pulse Bounds

For controlling PWM motor controllers, we need to define a set of bounds which define the meaning of pulse widths compared to their effect on the motor.
FRC PWM motor controllers generally operate by spliting the period into three discrete parts: center = no motion on the motor, max = maximum forward motion (clockwise at 12V) (max because it is the longest pulse width), min = maximum negative motion (counter-clockwise at 12V) (min because it is the shortest pulse width).  Depending on the motor controller, these values will differ, but are limited by the period of our PWM clock.

To calculate the pulse width for a wanted PercentVBus value we can use the following pseudu code:
```
if output > 0:
  pulse width = center + |output| * (max - center)
if output < 0:
  pulse width = center - |output| * (center - min)
else:
  pulse width = center
```

PWM motor controllers also define a _minimum throttle_. This value indicates the minimum need pulse width change from center to actually have enough power to make the motor move. Values below this will not make the motor spin at all. We may take this value into consideration when performing our calculation. But if we don't, it means that a low enough output will not move the motor at all. This property is also called _deadband_.

#### Example - VictorSP

The VictorSP is a motor controller from CTRE with a PWM input. [Manual](https://store.ctr-electronics.com/content/user-manual/Victor-SP-Quick-Start-Guide.pdf).

<img width="950" height="735" alt="VictorSP" src="https://github.com/user-attachments/assets/108d0e5b-3b12-4c32-8f7f-2b72e2969b57" />

The following table describe the properties of the PWM input (taken directly from the manual):

<img width="559" height="396" alt="VictorSP PWM" src="https://github.com/user-attachments/assets/e4bcb11b-3cf4-4303-953d-8b9a1bc9ae0e" />

From this, we can learn a few thing:
- The motor expect 1 - 2 ms pulse width to control the motor. This can be translated to 1.5ms center, 1ms min and 2ms max.
- The PWM period must be between 2.9-100ms. Our period is 5.05ms so it is fine. The controller will automatically work with any period in this range.
- The minimum throttle is 4%, so an absolute output less than 4% will yield no motion.

The following code is a simple control of the VictorSP:
```java
public class Robot extends TimedRobot {
  
  private PWM port;

  @Override
  public void robotInit() {
    port = new PWM(0);
    port.setPeriodMultiplier(PWM.PeriodMultiplier.k1X); // for 5.05ms period

    SmartDashboard.putNumber("output", 0);
  }
  ...
  @Override
  public void teleopPeriodic() {
    final int CENTER = 1500;
    final int MAX = 2000;
    final int MIN = 1000;

    double output = SmartDashboard.getNumber("output", 0);
    output = MathUtil.clamp(output, 1, -1);

    int pulseWidth = 0;
    if (output > 0) {
      int min = CENTER + 1;
      int max = MAX;
      pulseWidth = (int) (min + output * (max - min));
    } else if (output < 0) {
      int min = MIN;
      int max = CENTER + 1;
      pulseWidth = (int) (max - Math.abs(output) * (max - min));
    }

    port.setPulseTimeMicroseconds(pulseWidth);
  }
  ...
}
```

This logic is actually already implemented for us, with the use of `port.setBoundsMicroseconds` and `port.setSpeed`:
```java
public class Robot extends TimedRobot {
  
  private PWM port;

  @Override
  public void robotInit() {
    port = new PWM(0);
    port.setBoundsMicroseconds(2000, /*max deadband*/1520, 1500, /*min deadband*/1480, 1000);
    port.setPeriodMultiplier(PWM.PeriodMultiplier.k1X); // for 5.05ms period

    SmartDashboard.putNumber("output", 0);
  }
  ...
  @Override
  public void teleopPeriodic() {
    double output = SmartDashboard.getNumber("output", 0);
    output = MathUtil.clamp(output, 1, -1);
    port.setSpeed(output);
  }
  ...
}
```

### I2C

Unlike the previous ports we've discussed, the I2C port is made up of 2 signal connections (one for SDA and one for SCL). It is essential to connect them properly, connecting the wires to the wrong pins will not work.

<img width="512" height="502" alt="I2C port" src="https://github.com/user-attachments/assets/412eda31-8872-4f71-b277-166d03e54b8e" />

There is actually another set of I2C pins, in the MXP header.  

> [!WARNING]
> The main I2C port on the RoboRIO 1 has a software problem which can lead to system lockup. Avoid using. Using the MXP pins is fine.

Because of the complexity of the protocol, it is actually managed by the hardware. All we need to do with it is simply write and read data. But we don't read/write directly to the wires, but rather we work with a set of buffers which are connected to the hardware. When we wish to write, we write into the write-buffer which is then written to the line by the hardware. Same thing for reading: when new data arrives it is stored in the read-buffer and then we can read from that buffer.

```java
public class Robot extends TimedRobot {
  
  private I2C port;

  @Override
  public void robotInit() {
    // open a handle to the I2C hardware which allows read/write with device address=0x22
    port = new I2C(I2C.Port.kOnboard, 0x22);
  }
  ...
  @Override
  public void teleopPeriodic() {
    // read two adjacent registers: 0x20, 0x21
    // this sends a request to read the registers and receives the data about them.
    int startRegister = 0x20;
    byte[] buffer = new byte[2];
    port.read(startRegister, 2, buffer);

    // write to register 0x24 with the value 11
    port.write(0x24, 11);
  }
  ...
}
```

#### Example - ADXL345

The ADXL345 is a 3-axis accelerometer which uses I2C to communicate. [Datasheet](https://www.analog.com/media/en/technical-documentation/data-sheets/adxl345.pdf).
Connecting it is straight forward: just connect the SDA and SCL from the device to the RoboRio. However, if there are multiple devices, the lines have to be connected to all of them, so it will have to be splited.

Power also must be provided, it requires 2V - 3.6V and 140uA. This can be provided from the VCC and GND from one of the other ports, however, it must be done with a resistor since they provide 5V.

<img width="225" height="225" alt="ADXL345 pinout" src="https://github.com/user-attachments/assets/1add15ba-62ac-4644-8737-44bda7a505a9" />

The device has an address of `0x1D` with the following register map:

<img width="766" height="776" alt="ADXL345 register map" src="https://github.com/user-attachments/assets/2092ed05-7152-42b0-9c52-ba8279e0b301" />

We'll demonstrate reading the X-axis acceleration value. You can see it is made up of 2 registers `DATAX0` (0x32) and `DATAX1` (0x33). `DATAX0` contains the least significant byte, while `DATAX1` contains the most significat byte. Together, these registers compose a 2-byte value. Each 256 values represent a full 1 G acceleration. So 256 = 1G, 512 = 2G. This is true for the 2G mode (meaning a max of 2G acceleration is measured). 

<img width="423" height="309" alt="ADXL345 axis registers description" src="https://github.com/user-attachments/assets/864cd870-50d8-4656-ac44-b2f55b42f281" />

<img width="847" height="74" alt="ADXL345 sensitivity" src="https://github.com/user-attachments/assets/7d28f17b-ef4b-43de-807c-322bb7154bf5" />


We'll have to actively request to read those registers to receive an update on their value.

```java
public class Robot extends TimedRobot {
  
  private I2C port;

  @Override
  public void robotInit() {
    port = new I2C(I2C.Port.kOnboard, 0x1D);
  }
  ...
  @Override
  public void teleopPeriodic() {
    byte[] buffer = new byte[2];
    port.read(0x32, 2, buffer);

    // java uses Big Endian, with the most significat byte stored lower in memory and the least stored in upper memory.
    // so we shift the byte 8 bits to place it in the upper 8 bits of memory.
    int lsb = (buffer[0] << 8) | buffer[1];
    double accelerationG = lsb / 256.0;
  }
  ...
}
```

### SPI

Just as with I2C, the SPI port is made up of several wires implementing the communication of the SPI bus. Theres the MISO, MOSI, SCLK and 4 CS pins. There's also 2 VCC (of different voltages) and 1 GND. So this also of up to 4 devices in the SPI bus.

<img width="512" height="502" alt="SPI port" src="https://github.com/user-attachments/assets/75bf0f33-3991-40d5-aa33-7e995b1c5328" />

Being a bus, this is a complex protocol and already implemented in hardware and low-level software. All we do is interact with the read and write buffers to request and receive data from the devices. The port can also be configured to automatically read and write data in certain situations (called auto mode), like when receiving an interrupt. This opens a lot of possibilities, but we will not be examining this feature here.

```java
public class Robot extends TimedRobot {
  
  private SPI port;

  @Override
  public void robotInit() {
    // open a handle to the SPI hardware which allows read/write to the device connected to CS number 0.
    port = new SPI(SPI.Port.kOnboardCS0);

    // configure SPI operating mode
    // clock rate of 2000000 Hz
    // Mode3 = Clock idle = HIGH, data sampled on the rising edge of the clock
    // CS idle = HIGH
    port.setClockRate(2000000);
    port.setMode(SPI.Mode.kMode3);
    port.setChipSelectActiveLow();
  }
  ...
  @Override
  public void teleopPeriodic() {
    // adds data to the write buffer which will be written on the line
    // because SPI is more free-hand than I2C, what is written or read is dependent on the device we are communicating with
    byte[] bufferToWrite = "Hello World".getBytes(StandardCharsets.UTF_8);
    port.write(bufferToWrite, bufferToWrite.length);

    // reads the first 10 bytes in the read buffer
    byte[] bufferToRead = new byte[10];
    port.read(false, bufferToRead, bufferToRead.length);
  }  
  ...
}
```

## Exercises

In these exercise we will be trying to implement code to interface with different devices. These are open-ended exercises, you'll have to read the datasheet of each device and extract the necessary information to provide a theoratically function code which can read data from the sensor and display on the shuffleboard. Each device will be accompanied by what kind of code is expected and additional questions about the device whose answers are present in the device datasheet. These are not easy exercises, but they are well worth it. Read the datasheets carefully.

### Exercise 1 - MB1122

The MB1122 ultrasonic is one of a series of ultrasonics from MaxBotix. [Datasheet](https://maxbotix.com/pages/hrlv-maxsonar-ez-datasheet)

<img width="512" height="512" alt="MB1122" src="https://github.com/user-attachments/assets/883c9658-ab4f-49dd-b0db-128a9655c19d" />

Exercise - read the datasheet and answer the following questions:
- What is the minimum and maximum ranges of the device?
- What is the size of the smallest object it can detect?
- What data interfaces does it have for providing its output?
- Select one data interface and:
  - If the sensor is infornt of an object at a distance closer than the minimum range, what output will we get?
  - If the sensor is infornt of an object at a distance further than the maximum range, what output will we get? 
  - illustrate to which ports on the roborio would the device be connected. Specify exactly which pins are connected to what.
  - implement a basic code which periodically reads the distance measured by the sensor and display it on the shuffleboard.
 
 ### Exercise 2 - ADIS16470

 The ADIS16470 is an IMU (Inertial Measurement Unit) with a variety of motion sensors onboard. [Datasheet](https://www.analog.com/media/en/technical-documentation/data-sheets/ADIS16470.pdf).

<img width="640" height="469" alt="ADIS16470" src="https://github.com/user-attachments/assets/79c0dc59-dc0a-4fc5-81bd-bcfea55565f0" />

 Exercise - read the datasheet and answer the following questions:
- What types of sensors does the board contain?
- What data interface does it use to communicate?
- How can the device be placed in sleep mode? What effects does it have on the operation of the sensor?
- Read the chapter on interrupts in the datasheet. What interrupts does it have? How can we make use of them?
- Illustrate to which ports on the roborio would the device be connected. Specify exactly which pins are connected to what.
- Implement a basic code which periodically reads the at least 3 values from the sensor and displays it on the shuffleboard. You are free to select which.

