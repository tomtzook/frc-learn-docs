### Motor Basic Control (Talon SRX)

Define and create a _Talon SRX_ motor controller. Run the motor at 50% (clockwise) during _teleop_.

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
    motor.stopMotor();
}
```

### Motor Basic Control (Spark Max)

Define and create a _Spark Max_ motor controller. Run the motor at 50% (clockwise) during _teleop_.

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
    motor.stopMotor();
}
```

### Motor Basic Control (Talon FX)

Define and create a _Talon FX_ motor controller. Run the motor at 50% (clockwise) during _teleop_.

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

### Basic Controller Axis Use (Talon SRX, Xbox, Right Y)

Define and create a _Talon SRX_ motor controller and an _XBOX_ controller. Run the motor during _teleop_ according to _Right Y_ axis of the gamepad.

```java
private WPI_TalonSRX motor;
private XboxController xbox;

@Override
public void robotInit() {
    motor = new WPI_TalonSRX(RobotMap.MOTOR_IDENTIFIER);
    motor.configFactoryDefault(); // factory default

    xbox = new XboxController(0);
}

@Override
public void teleopInit() {
    
}

@Override
public void teleopPeriodic() {
    double speed = -xbox.getRightY();
    motor.set(speed);
}

@Override
public void teleopExit() {
    motor.stopMotor();
}
```

### Encoder Access (Talon SRX, SRX Encoder)

Define and create a _Talon SRX_ motor controller. Access an _SRX Magnetic Encoder_ connected to it. 

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

Define and create a _Spark Max_ motor controller. Access the _Integrated Encoder_ of the motor (_NEO_-series) it is connectd to. 

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

Define and create a _Talon FX_ motor controller. Access the _Integrated Encoder_ of the motor (_Falcon500_ / _Kraken_) it is connectd to. 

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

Define and create a _CAN Coder_ encoder. Access the _Absolute Position_ measurement from it.

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

### Gyro Access (Pigeon 2, Yaw)

Define and create a _Pigeon 2_ IMU. Access the _Yaw_ measurement from it.

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

### Subsystem, Single Motor (NEO 1.1, Spark Max, NEO Integrated Encoder)

Implement a _Subsysem_ with a single _NEO 1.1_ motor and a _Spark Max_ operating it. Grant access to the integrated encoder.

```java
public class SubsystemName extends SubsystemBase {

    private final CANSparkMax motor;
    private final RelativeEncoder encoder;

    public SubsystemName() {
        motor = new CANSparkMax(RobotMap.MOTOR_ID, CANSparkLowLevel.MotorType.kBrushless);
        motor.restoreFactoryDefaults(); // factory default

        encoder = motor.getEncoder();
        encoder.setPositionConversionFactor(1 / RobotMap.MOTOR_TO_MECHANISM_GEAR_RATIO);
        encoder.setVelocityConversionFactor(1 / RobotMap.MOTOR_TO_MECHANISM_GEAR_RATIO);
    }

    public double getPositionRotations() {
        return encoder.getPosition();
    }        

    public double getVelocityRpm() {
        return encoder.getVelocity();
    }

    public void move(double speed) {
        motor.set(speed);
    }

    public void stop() {
        motor.stop();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("SubsystemName_Position", getPositionRotations());
        SmartDashboard.putNumber("SubsystemName_Velocity", getVelocityRpm());
    }
}
```

### Command, Single Motor, Constant Speed, No Finish

Implement a _Command_ operating a _Subsystem_ with a single motor at a speed defined via the constructor.

```java
public class CommandName extends Command {

    private final SubsystemName subsystem;
    private final double speed;

    public CommandName(SubsystemName subsystem, double speed) {
        this.subsystem = subsystem;
        this.speed = speed;

        addRequirements(subsystem);
    }

    @Override
    public void initialize() {
        subsystem.move(speed);
    }

    @Override
    public void execute() {
        
    }

    @Override
    public void end(boolean interrupted) {
        subsystem.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
```

### Basic Command Scheduling

Create an instance of a _Subsystem_ and start a _Command_ in _teleop_.

```java
private SubsystemName subsystem;

@Override
public void robotInit() {
    subsystem = new SubsystemName();
}

@Override
public void teleopInit() {
    Command command = new CommandName(subsystem);
    command.schedule();
}

@Override
public void teleopPeriodic() {

}

@Override
public void teleopExit() {
    
}
```
