
The Swerve drive is an omnidirectional drive chassis based on 4 swerve modules. 
Each module is composed of two motors, a wheel and a bunch of sensors. 
One motor rotates the wheel forward and backwards and the second motor rotates the wheel assembly around its Z axis, 
positioning the wheel such that it can move in different directions, 
thus making the entire drive omnidirectional - by positioning the wheels at different orientations the 
chassis can achieve motion along the Y and X axes as well as rotating the entire chassis along its Z axis.

![image](https://github.com/user-attachments/assets/05172e6e-e1ca-460a-8763-1a2176766a51)

![image](https://github.com/user-attachments/assets/c22bdfc6-d9f3-4659-90fd-5156cded1b28)

Due to the omnidirectional motion of the drive, we can describe a wanted motion in 3 parts, which we will refer to as the motion vector:
- Y axis motion (forwards/backwards)
- X axis motion (rightwords/leftwords)
- Rotational motion (rotation around the drive Z axis)

Our code will have to take this motion vector and convert it into instructions for each module, where each module has 
two degrees of motion: spin the wheel to move towards a direction and rotate the wheel to a different direction.

For example, given the motion vector `(1, 0, 0)` our code will have to position the wheels of the modules forward and 
rotate the wheels. Given the motion vector `(0, 1, 0)` our code will position the wheels at a 45 degree angle similarly 
to how a Mecanum drive operates and rotate the wheels to move to the right.

The swerve algorithm is quite complicated, however, WPILib provides us with a built-in implementation which calculates 
module positioning and speeds so we will use it to run our swerve drive.

The swerve module we use is [MK4I](https://www.swervedrivespecialties.com/products/mk4i-swerve-module) module with 
_NEO 1.1_ motors, _CANcoder_ absolute encoders _L2_ gear ratio (which is a `6.75 : 1` ratio) and `2 inch` (`0.0508 m`) wheels.

## CH 1: The Swerve Module

We’ll start off by implementing the `SwerveModule` class to represent a wheel module composing the swerve.

The module contains two motors:
- The drive motor: moving forward and backwards
- The steering motor: rotates the drive wheel

We’ll create a class called `SwerveModule`, and fill it with the necessary parts to control the module.

### Part 1: Components of the Swerve Module

As mentioned before, the class should contain two _Spark Max_ (`CANSparkMax` class) controllers (drive and steer). 
Each controller has a relative encoder attached to it, which will be used to track the motion of the motors.

To control the module, we’ll use two PID controllers - one for drive and one for steering. However, we won’t use the `PIDController` from 
_WPILib_, we’ll use the _Spark Max_ integrated PID controller. You see, CAN-based motor controllers are smart, that is, they offer more than 
just controlling voltage flow to the motor. Thanks to the flexibility of CAN-bus, we can tell the motor controllers to do so much more.
One of the options is to configure and use an integrated PID controller. Why? You may ask. Well, normally because it offers better response and 
control than non-integrated PID controllers. This is because it is integrated as part of the motor controller.

Before configuring the PID, we must first reset the motor controller settings by invoking `restoreToFactoryDefault`. 
This is to make sure no changes we make in the future to the controllers will affect the code.

To use the PID controllers, we’ll access them from the _Spark MAX_ via `getPIDController`. Then we can configure them via 
`setP`, `setI`, `setD`, `setFF`. Create constants for them all and configure the controller. We’ll tune them in future chapters.
One additional thing to remember is to call `setOutputRange(-1, 1)`, to limit the controller output to valid motor values. 
Remember to do this in the constructor.

One additional thing we need to do is to reset the encoders by calling `setPosition(0)`.

### Part 2: Basic Functionality

We now need to create several methods with which we’ll control the module.

Implement the following methods:
- `stop` method to stop the motion of both motors.
  - Call `stopMotor` method from the motor controllers
- `move(drive, rotation)` method to move the motors by raw motor values
  - Call `set` with the given values
  - We’ll use this method to test the motion of the module
- `getHeadingDegrees` method to return the heading of the drive wheel in degrees
  - Use the steering motor’s encoder
  - Remember to take into account the motors gearbox
  - Remember to convert it into degrees
- `getVelocityRpm` method to return the rotation velocity of the drive motor in RPM
  - Use the drive motor’s encoder
  - Remember to take into account the motor’s gearbox
 
### Part 3: Functionality for Swerve Use

Now we will move on to create the functionality which we will use to run the module from our swerve system.

This functionality will make use of our PID controllers to move the motors. We will create a method: `setDesiredState(SwerveModuleState desiredState)`. 
This method receives the `SwerveModuleState`. This class contains information about what to do with the module, i.e. how to move it. It will be provided by the swerve system after calculations.
`SwerveModuleState` contains two values that are relevant to us: 
- `desiredState.angle.getDegrees()`: the heading the steering motor should be facing
- `desiredState.speedMetersPerSecond`: the velocity at which the drive motor speed rotate

Let’s start with setting the drive motor. We’ll need first to convert the velocity given to us from _Meters Per Second_ in wheel velocity to _RPM_ in motors rotations (remember to take the gearbox into consideration).
Then, we can configure the controller to move at that velocity with the PID controller by calling `setReference(velocityRPM, CANSparkMax.ControlType.kVelocity)`. This function is available in the `SparkPIDController` class we retrieved from our motor controller.

Now the steering motor. We need to convert the given angle to a value which is recognized by the motor controller which is motor rotations (from wheel orientation).
And now set the value for the motor with `setReference(position, CANSparkMax.ControlType.kPosition)`.

## CH 2: Swerve Drive

With the swerve module ready, we can now use them to create our subsystem.
This subsystem will combine 4 swerve modules - one for each wheel and direct them how to move.

### Part 4: Starting with the Subsystem

Create a new subsystem class. It needs to have 4 swerve modules. Receive them as an array in the constructor and save them as an array. It will be easier to use it as an array.
The array should contain the modules by front-left, front-right, back-left, back-right

We’ll create several basic methods to prepare our subsystem. 
Implement the following methods:
- `stop` method to stop the motion of the swerve modules. Iterate over the modules and call `stop`
- `move(drive, rotation)` method to move the modules by raw motor values. Iterate over the modules and call their `move`, passing the values on.
- `setDesiredStates(SwerveModuleState[] states)` method to move the modules by states. Iterate over the module and states and call the module’s `setDesiredState`.

These are the basic methods to control the modules.

### Part 5: Kinematics of Swerve

To be able to calculate exactly what states each module should have when trying to move the swerve, we’ll use the `SwerveDriveKinematics`. 
This class has the math necessary to make those calculations.

Create a member of type `SwerveDriveKinematics`. Initialize it in the constructor by passing 4 `Translation2d` objects. 
These values indicate the position of the swerve modules on the robot as offsets from the center. By order, these need to be front-left, front-right, back-left, back-right. 
Each needs to have passed to it the X and Y offsets. Use `0.707 / 2.0` as offset for Y axis and `0.702 / 2.0` as offset for X axis. Pass them in order
- `offset`, `offset`
- `offset`, `-offset`
- `-offset`, `offset`
- `-offset`, `-offset`

With the kinematics object initialized, we can now use it to perform the right calculations. Create a new method, `drive(ChassisSpeeds speeds)`.
This method will receive speeds to move the swerve at and can be converted into actions for the modules using our kinematics object. 
Use `kinematics.toSwerveModuleStates` to get an array of module states to pass to the modules.
Call `SwerveDriveKinematics.destaturateWheelSpeeds` to limit the maximum speed of the drive system. Use `5` as the maximum speed.
Now call `setDesiredStates` method we created in the previous part to set the states to the modules.

Note that `ChassisSpeeds` is made up of speeds for _x_, _y_ and rotations speed. Unlike our normal coordinate system, the _x_ here indicates forward-backward motion.

## CH 3: Improving Modules

### Part 6: Zero Angles

If you remember, relative encoders start at “0” one powered on. So the steering angle is gonna be fucked up if if the wheels aren’t positioned 
at the correct start position. So we have 2 options: keep resetting the wheels manually to the correct start position, or, use and absolute encoder.

Absolute encoders are a type of encoder which can recall the correct position even after being turned off. This is done because the positions are 
actually hardware specified instead of software specified (like relative encoder). Imagine an actual set of markers on the encoder, each specifying 
a different position. When turned on, the encoder uses those markers to identify the position, thus being able to tell the true position even if 
turned off. So to solve our problem we have one installed on each module, and we’ll use them to tell the true angle at startup.

The encoder we use is called `CANCoder`, a dedicated FRC encoder. Receive it in the constructor of the module. In the constructor, 
query the `getAbsolutePosition` method to the get the real position. And use it to update the steering encoder `setPosition` by taking the 
angle and converting it to values used by the encoder ([cancoder example](https://github.com/tomtzook/frc-learn-docs/blob/master/robot-code-snippets.md#encoder-access-cancoder-absolute)).

We’re not done yet though. Depending on the way the CANCoder was installed, it will read angles differently. To make sure the encoders give us a 
position which is synchronized by all, we need to subtract the angle read by the absolute encoder by a constant. To figure out this constant,
power up the robot, print the absolute encoder reading to the dashboard, straighten all wheels to the "wanted" zero position and get the values
shown by each absolute encoders. These will be the zero angles.

### Part 7: Optimization

Depending on the actions we make with the swerve drive, we may find the we request to rotate the angle of the motor too much.
For example, for one second we may direct the swerve drive to move towards (1 y, 0 x, 0 rotation) and with a simple move by the driver 
we suddenly request to move towards (-1 y, 0 x, 0 rotation). On its face, it looks like we want to rotate the wheel 180 and move thataway. 
But this would require quite the rotation to make (it takes time to rotate the wheel). 
Instead we can actually rotate the drive wheel backwards and not rotate it.

Use `SwerveModuleState.optimize` with the desired state and the current position of the wheel to run this optimization. It will return a new state to use
instead of the old. Do this in the `SwerveModule.setDesiredStae` method.




