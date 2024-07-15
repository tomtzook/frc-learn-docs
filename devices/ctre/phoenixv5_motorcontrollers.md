
## Phoenix V5 Motor Controllers

The _TalonSRX_ and _VictorSPX_ are the two motor controllers still in use, that use the Phoenix _V5_ framework. 
Although pretty old, they are still quite capable as motor controllers, they boast a lot of functionality, and although no longer sold or updated, they are still quite useful. Their major down side though is that they don't support most modern _FRC_ motor controllers, which have moved to being brushless instead of brushed.

Both controllers support a similar code interface and capabilities, so from our perspective using them is pretty much the same. They do have different capabilities, most notably the _VictorSPX_ lacks a data port for sensor connections, something that the _TalonSRX_ does have.

### Basic Configuration

The following are basic configurations available for all devices:
- `ID` is the CANBus identifier of the device. It is used to distinctly identify the device and communicate with it. This value should be unique per device. This value is a non-negative integer.

The following configurations apply only to motor controllers:
- `NeutralMode` describes the behaviour of the motor when entering idle motor. Idle motor refers to the motor output being at 0. Basically, when the motor is stopped. 
  - `Coast` the standard operating mode. In this mode, when entering idle mode, the motor simply stops receiving power and will do nothing. If the motor has momentum, it will continue rotating freely until such momentum is lost.
  - `Brake` In this mode, when entering idle mode, the motor controller shorts out the motor's power connections, resulting in the motor forcibly refusing to allow the stator to rotate. In affect, the motor will resist any attempt to rotate the shaft, and will immediatly negate any momentum the shaft had. This mode can be used to keep the shaft in place. Brake mode works in disabled mode due to the motor not actually requiring any outside current.
- `Inverted` determines if the motor output should be inverted (i.e. the motor will turn to the opposite direction, plus direction will act as minus direction and minus as plus).

> [!WARNING]
> Brake mode can seriously increase the temperature of the motor due to how power is generated. Be careful when using it.

#### Code Initialization and Basic Configuration

We've already initialized and used the TalonSRX and VictorSPX in the past, it is a simple matter of creating new instance of their classes:
```java
public class SomeSystem extends SubsystemBase {

  private final WPI_TalonSRX motorTalon;
  private final WPI_VictorSPX motorVictor;

  public SomeSystem() {
      motorTalon = new WPI_TalonSRX(0);
      motorVictor = new WPI_VictorSPX(1);
  }
}
```

For here we can configure and operate the motor controller as we like. As mentioned before, we should start by resetting the settings to factory default:
```java
public class SomeSystem extends SubsystemBase {

  private final WPI_TalonSRX motor;

  public SomeSystem() {
      motor = new WPI_TalonSRX(0);
      motor.configFactoryDefault();
  }
}
```

Only after restoring the settings should we start modifying the settings as wanted. Most settings should be set in the constructor when initializing the motor, so that the settings affect our operation of the motor throughout the robot code.
```java
public class SomeSystem extends SubsystemBase {

  private final WPI_TalonSRX motor;

  public SomeSystem() {
      motor = new WPI_TalonSRX(0);
      motor.configFactoryDefault();
      motor.setInverted(true);
      motor.setNeutralMode(NeutralMode.Brake);
  }
}
```

### Built-In Sensors

### Feedback Devices

### Limits

### Control Modes

### Links
