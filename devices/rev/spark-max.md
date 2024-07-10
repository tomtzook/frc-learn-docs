## SparkMax

![sparkmax](https://github.com/tomtzook/frc-learn-docs/assets/17641355/3a701c1e-f9bd-4061-8dc2-898305d7dccc)

The _SparkMax_ is a _smart_ CAN-connected motor controller. By saying smart, this refers to it being capable of more then simple _PercentVBus_ control (which was the most old motor controllers like Talon were capable of). It also alludes to it having actual software running on it, which makes it capable of much more then a simple electronic circuit. We should think of it as a small micro-controller, capable of complex functionality. Being connected to the robot's CAN-Bus allows it to receive complex instructions from the robot code.

SparkMax is a product of _REV_ and is thus compatible and intended to work with _REV_'s products, mainly the _REV Hardware Client_ and _NEO_ series motors.

### REV Hardware Client

The [_REV Hardware Client_](https://docs.revrobotics.com/rev-hardware-client) allows us to communicate and control the _SparkMax_.

To use it, connect a USB-C cable from the controlling laptop to the SparkMAX (or any other REV devices connected to the same CAN-Bus). Once connected, you will be able to select a specific device and configure or operate it.

> [!NOTE]
> To operate the SparkMax (run the motor), the RoboRIO must not be active on the CANBus. Either disconnect it from Power, disconnect it from the CANBus, or make sure the robot code is not running.

### Firmware

The firmware is the software (code) running on the SparkMax. It is booted automatically (and quickly) when it receives power.

This firmware can and should be regularly updated, as _REV_ releases new versions with fixes and improvements. Use the _REV Hardware Client_'s update functionality to do so.

![client update tab](https://github.com/tomtzook/frc-learn-docs/assets/17641355/12a45b0b-6faf-420c-b084-254aced5b473)

> [!NOTE]
> For each season, REV typically releases new versions of the firmware. Make sure to check and update at the start and during the season.

### Supported Motors

The SparkMAX was made with the intention to use it with _REV_'s _NEO_ motors (_NEO 1.1_ and _NEO 550_). These are Brushless DC motors with an integrated Relative encoder. So typically, we would use it in conjuction with those motors. 

However, the SparkMAX _can_ be used with other motors. Specifically, it can be used with any Brushed motors (e.g. CIM or Bag motors). When used with such motors, SparkMAX must be configured to work with Brushed motors and will not have an integrated encoder since these motors do not have one.

> [!WARNING]
> SparkMax should not be used with non-REV brushless motors. These motors should be operated by the motor controllers designed to operate them.

> [!WARNING]
> When connecting the SparkMAX to a motor, make sure to configure it correctly. If it is configured wrongly (e.g configured for brushed mode while connected to brushless) the connected motor may be damaged. This is because brushed and brushless motors are operated in different ways.

### Configuration

There are several basic global configurations for the SparkMAX. These configurations apply to all and any operations of the motor controller. There are also more specific and in-depth configurations for certain features of the SparkMAX. All of these features may be configured via either code or via the _REV Hardware Client_.

The basic tab in the _REV Hardware Client_ features a select number of configurations.

![SparkMAX Basic Tab](https://github.com/tomtzook/frc-learn-docs/assets/17641355/11fb9e72-d5df-477c-b2fa-247f58adcd9c)

The advanced tab in the _REV Hardware Client_ features a categorized list of all the configuration values.

![SparkMAX Advanced Tab](https://github.com/tomtzook/frc-learn-docs/assets/17641355/59a187d3-ddce-478c-b90a-11ca4f081a7a)

The SparkMAX configuration can be volatile or persistent. Any changes to the configuration are normally volatile (via code of the client). Power cycling the device will discard these changes. To persist configuration changes, use the _Burn Flash_ feature (via code or the client). 

The configuration may be returned to factory default (default settings). This is a volatile change. Using _Burn Flash_ will persist these changes. Use factory default to reset changes to the configurations. This is highly recommended for robot code to use, especially for competitions, as someone may change the configurations in an unwanted manner, affecting the device's functionality. 

As a standard operating procedures, we won't be burning configuration (burn flash). Instead we will be reseting to factory default in code as a first step, and the configure what we want and need in the code.

#### Basic Configuration

The following are basic configurations available for the SparkMAX.

`ID` is the CANBus identifier of the device. It is used to distinctly identify the device and communicate with it. This value should be unique per device. This value is a non-negative integer.

`MotorType` indicates the type of motor to control. It can be either
- `NEO Brushless` for NEO-series motors
- `Brushed` for any brushed motor

`IdleMode` describes the behaviour of the motor when entering idle motor. Idle motor refers to the motor output being at 0. Basically, when the motor is stopped. 
- `Coast` the standard operating mode. In this mode, when entering idle mode, the motor simply stops receiving power and will do nothing. If the motor has momentum, it will continue rotating freely until such momentum is lost.
- `Brake` In this mode, when entering idle mode, the SparkMax shorts out the motor's power connections, resulting in the motor forcibly refusing to allow the stator to rotate. In affect, the motor will resist any attempt to rotate the shaft, and will immediatly negate any momentum the shaft had. This mode can be used to keep the shaft in place. Brake mode works in disabled mode due to the motor not actually requiring any outside current.

> [!WARNING]
> Brake mode can seriously increase the temperature of the motor due to how power is generated. Be careful when using it.

`Inverted` determines if the motor output should be inverted (i.e. the motor will turn to the opposite direction, plus direction will act as minus direction and minus as plus).

#### Code Initialization and Basic Configuration

We've already initialized and used the SparkMax in the past, it is a simple matter of creating new instance of `CANSparkMax`:
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      // or CANSparkLowLevel.MotorType.kBrushless for brushed motor
  }
}
```
Note that you should pass `CANSparkLowLevel.MotorType` to the constructor instead of `CANSparkMaxLowLevel.MotorType` which was deprecated. 

For here we can configure and operate the motor controller as we like. As mentioned before, we should start by resetting the settings to factory default:
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      motor.restoreFactoryDefaults();
  }
}
```

Only after restoring the settings should we start modifying the settings as wanted. Most settings should be set in the constructor when initializing the motor, so that the settings affect our operation of the motor throughout the robot code.
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      motor.restoreFactoryDefaults();

      motor.setInverted(false); // or true to set as inverted
      motor.setIdleMode(CANSparkBase.IdleMode.kCoast); // kBrake for brake mode 
  }
}
```

### Built-In Sensors

SparkMax comes with a set of built-in sensors and information exposed to us (the users). We can use this information to track and check that the motor is operated as we want. 

The output current sensor tracks the current passed to the motor. We can use this to monitor the current consumption of the motor. This value may be used to detect a faulty motor, improper power supply, and more. Use `motor.getOuputCurrent` to read this value in ampere.

The input voltage tracks the voltage provided to the motor controller from the power supply. Use `motor.getBusVoltage` to access this value in volts.

The applied output tracks the output of the motor controller. By multiplying this value with the input voltage we can get the output voltage. Use `motor.getAppliedOutput` to get this value in `-1` to `1`.

The temperature of the motor can be retreived with `motor.getMotorTemperature`, which returns the temperature in celcius. Unlike the previous sensors, this information is dependent on a motor sensor, which doesn't exist in the motor controller. As such, we can only expect to receive this information from NEO series motors which have such sensor and can connect it to the SparkMAX.

> [!NOTE]
> All these sensors can be easily viewed in the REV Hardware Client's Telemetry tab.

### Faults

### Feedback Devices

SparkMAX supports the connection of multiple different _feedback devices_. Such devices (usually encoders) allow running motor control algorithms on the SparkMAX utilizing the sensor information. There values can also be read from the SparkMAX over the CANBus. 

SparkMAX has two ports: the encoder port and the data port.

The encoder port allows connecting the integrated encoder from NEO-series motors, as well as the integrated temperature sensor. This is the only intented function for this port. The port accepts the connections for a quadrature relative encoder. 

![encoder port](https://github.com/tomtzook/frc-learn-docs/assets/17641355/4e8f919a-3ec8-4fbe-8fae-68b67042e67f)

![encoder port pinout](https://github.com/tomtzook/frc-learn-docs/assets/17641355/c4267793-1fe3-4d12-8c2b-f3c4b0391d57)

The following code example uses this encoder:
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final RelativeEncoder encoder;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      encoder = motor.getEncoder(); // access the integrated encoder interface

      double position = encoder.getPosition(); // access the position measurement from the encoder. returns data in revolutions measured by the encoder.
      double velocity = encoder.getVelocity(); // access the velocity measurement from the encoder. returns data in RPM as measured by the encoder.
  }
}
```

The data port is the second set of connection pins. It should be used by any other sensors we may connect to SparkMAX. 

![data port](https://github.com/tomtzook/frc-learn-docs/assets/17641355/3aca0cbf-c2eb-4ce4-a32c-033708fa1693)

![data port pinout](https://github.com/tomtzook/frc-learn-docs/assets/17641355/6a796eae-dd7d-4c08-a4f8-55d77f4651ea)

When working in _brushed mode_, we can connect an external encoder to this port into (with pins 5, 6 and 9). This encoder acts as a replacement to the integrated encoder of brushless motors, and should be used instead of the encoder port. We can access it with the `RelativeEncoder` interface (like in the previous example):
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final RelativeEncoder encoder;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      final int CPR = 4096;
      encoder = motor.getEncoder(SparkRelativeEncoder.Type.kQuadrature, CPR);

      double position = encoder.getPosition(); // access the position measurement from the encoder. returns data in revolutions measured by the encoder.
      double velocity = encoder.getVelocity(); // access the velocity measurement from the encoder. returns data in RPM as measured by the encoder.
  }
}
```

When working in brushless mode, we may connect an additional encoder (outside the integrated motor encoder connected to the encoder port). This is called _alternate encoder mode_, and uses pins 4, 6 and 8 (the normal encoder ports are occupied for the integrated encoder, shared between the encoder and data ports). Again, we use the `RelativeEncoder` interface, just acquire it differently:
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final RelativeEncoder encoder;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      final int CPR = 4096;
      encoder = motor.getAlternateEncoder(SparkMaxAlternateEncoder.Type.kQuadrature, CPR);

      double position = encoder.getPosition(); // access the position measurement from the encoder. returns data in revolutions measured by the encoder.
      double velocity = encoder.getVelocity(); // access the velocity measurement from the encoder. returns data in RPM as measured by the encoder.
  }
}
```

The data port also supports connections for absolute encoders using the _multi function pin_ which allows connecting pulse-width based absolute encoders (like the through-bore encoder). This can be used in both brushed or brushless modes, but cannot be used in conjunction with _alternate encoder mode_. We use the `AbsoluteEncoder` interface to access absolute encoder data:
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final AbsoluteEncoder encoder;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      encoder = motor.getAbsoluteEncoder(SparkAbsoluteEncoder.Type.kDutyCycle);

      double position = encoder.getPosition(); // access the position measurement from the encoder. returns data in revolutions measured by the encoder. For absolute encoders, this value will be between 0 and 1 reflecting 0 to 360.
  }
}
```

The data port has a single port for analog input sensors. This can be absolute or relative encoders, the only difference is that they work with an analog interface instead of the digital interfaces of pulse-width and quadrature encoders. We would rarely use these, because they have limitations, but they can be accessed via `motor.getAnalog` and work for both brushed and brushless modes.

### Limits

SparkMAX supports both hardware and software limits.

Hardware limits are based on limit switches. The data port supports connecting two limit switches via Digital input ports (pins 4 and 8). 

Each switch is responsible for a limit for a specific direction. The forward limit switch limits the forward motion of the motor (clockwise, or counter clockwise for inverted). Once the switch is closed, forward motion will no longer be possible, but reverse motion will. The reverse limit switch does something similar, just affecting the reverse motion direction. This allows limiting the system motions based on two motion directions, which is a common situations for limited systems.

Each of the swiches can be configured to operate in either _normally open_ or _normally closed_ modes. The selected mode should match the type of connections the actual sensor uses. All of this can be configured via code (or the REV Hardware Client). Though, because these limits are mostly automatic, they do not need the intervention of our code to function:
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final SparkLimitSwitch fwdSwitch;
  private final SparkLimitSwitch rvsdSwitch;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      fwdSwitch = motor.getForwardLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
      fwdSwitch.enableLimitSwitch(true);
      rvsdSwitch = motor.getReverseLimitSwitch(SparkLimitSwitch.Type.kNormallyOpen);
      rvsdSwitch.enableLimitSwitch(true);
  }
}
```

This truth table describes the expected behviour of the limits when using different polarities.

![switch polarity table](https://github.com/tomtzook/frc-learn-docs/assets/17641355/7adeba64-659e-442a-9e28-d43e774d6b75)

> [!WARNING]
> Hardware limit switches are not supported when using alternate encoder mode

Software limit switches use the NEO-series integrated encoder to provide motion limits based on sensor position. They must be configured and require no additional hardware. But are very limited to what the encoders are capable of sensing and reporting. The limit configured must match the measurement units used by the encoder. 

Just like hardware limits, software limits support limiting based on forward or reverse motion direction.
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      motor.setSoftLimit(CANSparkBase.SoftLimitDirection.kForward, 40); // stop forward motion when encoder.getPosition reports 40 and above
      motor.enableSoftLimit(CANSparkBase.SoftLimitDirection.kForward, true);
      motor.setSoftLimit(CANSparkBase.SoftLimitDirection.kReverse, 0); // stop reverse motion when encoder.getPosition reports 0 and below
      motor.enableSoftLimit(CANSparkBase.SoftLimitDirection.kReverse, true);
  }
}
```

> [!NOTE]
> It is not clear whether software limits can function with feedback devices other than the integrated NEO encoders

### Control Modes

Unlike basic PWM motor controllers, SparkMax is capable of running several control modes to operate the motor. We are familiar with the _DutyCycle_ mode, which accepts a _PercentVBus_ value. This is the base control mode, but there are more:
- _Voltage_ mode accepts a voltage number instead of _PercentVBus_.
- _Current_ mode accepts wanted current for the motor, uses a PID loop.
- _Position_ mode accepts wanted position according to a feedback device, uses a PID loop.
- _Velocity_ mode accepts wanted velocity according to a feedback device, uses a PID loop.
- _Follower_ mode follows the output of a different SparkMax.
- _Smart Motion_ mode accepts wanted position according to a feedback device, uses a PID loop with motion profiling.
- _Smart Velocity_ mode is an unknown at the moment.

All of these control modes basically produce a _PercentVBus_ output for the motor, but they calculate this output from different factors. _DutyCycle_ and _Voltage_ are the most basic as they operate the motor directly without much additional work. The rest depend on a PID loop running on the SparkMax.

#### PIDF

SparkMAX implements and is capable of running a PIDF loop. This is similar to the PID loops we are familiar with, with some differences.

First, the loop runs on the SparkMAX and not the RoboRIO. This frees up our code to do other things, and actually simplifies writing code for the robot. The loop runs at a period of 1ms (unlike 20ms on the RoboRIO). This means it is significantly more smooth and responsive then anything on the RoboRIO.

Second, it is a PIDF loop, not PID. This means it has an additional component: F (feed-forward). This is augments the normal PID equation with

$$output = k_f * SetPoint$$

So we also get this component to control our system. Which can be useful in some circumstances, like controlling the velocity of a flywheel.

When using PIDF control modes, we provide the SparkMAX with our wanted setpoint and the SparkMAX calculates the output by on provided gains and using process variable from a configured feedback sensor (which is connected to the SparkMAX). So we are limited to using sensors which are connected to the SparkMAX, usually encoders.

The overall output is a _PercentVBus_ output which is then used to operate the motor.

The following code configures the PIDF loop and runs it for position mode using the NEO integrated encoder.
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final RelativeEncoder encoder;
  private final SparkPIDController pidController;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      encoder = motor.getEncoder();
      pidController = motor.getPIDController();

      pidController.setP(0.01);
      pidController.setI(0.0001);
      pidController.setD(0);
      pidController.setFF(0);
      pidController.setIZone(2);
      pidController.setIMaxAccum(5); // max I accumulation ceiling
      pidController.setDFilter(0); // filter for D component output
      pidController.setOutputRange(-1, 1); // maximum output range
      pidController.setFeedbackDevice(encoder); // configure feedback device to use
  }

  public void moveToSetpoint(double setPoint) {
      // configure the wanted setpoint to start the PID loop
      pidController.setReference(setPoint, CANSparkBase.ControlType.kPosition);
  }

  public boolean didReachSetPoint(double setPoint) {
      double position = encoder.getPosition();
      double velocity = encoder.getVelocity();

      return MathUtil.isNear(setPoint, position, 1) && Math.abs(velocity) < 5;
  }

  public void stop() {
      motor.stopMotor();
  }
}

public class MoveSystemToPosition extends Command {

    private final SomeSystem system;
    private final double setPoint;

    public MoveSystemToPosition(SomeSystem system, double setPoint) {
        this.system = system;
        this.setPoint = setPoint;

        addRequirements(system);
    }

    @Override
    public void initialize() {
        system.moveToSetpoint(setPoint);
    }

    @Override
    public void execute() {}

    @Override
    public boolean isFinished() {
        return system.didReachSetPoint(setPoint);
    }

    @Override
    public boolean end(boolean interrupted) {
        system.stop();
    }
} 
```

After calling `setReference` once, the PID loop starts, no need to call it again unless intending to change the setpoint, control mode or other configurations provided through `setReference`. So when using it in a command, calling it in `initialize` is generally enough. The loop will stop when `stopMotor` or another operation command to the motor is done (`set`, another `setReference` and so on).

You'll notice that the PID controller has several supported settings:
- P: the $k_p$ gain
- I: the $k_i$ gain
- D: the $k_d$ gain
- FF: the $k_f$ gain
- IZone: we are familiar with IZone already
- IMaxAccum: the maximum absolute value for the I accumulator variable.
- DFilter: the maximum absolute value for the D output
- OutputRange: maximum range of output values
- FeedbackDevice: which feedback device to use for ProcessVariable. Should be a `RelativeEncoder` or `AbsoluteEncoder` instance.

> [!NOTE]
> The setpoint value should match the measurement units of the feedback sensor. For position mode this is typically measured in motor revolutions.

Notice `isFinished` and `didReachSetPoint`. When using `PIDController` we used `atSetpoint` to check if we reached the wanted setpoint. SparkMAX doesn't really provide something for it, so intead we implement a logic to do this check
manually. We check for 2 things based on the same sensor as used by the PID: position and velocity. This way we know that we've reached the wanted position and stopped at this position.

Each SparkMax has actually 4 (0 -> 3) PIDF profiles (or slots). Each slot is an independent set of the configuration values we saw above. And when calling `setReference` we can choose which slot to use for the PID loop. This allows us to configure several different gains (each for a specific set of operations) and switch between the seemlessly. Note that the feedback device is configured for all the slots.

```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final RelativeEncoder encoder;
  private final SparkPIDController pidController;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      encoder = motor.getEncoder();
      pidController = motor.getPIDController();

      pidController.setFeedbackDevice(encoder);

      // configure slot 0
      pidController.setP(0.01, 0);
      pidController.setI(0.0001, 0);
      pidController.setD(0, 0);
      pidController.setFF(0, 0);
      pidController.setIZone(2, 0);
      pidController.setIMaxAccum(5, 0);
      pidController.setDFilter(0, 0);
      pidController.setOutputRange(-1, 1, 0);

      // configure slot 1
      pidController.setP(0.5, 1);
      pidController.setI(0, 1);
      pidController.setD(0.02, 1);
      pidController.setFF(0, 1);
      pidController.setIZone(0, 1);
      pidController.setIMaxAccum(0, 1);
      pidController.setDFilter(0.3, 1);
      pidController.setOutputRange(-1, 1, 1);
  }
  ...
  public void moveToSetpoint(double setPoint) {
      // configure the wanted setpoint to start the PID loop with slot 0
      pidController.setReference(setPoint, CANSparkBase.ControlType.kPosition, 0);
  }
  public void rotateAtSpeed(double setPoint) {
      // configure the wanted setpoint to start the PID loop with slot 1
      pidController.setReference(setPoint, CANSparkBase.ControlType.kVelocity, 1);
  }
}
```

##### Wrap Around

Encoder sensors work on a shaft with rotational motion. Absolute encoder have their values wrap around when they reach 359 to 0. This creates a problem for PID loops as they do not natively understand this concept, instead they thing that the value has changed by 359 instead of just 1 degree and this causes the output change dramatically as a result, this is normally not wanted.

To handle this, the SparkMAX PIDF loop supports a wrap around mode, which allows them to understand this situation (if properly configured). This setting affects all the slots and can only be used for position loops. 
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax motor;
  private final RelativeEncoder encoder;
  private final SparkPIDController pidController;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      ...
      encoder = motor.getEncoder();
      pidController = motor.getPIDController();

      pidController.setFeedbackDevice(encoder);

      pidController.setPositionPIDWrappingMinInput(0);
      pidController.setPositionPIDWrappingMaxInput(360);
      pidController.setPositionPIDWrappingEnabled(true);

      pidController.setP(0.01);
      pidController.setI(0.0001);
      pidController.setD(0);
      pidController.setFF(0);
      pidController.setIZone(2);
      pidController.setIMaxAccum(5); 
      pidController.setDFilter(0); 
      pidController.setOutputRange(-1, 1);
  }
  ...
}
```

##### Tuning

Tuning the PIDF can be done similarly to tuning a `PIDController`, using the shuffleboard. But idealy, one should use the _REV Hardware Client_ instead. The _Telemetry_ tab allows viewing information from the different sensors connected to the SparkMAX as well as changing the configuration values. This means that we can tune the PIDF settings while viewing a graph of the sensor values.

![telemetry tab + tuning](https://github.com/tomtzook/frc-learn-docs/assets/17641355/ed106ef9-0f07-4d4b-8528-b55add39b722)

#### Follower

The _Follower_ mode is quite unique and interesting. It allows a SparkMAX to mimic the output of another SparkMAX. This is incredibly useful when working with a system that has two or motor motors that are supposed to operate in sync. Using this mode, the _follower_ SparkMAX expects CANBus messages from the _master_ SparkMAX which indicate what output the _master_ is using to drive its motor. The _follower_ motor then uses the same output exactly. This also works well with limits. A _master_ may have multiple followers.

```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax master;
  private final CANSparkMax follower;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      follower = new CANSparkMax(1, CANSparkLowLevel.MotorType.kBrushless);
      ...

      follower.follow(master);
  }

  public void move(double speed) {
      master.set(speed);
  }

  public void stop() {
      master.stopMotor();
  }
}
```

If the follower motor moves in the wrong direction due to being placed the other way around, using `setInverted` with `follow` won't work, using `follow(master, true)` instead.

```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax master;
  private final CANSparkMax follower;

  public SomeSystem() {
      motor = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      follower = new CANSparkMax(1, CANSparkLowLevel.MotorType.kBrushless);
      ...

      follower.follow(master, true);
  }

  public void move(double speed) {
      master.set(speed);
  }

  public void stop() {
      master.stopMotor();
  }
}
```

This functions over all possible control modes that the _master_ may be using, with the _follower_ using the _PercentVBus_ output to the motor. So it is possible to use following while using a PIDF on the _master_ motor. Note that the _follower_ won't be running a PIDF loop itself, just use the output from the _master_'s loop, and thus the entire motion is also only based on the feedback device used for the _master_'s loop.
```java
public class SomeSystem extends SubsystemBase {

  private final CANSparkMax master;
  private final RelativeEncoder encoder;
  private final SparkPIDController pidController;

  private final CANSparkMax follower;

  public SomeSystem() {
      master = new CANSparkMax(0, CANSparkLowLevel.MotorType.kBrushless);
      follower = new CANSparkMax(1, CANSparkLowLevel.MotorType.kBrushless);
      ...
      encoder = master.getEncoder();
      pidController = master.getPIDController();

      pidController.setFeedbackDevice(encoder);

      // configure slot 0
      pidController.setP(0.01, 0);
      pidController.setI(0.0001, 0);
      pidController.setD(0, 0);
      pidController.setFF(0, 0);
      pidController.setIZone(2, 0);
      pidController.setIMaxAccum(5, 0);
      pidController.setDFilter(0, 0);
      pidController.setOutputRange(-1, 1, 0);

      // configure slot 1
      pidController.setP(0.5, 1);
      pidController.setI(0, 1);
      pidController.setD(0.02, 1);
      pidController.setFF(0, 1);
      pidController.setIZone(0, 1);
      pidController.setIMaxAccum(0, 1);
      pidController.setDFilter(0.3, 1);
      pidController.setOutputRange(-1, 1, 1);

      follower.follow(master);
  }
  
  public void moveToSetpoint(double setPoint) {
      // configure the wanted setpoint to start the PID loop with slot 0
      pidController.setReference(setPoint, CANSparkBase.ControlType.kPosition, 0);
  }
```

It should be noted that this does not equate to running a PID loop on both the master and follower. Because there is not consideration to the follower sensor, it is not certain to expect the follower to reach the setpoint used by the master. If they both operate the same shaft/mechanism together (e.g an arm operated by two motors), than this is fine, if not (e.g. two seperate motors operating a single wheel each, like in a shooter), then follow should not be used.


#### Smart Motion

### Links

- [SparkMAX REV Docs](https://docs.revrobotics.com/brushless/spark-max/overview)
- [SparkMAX Code Examples](https://github.com/REVrobotics/SPARK-MAX-Examples)
