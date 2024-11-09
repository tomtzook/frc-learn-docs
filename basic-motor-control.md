Motors serve as the primary method with which FRC robots produce motion. As such, controlling motors is the bare minimum for any FRC robot code.

DC motors used in FRC are controlled directly by motor controllers, which in turn, are controlled by the robot code running on the RoboRIO. In this document we will
examine how to control such devices in the most basic form.

> [!NOTE]
> Read [introduction to motors in FRC](introduction-to-motors-in-frc.md) for more information about DC motors

## Motor Controllers

Motor controllers are complex devices, made up of electronic circuitry and a microcontroller. Each single motor controller is dedicated to controlling a single motor.
Due to the complexity of controlling motors, especially brushless, these devices are a requirement, and RoboRIO cannot directly control motors on its own.

The following are the current contemprary motor controllers used in FRC:

Name | Image | Manufacturer | Interface to RoboRIO | Supported Motors | Sensor Ports | Required Vendordep | Code Class
-----|-------|--------------|----------------------|-----------------------|--------------------|----------------|-----------
TalonSRX | ![talonsrx](https://github.com/user-attachments/assets/45fc5daa-1417-4049-ab0f-4923489d99c9) | CTRE | CANBus | Any Brushed Motor | Quadrature Encoder, Limit Swiches, Analog Sensor | _Phoenix5_ | `com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX`
VictorSPX | ![victorspx](https://github.com/user-attachments/assets/c3ac7dfe-d861-44ea-b9d9-6017831332d9) | CTRE | CANBus | Any Brushed Motor | - | _Pheonix5_ | `com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX`
TalonFX | ![talonfx + falcon500](https://github.com/user-attachments/assets/1dfcd907-9af3-4b16-8e6f-a483924640ec) | CTRE | CANBus | _Falcon500_/_Kraken_ (Integrated) | Integrated Encoder, Limit Switches | _Phoenix6_ | `com.ctre.phoenix6.hardware.TalonFX`
SparkMax | ![sparkmax](https://github.com/user-attachments/assets/3ba2521e-372c-4dc0-a55b-1e0762d4e500) | REV | CANBus | Any Brushed Motor, _NEO_-Series Motors | Quadrature Encoder, Absolute Enoder, Limit Switches, Analog Sensor | _REVLib_ | `com.revrobotics.CANSparkMax`

#### PercentVBus

At the core of each motor controller in FRC, is the _PercentVBus_ control mode. In this mode, the motor controller receives a _PercentVBus_ number from the RoboRIO indicating how to operate the motor.
This number is made up of:
- An absolute value [0, 1] indicating the percentage at which to run the motor (e.g. `0.5` = 50%). The percentage is a percentage of the voltage bus, i.e. the power supply voltage. If the battery is at maximum (>= 12V), 50% will provide 6V. If the battery is at, say, 10V, then 50% will provide 5V.
- A sign indicating the direction of rotation. Positive number provide clockwise rotation, while negative numbers provide counter-clockwise rotation.

For example, to request the motor to run at 50% of available voltage in a counter-clockwise direction, we will request the motor controller to run with `-0.5`. 
Requesting the motor to move with `0` will stop the motor rotation.

#### Motor Controller Classes

Manufacturers of motor controllers for FRC provide teams with libraries of code. These libraries provide ready-to-use code for their devices. Motor controller code, specifically, is organized around classes. Each motor controller implementation is provided in a complete class, and to use this motor controller, one only needs to use that class. The _TalonSRX_ motor controller from _CTRE_ is implemented in the `WPI_TalonSRX` class, for example.

Each instance of such classes is capable of controlling a single motor controller, and thus only a single motor. If the robot has multiple motor controllers, one needs multiple instances. Of course the instances must be of the type of the specific motor controller used.

#### Identifier

In order to control a specific motor controller, we must have a way to differentiate between it and others. Given that if we want to move an elevator, we would want to control the motor operating that elevator and not other motors.

In this era of CANBus use in FRC, each motor controller has an integer identifier. This identifier may be retreived or modified via propriatry software of the manufacturer; We will not discuss how here.

Once the identifier is known, it can be used with the motor controller class to create an instance controlling a specific motor controller.

![identifier in phoenix tuner x](https://github.com/user-attachments/assets/71354aab-bd90-40fa-8b04-396ffdc024cb)

### Motor Controller in Code

In this example we will use a `TalonSRX` motor controller with identifier `2` which controls an elevator, and write code to run the motor.

To start, we need to define a constant with the identifier of the motor in `RobotMap`; all identifiers must be defined here. This will provide an easy location to find all the identifiers.

```java
public class RobotMap {

  public static final int ELEVATOR_MOTOR = 2;
}
```

Next, we will create an instance of the motor controller class so it could be used. This is done in the `Robot` class. 
We will define a member variable for the instance in the class so it could be used throughout the class. 
We will provide a new instance as a value to this variable only in `robotInit`, not earlier and not later.

```java
public class Robot extends TimedRobot {

    // define variable for the motor controller instance
    private WPI_TalonSRX elevatorMotor;

    @Override
    public void robotInit() {
        // create instance for motor controller
        elevatorMotor = new WPI_TalonSRX(RobotMap.ELEVATOR_MOTOR);
    }

    ....
}
```

Now we can tell the motor to rotate when we want. This is done using the `set` method of the motor controller class, which expects the _PercentVBus_ value.

```java
public class Robot extends TimedRobot {

    private WPI_TalonSRX elevatorMotor;

    @Override
    public void robotInit() {
        elevatorMotor = new WPI_TalonSRX(RobotMap.ELEVATOR_MOTOR);
    }

    ....

    @Override
    public void teleopInit() {
        // set the motor to move at 50%, clockwise
        elevatorMotor.set(0.5);
    }

    ....
}
```

Once `set` is called, the motor controller stores this command information and executes it. It will continue to run the motor as such until a new command is received. So calling `set` in `teleopInit` will still
keep the motor running for the duration of the _teleop_ mode.

We can and should stop the motor once we exit the motor, or when we finished the move. `stopMotor` will do that, overriding any last `set`.

```java
public class Robot extends TimedRobot {
    ....

    @Override
    public void teleopExit() {
        elevatorMotor.stopMotor();
    }

    ....
}
```

If `stopMotor` is not called and we switch mode to _disabled_ the motor will stop rotating. But once returning to a different mode, the motor will start rotating again. This is because in _disabled_ mode, 
rotating motor is disabled in the robot and motor controllers will refuse operating motors.

It is good practice to call `stopMotor` once the motion wanted ends; either because it finished, or because it was interrupted by something else (like switching mode).
