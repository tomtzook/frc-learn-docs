Robots, by definition, come in so many forms and designs. This is very expected, as the design of the robot is directly influenced by its purpose. 
The same thing can be said about frc robots, however this is up to a limit. As FRC robots actually have many basic properties and design which are shared. So despite having many different teams working on robots, we will see quite a few similarities between the robots.

In FRC, robots are designed following the requirements for the specific competition/game they are meant to play. So many (if not all) of the decisions about how the robot is composed and what it can do and how are dictated but the game. 
Moreover, FRC teams typically follow very specific design concepts and philosphies to robot building and as such, familiarizing ourselves with these will allow us to understand how the robot functions and how it can be controlled and operated.

## Systems

We typically view as a set of different systems, which may or may not operate together. Each system plays a very specific role in the robot, and they function together as a whole to provide a complete capability. 

### Chassis

The base of each robot is the chassis, sometimes referred to as a _drive system_. This system plays to parts:
- provides a base for all the robot systems, which are placed on it.
- provides ground mobility, i.e. allowing the robot to move around on the floor.

![drive base](https://github.com/user-attachments/assets/19fcfab1-a8ae-48b0-b408-85af5e451837)

The above is an example of a chassis. It is made up of a metal structure to hold the robot together and provide a structural support for placing things on top of it. The wheels allow this base to move around.

Most chassis designs are a rectangle metal structure, which provides a solid support for placing many things on it. As robots can weigh up to around 55 kg, so it must have a structure capable of supporting it. 
This structure may take several different variations, but the concept is the same.

The driving aspect of the chassis is also quite important, as an immobile robot is quite useless. There are several different configurations used in FRC to provide this mobility, each with its own advantages and disadvantages.

#### Tank Drive

Tank-Drive chassis is a relatively simplistic, but robust design. It is capable of supporting a lot of weight while still being quite mobile. 

![tank drive](https://github.com/user-attachments/assets/954c05b8-1414-4a50-8fbf-0f19fae5242f)

A tank drive uses two sets of independently controlled wheels - one per side of the robot (right and left), with anywhere between 2-4 wheels per side. At its base, this drive configuration only allows forward or backward motion of each side of the robot,
limiting manovering capabilites (although at the hands of a good driver, this is fine). The wheels themselves are basic high-friction wheels.

Controlling this drive usually involves controlling each side independently and moving them forward or backwards. Moving both sides forwards or backwards will move the entire chassis forward or backward. Moving each side in a different direction will rotate the robot
in place. It is also possible to combine different powers and directions to each side to provide slightly more specific turning rates (e.g. moving both sides forward, but providing more power to the left side will result in a slight turn to the right).

#### Omni

#### Mecanum

#### Swerve 

### Arms

Arms are flexible systems providing further reach to a ground-based chassis. They can come in many configurations with different advantages and disadvantages, but are generally used to reach upwards, or further ahead of the chassis. 

![arm 1](https://github.com/user-attachments/assets/0515d52a-cf6b-4b72-9c9a-098e6b47165e)

Arms are usually characterized by the amount of joints they have. Each joint provide an additional degree of freedom. Though 1-joint arms is most common, 2-joint arms might be occasitionally seen.

Each joint is powered by a motor attached to a shaft which holds the arm. Rotating this shaft will rotate the arm, raising it upwards or lowering it downwards.

![arm 2](https://github.com/user-attachments/assets/8427f938-1037-45ee-9cc5-a0f175ad61a4)

It is not uncommon to see an additional system or component attached to the end or front of the arm. This basically provides that system with a reach out of the chassis. It can be used on game pieces placed on top of things, or reach the ground better. Examples may include clamps to hold an item, or intake to collect items into the robot.

![arm 3](https://github.com/user-attachments/assets/3b700cf7-59bf-437c-85a0-17d69a28fe81)

Controlling an arm system typically involves controlling the joint arm as to move the arm into a specific position, usually indicated by the angle between the arm and the chassis. Different angles (positions) are used for different operations with the arm. Keeping the arm in place also requires some work, as gravity keeps working to lower it. 

### Elevators

Elevators provide both floor and high vertical reach. They can be used to lift objects from lower heights to higher heights, and vice versa. They typically require some proximity to what they are lifting (i.e. the robot must be very close to an object to lift it), but can handle quite a bit of weight.

![elevator 1](https://github.com/user-attachments/assets/cf7c34bc-2f83-4b96-82fe-2c0d23ad982b)

An elevator (similar to real life elevators) are composed of a shaft, which is empty and provides a space for vertical motion, and a carriage which is the actual elevator and moves up and down in a shaft. The motion is dependent on a motor which pulls the carriage up, or lowers it.

A typical design will use a motor which pulls on a rope (attached to the carriage) and thus raises the elevator. To lower it, the motor releases rope and gravity pulls the carriage down. Other designs exist however.

![elevator 2](https://github.com/user-attachments/assets/1cd5a110-4674-4cf7-8073-73d5f3a95ff7)

Most elevators will have some other mechanism in the carriage. This mechanism would allow the elevator to capture and hold some game piece.

### Shooters

Shooters mechanisms allow, basically, to _shoot_ a game piece out of the robot at range. This provides a robot with the capability to launch something to a distant target, typically a target at height. One could think of it as if shooting a hoop in basketball - trying to score the ball into the high basket. 

A few games in recent history did involve scoring a piece into a basket facsimile.

![shooter 1](https://github.com/user-attachments/assets/e8039838-d90d-43bf-9456-ffabf8c184ce)

Most shooters use a set of wheels, rotated at high speeds by motors. When a piece is fed into these wheels they push it out at speed, throwing it a certain distance and height.

![shooter 2](https://github.com/user-attachments/assets/0d77941e-b2bd-4655-8639-061ab3d120ed)

### Turrets

![turret 1](https://github.com/user-attachments/assets/1e71f49d-32ae-4009-a4f1-b1cafff0ea44)

![turret 2](https://github.com/user-attachments/assets/334e9a9c-8a11-4345-b050-91200e5b7dd9)


### Intake

![intake 1](https://github.com/user-attachments/assets/16053c25-ee95-41c7-96e9-582452ff88eb)

![intake 2](https://github.com/user-attachments/assets/5d3052af-024d-4b19-9b5e-1df53c487558)

## Motion 

For a robot, or any mechanism, to move and operate, there is a requirement for some device to create this motion. As the previous section may have illustrated - motors are a primary choice for FRC robots. They provide a controllable rotational motion, which may also be converted to linear motion. 

![cim motor](https://github.com/user-attachments/assets/16957807-3851-4153-a507-81379536ef44)


One can attach motors to wheels, drums, shafts and more. With the application of power to the motor, we receive a motion. By controlling this, we control the motion of the robot.
