Motors provide FRC robots with force and motion. They are the base for designing most systems and are pretty reliable and not to complicated to use.

To make it clear, this document does not aim to teach the inner workings of DC motors, but simplify introduce to some of the basic concepts.

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

The following are some of the motors currently use in FRC

Name | Image | Manufacturer | Type | Integrated Motor Controller
------|------|--------------|------|----------
CIM | ![cim](https://github.com/user-attachments/assets/836f685a-71db-4661-a21b-3ab4dc2597c1) | VEX | Brushed | No
NEO 1.1 | ![neo 1.1](https://github.com/user-attachments/assets/7dd6e612-4f04-427c-bb1a-5b4422e5ebf7) | REV | Brushless | No
NEO 500 | ![neo 500](https://github.com/user-attachments/assets/c2d17baa-9d4e-437c-a19c-4e46a7ec6538) | REV | Brushless | No
Falcon 500 | ![falcon 500](https://github.com/user-attachments/assets/358d1c18-8c02-471c-942a-9a6a5428b047) | VEX/CTRE | Brushless | Yes
Kraken | ![kraken](https://github.com/user-attachments/assets/489b8de1-4880-417d-9797-7876897e46e4) | VEX/CTRE | Brushless | Yes

As can been seen, FRC made a big move towards brushless motors in recent years. This makes sense, as brushless motors have several advantages over brushed; such as: higher torque, less maintanance requirements and longer lifespan.

## Controlling DC Motors

## Gears and Gear Box
