### Motor Basic Control (TalonSRX)

```java
private WPI_TalonSRX motor;

@Override
public void robotInit() {
    motor = new WPI_TalonSRX(RobotMap.MOTOR_IDENTIFIER);
    motor.configFactoryDefault(); // factory default
}

@Override
public void teleopInit() {
    motor.set(0.5);
}

@Override
public void teleopPeriodic() {

}

@Override
public void teleopExit() {
    motor.stopMotor()
}
```

### Motor Basic Control (SparkMax)

```java
private CANSparkMax motor;

@Override
public void robotInit() {
    motor = new CANSparkMax(RobotMap.MOTOR_IDENTIFIER, CANSparkLowLevel.MotorType.kBrushless);
    motor.restoreFactoryDefaults(); // factory default
}

@Override
public void teleopInit() {
    motor.set(0.5);
}

@Override
public void teleopPeriodic() {

}

@Override
public void teleopExit() {
    motor.stopMotor()
}
```

### Motor Basic Control (TalonFX)

```java
private TalonFX motor;
private DutyCycleOut dutyCycleControl;
private NeutralOut neutralControl;

@Override
public void robotInit() {
    motor = new TalonFX(RobotMap.MOTOR_IDENTIFIER);
    motor.getConfigurator().apply(new TalonFXConfiguration()); // factory default

    dutyCycleControl = new DutyCycleOut(0);
    neutralControl = new NeutralOut();
}

@Override
public void teleopInit() {
    motor.setControl(dutyCycleControl.withOutput(0.5));
}

@Override
public void teleopPeriodic() {

}

@Override
public void teleopExit() {
    motor.setControl(neutralControl);
}
```

### Encoder Access (Talon SRX, SRX Encoder)

```java
private static final int PPR = 4096;
private static final double MOTOR_TO_MECHANISM_GEAR_RATIO = 8.0 / 1.0; // driver : driven

private WPI_TalonSRX motor;

@Override
public void robotInit() {
    motor = new WPI_TalonSRX(RobotMap.MOTOR_IDENTIFIER);
    motor.configFactoryDefault(); // factory default
}

@Override
public void teleopPeriodic() {
    double counts = motor.getSelectedSensorPosition();
    double mechanismRotations = counts / PPR / MOTOR_TO_MECHANISM_GEAR_RATIO;

    double countsPer100ms = motor.getSelectedSensorVelocity();
    double mechanismRpm = countsPer100ms / PPR * 600.0 / MOTOR_TO_MECHANISM_GEAR_RATIO;
}
```

### Encoder Access (Spark Max, NEO Integrated Encoder)

```java
private static final double MOTOR_TO_MECHANISM_GEAR_RATIO = 8.0 / 1.0; // driver : driven

private CANSparkMax motor;
private RelativeEncoder encoder;

@Override
public void robotInit() {
    motor = new CANSparkMax(RobotMap.MOTOR_IDENTIFIER, CANSparkLowLevel.MotorType.kBrushless);
    motor.restoreFactoryDefaults(); // factory default

    encoder = motor.getEncoder();
    encoder.setPositionConversionFactor(1 / MOTOR_TO_MECHANISM_GEAR_RATIO);
    encoder.setVelocityConversionFactor(1 / MOTOR_TO_MECHANISM_GEAR_RATIO);
}

@Override
public void teleopPeriodic() {
    double mechanismRotations = encoder.getPosition();
    double mechanismRpm = encoder.getVelocity(); 
}
```

### Encoder Access (Talon FX, Integrated Encoder)

```java
private static final double MOTOR_TO_MECHANISM_GEAR_RATIO = 8.0 / 1.0; // driver : driven

private TalonFX motor;
private StatusSignal<Double> positionSignal;
private StatusSignal<Double> velocitySignal;

@Override
public void robotInit() {
    motor = new TalonFX(RobotMap.MOTOR_IDENTIFIER);

    TalonFXConfiguration configuration = new TalonFXConfiguration();
    configuration.Feedback.FeedbackSensorSource = FeedbackSensorSourceValue.RotorSensor;
    configuration.Feedback.SensorToMechanismRatio = MOTOR_TO_MECHANISM_GEAR_RATIO;
    motor.getConfigurator().apply(configuration);

    positionSignal = motor.getPosition();
    velocitySignal = motor.getVelocity();
}

@Override
public void teleopPeriodic() {
    BaseStatusSignal.refreshAll(positionSignal, velocitySignal); // only once per 20ms loop, typically in periodic

    double mechanismRotations = positionSIgnal.getValueAsDouble();
    double mechanismRotationsPerSecond = velocitySignal.getValueAsDouble();
}
```

### Encoder Access (CANCoder, Absolute)

```java
private CANCoder encoder;
private StatusSignal<Double> absPositionSignal;

@Override
public void robotInit() {
    encoder = new CANCoder(RobotMap.ENCODER);

    CANcoderConfiguration configuration = new CANcoderConfiguration();
    configuration.MagnetSensor.AbsoluteSensorRange = AbsoluteSensorRangeValue.Unsigned_0To1;
    encoder.getConfigurator().apply(configuration);

    absPositionSignal = motor.getAbsolutePosition();
}

@Override
public void teleopPeriodic() {
    BaseStatusSignal.refreshAll(absPositionSignal); // only once per 20ms loop, typically in periodic

    // typically absolute encoders are placed on the shaft after a gearbox, so gearbox ratio handling isn't needed here.
    double mechanismRotationAbs = absPositionSignal.getValueAsDouble(); // [0, 1] -> [0, 360]
}
```

### Gyro Access (Pigeon2, Yaw)

```java
private Pigeon2 pigeon;
private StatusSignal<Double> yawSignal;

@Override
public void robotInit() {
    pigeon = new Pigeon2(RobotMap.PIGEON);

    pigeon.getConfigurator().apply(new Pigeon2Configuration());
}

@Override
public void teleopPeriodic() {
    BaseStatusSignal.refreshAll(yawSignal); // only once per 20ms loop, typically in periodic

    double yawDegrees = yawSignal.getValueAsDouble(); 
}
```
