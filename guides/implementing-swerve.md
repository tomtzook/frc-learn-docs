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

### Swerve Module


> mention about having to orient the wheel quickly or they will have a noticeble effect on the motion

#### Zero Angles

#### Optimizations
