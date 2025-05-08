_Swerve Drive_ is a flexible, powerful, and now, very popular drive configuration. What makes it so capable, is its omni-directional nature (i.e. it can move in any direction without rotating). 
However, unlike older omni-directional configurations (e.g. using Omni or Mecanum wheels), _Swerve_ retains the speed and power of a tank drive, making it a sort of _Best of Both Worlds_.

This comes at a cost, of course, as it is rather complex, both mechanically and programmatically (and also expensive), But well worth it if done right.

![Swerve Drive](https://github.com/user-attachments/assets/3d9af5e5-4209-4172-9cb5-1f18512ec22f)

## How Swerve Works

_Swerve_ drives are rectangular only, and made up of **for** _Swerve Modules_ at each vertex of the chassis. Each of these modules is made up
of a wheel, and two motors. One motor, the _Drive_ motor, powers the wheel, while a second motor, the _Steer_ motor, orients the wheel to face different directions.
By changing the heading of the wheel, the module can put forth the full power of the _Drive_ motor to move in that specific direction.

![swerve module](https://github.com/user-attachments/assets/d20a8b19-9cf1-4b3e-93c8-d6b4f23bcb16)

![drive from above](https://github.com/user-attachments/assets/f81b3d13-4ff8-4175-81ed-a3bd1649e873)

While each module is capable of motion on its own, it is how we combine them together, that allows the _Swerve_ to work. 
To move the _Swerve_ at any one direction, we typically have to orient the wheels of all the modules to face that specific direction, and
then rotate the wheels to actually move.

![drive at direction](https://github.com/user-attachments/assets/e4267000-1809-449f-9534-fc0e015263a8)

This is the core of all directional motion with the _Swerve_. The other kind of motion is rotation, which rotates the _Swerve_ around its center (Yaw).
Again, by orienting the different wheels to specific directions, we can produce this kind of motion.

![drive rotation](https://github.com/user-attachments/assets/01774808-9588-42c2-b40b-efb7826d90fd)

All of _Swerve_ motion boils down to where we orient each module to, and then driving the module at that direction. The combined effect of all the modules
together will produce the desired motion.

We can basically look at the motion of a swerve as having 3 degrees of freedom: Y (forward,backward) motion, X (right,left) motion, Rotation (around Z axis, yaw).
The Y and X components, dictate directional motion. We can combine all 3 to produce a specific state for all modules to acheive this motion. We will call a combination of
all three _Motion Vector_. 

Given the motion vector `(5, 1, 0)` we will be producing a motion of `5 meters/second` forward, `1 meter/second` to the right and `0 radians/second` rotation, giving us a diagonal motion, which is faster forward.
Then it is just a matter of figuring out what each module should do to acheive this.

This is how we will represent motion in code.

> [!NOTE]
> We typically use speeds in the motion vector when working with Swerve. For X/Y motion the speed is in meters per second, while for rotation it is in radians per second.

### Swerve Module Properties

Different _Swerve Modules_ have different capabilities and different workings. It is important to know what module we work with to accurately program it. There are a few things
we should take note of:
- Type of Drive Motor and Motor Controller: each motor has different behaviour, and using different motor controllers involves different code.
- Type of Steer Motor and Motor Controller: same as above
- Drive Gear Ratio: all modules will have a gear between the drive motor and wheel. This matters to our code calculations.
- Steer Gear Ratio: same as above, but for steer.
- Encoders: each module will have at minimum 2 encoders, but typically 3. Using them in code changes depending on what they are.
  - Integrated Drive Encoder
  - Integrated Steer Encoder
  - Absolute Steer Encoder   

## Implementing

We will divide our code into two distinct parts: the _Swerve module_ and the _Swerve drive_. The module will control each individual module, and the drive will be responsibly for the behaviour of the entire chassis. 

The drive will be operated passing it a _motion vector_, from which it will control each individual module to meet the request in the vector.

Control of the module is rather straight-forward: the swerve drive will request each module for a specific state (`SwerveModuleState` class), containing `velocity` for the Drive motor and `angle` for the Steer motor, and the module will have to do its best to maintain this state. This state will be calculated from the _motion vector_.

The drive motor will be controlled using the _built-in velocity closed loop controller_, using the drive motor's _integrated relative encoder_. The state will contain a `velocity` measure in _meters per second_ which we will set to the PID controller and let it take care of maintaining this velocity.

The steer motor will be controlled using the _built-in position closed loop controller_, using the steer motor's _integrated relative encoder_. The state will contain a `position` measure in _degrees_ which we will set to the PID controller and let it take care of maintaining.

### Swerve Module

The first place to start our swerve code is with the basic modules. Because all the modules basically behave the same way, we will
be creating a new class called `SwerveModule` to represent them all, and create 4 instances to use (1 for each module).

Create the `SwerveModule` class (preferrably under `subsystems` package). In it, we will be defining the drive and steer motor controllers
and any relevant properties.

> [!NOTE]
> `SwerveModule` is **not** a subsystem

It is recommended to pass the constructor of the class the motor ids for drive and steer motors, since this class will be used multiple times for different modules with different motor ids.

```java
public class SwerveModule {

  private final MotorControllerType driveMotor;
  private final MotorControllerType steerMotor;

  public SwerveModule(int driveMotorId, int steerMotorId) {
    // this is just an illustration of how the code would look. The exact manner of initialization of the motor controller object
    // is highly dependent on the specific motor controller type.
    driveMotor = new MotorControllerType(driveMotorId);
    steerMotor = new MotorControllerType(steerMotorId);
  }
}
```

#### Module Initial Configuration

Initialize and configure your motor controllers in the constructor. Make sure to configure both motors according to our needs
- Drive motor should be in Brake mode, allowing our swerve to quickly stop and hold its ground while not moving.
- Steer motor should be in Coast mode, we will be using PID to hold it in place.
- Configure the Drive and Steer motors with inversion acoording to the assembly properties.
- Configure the Drive motor for PID loop with the motor's integrated encoder, and the same for the Steer motor 
  - For CTRE, use the `config.Feedback.FeedbackSensorSource = source` property, and set it to `config.FeedbackSensorSourceValue.RotorSensor`
  - For REV, use the `config.closedLoop.feedbackSensor(sensor)` method, and set it to `config.ClosedLoopConfig.FeedbackSensor.kPrimaryEncoder`
- Configure the encoder with conversion factors according to the gear ratios of Drive and Steer
  - This will make it easier to query the encoder values, removing the need for constant conversions.
  - We are not making a full conversion to the wheel, because it is linear data (as opposed to the rotational data of the motor) and is typically not represented well by the encoder API.
  - For CTRE, use the `config.Feedback.SensorToMechanismRatio = gearRatio` property, and set it to the gear ratio (_driver / driven_)
  - For REV, use the methods `config.encoder.positionConversionFactor(1 / gearRatio)` and `config.encoder.velocityConversionFactor(1 / gearRatio)`. Here it is `1 / gearRatio` (_driven / driver_ basically) because rev simply represents it differently than CTRE.
- Configure the PID controllers of the motors to initial PID values. For now, just set `P` to `1` and the rest to `0`, we'll be calibrating later. Set the minimum and maximum output range to `-1` (min, 100% reverse) and `1` (max, 100% forward). 
  - For CTRE, use `config.Slot0.kP = kP`, `config.Slot0.kI = kI` and `config.Slot0.kD = kD` properties (CTRE has no configurable IZone, it is automatic; min and max values are always `-1` and `1`)
  - For REV, use `config.closedLoop.pidf(kP, kI, kD, kF)`, `config.closedLoop.iZone(izone)` and `config.closedLoop.outputRange(min, max)`
- For the Steer motor, configure closed-loop position wrapping. This is necessary because the steer has a full rotation motion (0 -> 360), which is not natively supported in PID. Position-wrapping provides such support.
  - For CTRE, set `config.ClosedLoopGeneral.ContinuousWrap = true`. 
  - For REV, use `config.closedLoop.positionWrappingInputRange(0, 1)` (`0` = `0`; `1` = _full rotation_, `360`) and `config.closedLoop.positionWrappingEnabled(true)`.

Once your are finished setting the configuration values, write them to the motor controllers to apply the changes.

The general template of how it would look should be 
```java
public SwerveModule(int driveMotorId, int steerMotorId) {
    driveMotor = new MotorControllerType(driveMotorId);
    steerMotor = new MotorControllerType(steerMotorId);

    // the real type depends on the motor, for TalonFX this would be TalonFXConfiguration, for example.
    DriveMotorConfiguration driveConfig = new DriveMotorConfiguration();
    // the fields and values here are very dependent on the motor controller type. This is just a general illustration of what settings to touch.
    driveConfig.inverted = driveInverted;
    driveConfig.idleMode = brakeMode;
    driveConfig.feedbackSensor = motorSensor;
    driveConfig.conversionFactor = gearRatio;
    driveConfig.p = 1;
    driveConfig.i = 0;
    driveConfig.d = 0;
    driveConfig.iZone = 0;
    driveConfig.outputMin = -1;
    driveConfig.outputMax = 1;
    // how to apply configuration depends on the specific motor, for TalonFX this is done via the configurator, for example.
    driveMotor.configure(driveConfig);

    // the real type depends on the motor, for SparkMax this would be SparkMaxConfig, for example.
    SteerMotorConfiguration steerConfig = new SteerMotorConfiguration();
    // the fields and values here are very dependent on the motor controller type. This is just a general illustration of what settings to touch.
    steerConfig.inverted = steerInverted;
    steerConfig.idleMode = coastMode;
    steerConfig.feedbackSensor = motorSensor;
    steerConfig.conversionFactor = gearRatio;
    steerConfig.p = 1;
    steerConfig.i = 0;
    steerConfig.d = 0;
    steerConfig.iZone = 0;
    steerConfig.outputMin = -1;
    steerConfig.outputMax = 1;
    steerConfig.positionWrapping = true;
    // how to apply configuration depends on the specific motor, for SparkMax this is done via motor.configure, for example.
    steerMotor.configure(steerConfig);
}
```

#### Module Sensors

There are 2 sensors we care about: the integrated encoders of the motors. Each providing us of several points of information we need to use. For the Drive motor we will be using its position for odometery and velocity to track its state. For the Steer motor, we care about its position for optimizations, odometry and state tracking.

As such, implement 5 methods:
- `getHeadingDegrees`: returns the position of the steer encoder (in degrees). Heading refers to it indicating where the wheel is faced.
- `getPositionMeters`: returns the position of the drive encoder (in meters).
- `getVelocityMetersPerSecond`: returns the velocity of the drive encoder (in meters per second).
- `getState`: return a `SwerveModuleState`. Create this class from the drive velocity and steer position (`new SwerveModuleState(velocity, heading)`).
- `getPosition`: return a `SwerveModulePosition`. Create this class from the drive position and steer position (`new SwerveModuleState(position, heading)`).

To implement each of those, you will need to access the encoders for the motors and perform some unit conversions.
- For CTRE
  - you will need to use the `StatusSignal` API to access the sensor information.
  - The motor will export `getPosition` and `getVelocity` which return a `StatusSignal` each.
  - Call these methods in the constructor and save the returned signal objects in members.
  - Update these signals in an `update` method (create one). Call `BaseStatusSignal.refreshAll(drivePosition, driveVelocity)` to refresh the value of the signals from the sensor.
  - Call `getValue` from a signal to receive its current value. This will return a wrapped value, you will need to export it to `double` and then convert the value to the wanted measurement unit.
    - For velocity you will receive `AngularVelocity`, use `velocity.in(Units.RotationsPerSecond)` to get it in Rps and then convert the value to meters per second.
    - For position you will receive `Angle`, use `position.in(Units.Rotations)` to get it in rotations and then convert the value to meters.
    - Recall that we configured the device to give us encoder info **after** the gear-box, so no need to consider it here.
- For REV   
  - you will use the `RelativeEncoder` API.
  - Retreive the `RelativeEncoder` instance in the constructor and save it into a member.
  - Use `encoder.getPosition` to receive the position in rotations.
  - Use `encoder.getVelocity` to receive the position in rotations per minute (RPM).
  - Recall that we configured the device to give us encoder info **after** the gear-box, so no need to consider it here.

We will see how these methods are used later. 

> [!NOTE]
> For CTRE motors, it is requested above to create an `update` method. This should be a simple `public void` parameter-less method.
> It will be called for all the modules from the swerve drive.

#### Module Control

First, start by implementing a simple `stop` method to stop both Drive and Steer motors.
- To stop a CTRE motor, use the `NeutralOut` control type, and call `motor.setControl` with it.
- To stop a REV motor, simply call `motor.stopMotor`.

The main control of the module is via `SwerveModuleState` provided from the swerve drive. As such, this is what we will implement here. Create a method called `set` which receives a `SwerveModuleState` parameter and is `void`. In this method we will take the state data and set the motors.

For the drive motor, we will use `state.speedMetersPerSecond` value. This will need to be converted to encoder measurement units. Recall that we set the motor ratio/conversion factors to include the gear ratio; As such, we do not need to consider the gear ratio, just the wheel. After conversion, set the motor to use a velocity PID loop with the calculated velocity. 
- For CTRE use the `VelocityDutyCycle` control mode. Set its `Velocity` variable to the velocity and call `motor.setControl` with it.
- For REV use the `SparkClosedLoopController` object from the motor, and call `setReference` with the velocity value and `kVelocity` control type. 

For the steer motor, we will use `state.angle` value. This will need to be converted to encoder measurement units. Recall that we set the motor ratio/conversion factors to include the gear ratio; As such, we do not need to consider the gear ratio, just convert this angle to rotations. Do this with `state.angle.getRotations()`. After conversion, set the motor to use a position PID loop with the calculated position.
- For CTRE use the `PositionDutyCycle` control mode. Set its `position` variable to the position and call `motor.setControl` with it.
- For REV use the `SparkClosedLoopController` object from the motor, and call `setReference` with the position value and `kPosition` control type.

This is the basic implementation of the control. We will need to improve it in the future, but this is a good start.

#### Drive Feed Forward

_Feed Forward_ is quite useful for velocity control loops, mostly because unlike proportional control (P in PID), feed-forward provide a constant output needed to maintain velocity. So we will add it to our control of the drive motor.

First, declare a `SimpleMotorFeedforward` member in the class and initialize it in the constructor. It expects 3 values for its constructor
- `ks`: static feed-forward output, always provides a constant output. Measured in `volts` (voltage).
- `kv`: velocity feed-forward output, provides output based on the wanted velocity. Measured in `volts per meters per second`. 
- `ka`: acceleration feed-forward output, provides output based on the wanted acceleration. Measured in `volts per meters per seconds squared`.

`kv` can be calculated from `nominalMotorInputVolts / maxDriveSpeedMps`. That is, the optimal voltage for the motor (12 V for our motors) divided by the max speed that motor can acheive. The speed can be calculated from the motor specs
```
maxSpeed = motorFreeSpeedInRotations / gearRatio * wheelCircumference
```

The `freeSpeed` can actually be found in code, by using the `DCMotor` class. Retrieve the motor type in your module (e.g. `DCMotor.getNEO(1)` for _NEO 1.1_) and access the `freeSpeedRadPerSec` variable which provides the free speed in `rad/sec` (you will need to convert it to rotations).

`ka` can be calculated from `nominalMotorInputVolts / (wheelGripCoefficientOfFriction * standardGravity)` which basically translates to `12 / (1.19 * 9.81)`
- `wheelGripCoefficientOfFriction`: `1.19` is the typicall constant seen on the field with common FRC wheels
- `standardGravity`: a physics constant indicating the acceleration Earth's gravity applies on a body. 

The output of this feed foward type is calculated based on the input of _current velocity_ and _wanted velocity_. It provides a voltage
output to give the motor to _maintain_ to acheive _wanted velocity_.

In your `set` method, modify the control of the drive motor such that before setting the motor output you
- call `feedForward.calculateWithVelocities(currentVelocity, wantedVelocity)`
  - `currentVelocity` is the current velocity of the drive wheel (in meters per second). You can get it from `getVelocityMetersPerSecond`
  - `wantedVelocity` is the wanted velocity of the drive wheel (in meters per second). You can get it from the state (`SwerveModuleState.speedMetersPerSecond`).
- The output you will receive is in volts. Convert it to percent (`-1 -> 1`) by dividing by the battery voltage (from `RobotController.getBatteryVoltage()`)
- Set this value to the motor output
  - For CTRE, set the variable `FeedForward` in `VelocityDutyCycle`
  - For REV, use a different overload of `setReference` method: `setReference(velocityRotations, SparkBase.ControlType.kVelocity, ClosedLoopSlot.kSlot0, feedForwardPercent, SparkClosedLoopController.ArbFFUnits.kPercentOut);`

Now the Drive PID control is augmented with Feed Forward. 

> [!WARNING]
> Note that the feed foward will only update its value according to change when `set` is called.
> If `set` is only called once, the feed forward value will be stuck as the same because no new value was passed to the motor controller.
> This may cause issues with its behavior.

#### Optimizations

#### Absolute Encoder

##### Zero Angles


> mention about having to orient the wheel quickly or they will have a noticeble effect on the motion
