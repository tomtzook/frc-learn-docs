Motors provide FRC robots with force and motion. They are the base for designing most systems and are pretty reliable and not to complicated to use.

> [!NOTE]
> To make it clear, this document does not aim to teach the inner workings of DC motors, but simplify introduce to some of the basic concepts.

## DC Motors

FRC robots are limited to use DC (Direct Current) motors. Such motors are operated by providing Direct Current from a power source. 
This is enforced by the game rules. In fact, the game rules only allow using specific motors verified by FIRST. 

![dc motor](https://github.com/user-attachments/assets/9bc77ae8-267a-4804-83b1-7bf11a67d91b)

Legal motors can typically rotate up to 5800 RPM and provide a max of around 2.4 Nm torque. This is more than sufficent for robots. The selection of a specific motor is very dependent on system requirement, funds, control requirments and such. So we will see a combination of different motors on a robot.

Normally, a motor will be used by connecting its output shaft (the rotating part) to a bigger shaft/axle which causes a rotation of some mechanism. This may be a wheel,
a shaft mounting some arm, or more. Regardless, motors always produce a rotational motion and this may be used for rotating mechanisms, or converted to linear motion.

![motor mounted to wheel](https://github.com/user-attachments/assets/9d2b9fee-24bf-4f38-944b-9dd8576e3335)

The example above shows a motor attached to a wheel. As the motor rotates, so will the wheel.

DC motors are built on the concepts on magnets and magnetic fields. By passing a Direct Current through coils, a magnetic field is generated and pulls/pushes on magnets. This creates a rotation motion.
There are 2 derivatives to this design: Brushed and Brushless; Each is a bit different, but both use the same basic principles.

![brushed dc motor](https://github.com/user-attachments/assets/77cd167c-b096-48d4-a659-73d477d20780)

### Charistaristics

DC motors are controlled via the power supplied to them. More specifically, there are 2 properties we can influence: Voltage and Current. Each motor is rated to a set maximum of voltage and current, so changing these allows us to make the motor motor differentlly.

FRC motors are rated for 12V voltage. When supplied with such voltage they rotate at the maximum possible speed. When supplied with 6V (50% of 12) they rotate at 50% of the maximum possible speed, and so on. So by controlling this voltage supply we determine the speed of rotation. It should be noted that this is a linear relationship between voltage and speed. By controlling the polarity of the electrical circuit (switching between positive and negative wires, to basically reverse the flow of current) we control the rotation direction. 

When freely rotating (i.e. rotating without something interrupting the motion), a DC motor will draw minimal current and apply minimal torque (minimal = close to 0 as possible). However, as soon as something is resisting the motion (could be friction, gravity) the motor will start drawing more current and thus will apply more torque. This increases until a motor reaches its _stall_ state, where it is unable to overcome the counteracting force. At this state, it draws the maximum current (can reach up to 130A, depending on the motor) and applies the maximum possible torque.

Due to [Ohm's law](https://en.wikipedia.org/wiki/Ohm%27s_law), we know that there is a relationship between Voltage and Current. As such we can understand that If we supply only a 6V voltage, we will have a proportional limit on the amount of current the motor may draw. Thus, by controlling the voltage, we basically control the maximum speed (when freely rotating) and maximum torque (when stalled).

### Motor Controllers

Motor controllers are electronic devices which take the rule of controlling the motors. At their base, they are electronics circuits which allow control of the voltage applied to a motor. Though most modern motor controllers are capable of so much more.

![talonsrx](https://github.com/user-attachments/assets/88cef2dc-4e4a-4e69-b025-caf922a3314d)

For FRC use, they are essential, as controlling the power supply and behaviour of a motor requires a specialized circuit per motor, thus, our robot computer cannot perform this job itself.

> [!NOTE]
> This document will not perform a deep dive into all the capabilities of modern motor controllers.

At its core, every motor controller provides the capability to control a motor's speed/power and direction of rotation. This is usually represented in _PercentVBus_ (Percent Voltage Bus) values. This values (-1 -> 1) indicate
1. The amount of voltage supplied in percentage of the available battery voltage. At maximum charge, the battery can supply 12V voltage, but this decreases as it is used. So value `1` indicates $100%$ voltage; at maximum charge this will mean 12V, but may mean 10V if the battery voltage has dropped. This percentage is indicated by the absolute value of the number between 0 to 1 (`0.5` = 50%)
2. The direction of rotation, indicated by the sign: + = clockwise, - = counter-clockwise.

### DC Motors Used in FRC

The following are some of the motors currently use in FRC

Name | Image | Manufacturer | Type | Motor Controller to Use | Is Motor Controller Integrated?
------|------|--------------|------|----------|--------
CIM | ![cim](https://github.com/user-attachments/assets/836f685a-71db-4661-a21b-3ab4dc2597c1) | VEX | Brushed | Any | No
NEO 1.1 | ![neo 1.1](https://github.com/user-attachments/assets/7dd6e612-4f04-427c-bb1a-5b4422e5ebf7) | REV | Brushless | SparkMax | No
NEO 500 | ![neo 500](https://github.com/user-attachments/assets/c2d17baa-9d4e-437c-a19c-4e46a7ec6538) | REV | Brushless | SparkMax | No
NEO Vortex | ![neo vortex](https://github.com/user-attachments/assets/f138cced-2e52-451c-921a-40504765f5c1) | REV | Brushless | SparkMax | No
Falcon 500 | ![falcon 500](https://github.com/user-attachments/assets/358d1c18-8c02-471c-942a-9a6a5428b047) | VEX/CTRE | Brushless | TalonFX | Yes
Kraken | ![kraken](https://github.com/user-attachments/assets/489b8de1-4880-417d-9797-7876897e46e4) | VEX/CTRE | Brushless | TalonFX | Yes

As can been seen, FRC made a big move towards brushless motors in recent years. This makes sense, as brushless motors have several advantages over brushed; such as: higher torque, less maintanance requirements and longer lifespan.

### Gears and Gear Box
