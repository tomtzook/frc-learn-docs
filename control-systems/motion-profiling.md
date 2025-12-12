When applying a _control loop_ to control a system (using _PID_ as an example), our loop will typically attempt to reach its _setpoint_ as fast as possible. But at their core, the output 
calculation will provide us with a desired output according to the function, which will be a great output in theory, but this pays little attention to what is physically possible by the system.

After all, implementations, like the one in _PID_, look at the system via the state, with absolutely no knowledge of the real 3D mechanics in play. This may not seem like a problem, and some times it really doesn't matter much.

But consider: our system starts at position 0 and we want to go to position 5. We start our _pid_ loop which gives an output of `1`. So this means we are going to order our motor to go from 0% to 100%. This is a problem. What essentially happens is that we force the motor to accelerate at potentially dangerous speed, and the system it operates will have to go through it as well. This will put high pressure on the components and could easily damage some of them. The same occurs when going from 100% to 0% (doubly so with _brake_ mode), with the system abruptly stopping and throwing the chassis around if it has enough force.

For the reasons above, it is typically advised to slowly ramp up or down the motor. Going from 0% to 10%, 20%, 30%, etc. This essentially complicates our _control loop_, but for good reason.

We can descirbe the wanted behaviour of the motor (position, velocity, acceleration) under these requirements as such:

<p align="center">
  <img width="486" height="455" alt="image" src="https://github.com/user-attachments/assets/96de8a06-0092-47bc-bed5-d686184b896f" />
</p>

- We start at 0 position, velocity and acceleration
- _Acceleration Phase_: Accelerate the motor until reaching maximum wanted velocity
- _Cruise Phase_: staying at the maximum velocity, contine moving
- _Deceleration Phase_: Decelerate the motor until stopping

This may seem like a completely normal motion, and it is. As controlled motion typically does look like this. It's worth noting that both the velocity and acceleration increase at a relatively steady pace, and have a designated maximum value. This prevents the system from exceeding its capabilities (assuming the maximum velocity and acceleration are within system tolerance).

Now, let's compare this to how a basic _PID_ controller will behave. I've set up a simple simulation involving a free wheel operating with _PID_ and recorded the graph.

<p align="center">
  <img width="1114" height="473" alt="image" src="https://github.com/user-attachments/assets/39cd8ed5-12ba-4516-8200-bd81a6d79dd6" />
</p>

The acceleration is marked in _Green_. Velocity in _Red_ and Position in _Blue_. 

Notice the insane magnitude of the acceleration at the start and how quickly it is reached. As this is a simulation, not damage is done, but on a real system... For the record, the acceleration peaks at around `2500` _Degrees per second per second_ and is reached in virtually an instance.

Now the same simulation, but with the appliance of controlled motion.

<p align="center">
  <img width="727" height="346" alt="image" src="https://github.com/user-attachments/assets/89d816ca-7496-4013-b248-7e57d7dd4eb3" />
</p>

Now the acceleration actually ramps up slowly instead of rising up at an insane jerk, taking around 1.2 secounds to reach its peak. The maximum acceleration is also different, because I have limited it in the motion parameters. This should help illustrate the advantage of using a more controlled motion.

> [!NOTE]
> The term _Jerk_ refers to the rate of change of the acceleration.

## Motion Profiling

The idea of creating a controlled motion can be performed via the process of _Motion Profiling_. In this process, the computer/robot or a person create a _Profile_ of the wanted motion, specifying the position, velocity and acceleration of the robot at a series of points in time. The robot must then follow these specifications (matching its position, velocity and acceleration to those specified in the _profile_) to acheive the wanted motion.

This allows us to create a very specific way for the system to perform movement, which includes limiting velocity/acceleration, or even more.

At its core though, _Motion Profiling_ relies on our ability to control the system's position and velocity (sometimes _acceleration_ too) with high accuracy, as something must make the robot follow the exact _profile_. This is typically done using _Feedback_ and _Feedforward_ controllers (like _PID_). Essentially, _Motion Profiling_ cannot exist on its own. So the idea is: instead of the _PID_ controller alone trying to reach our goal, the _Motion Profile_ will dictate _set points_ for it to control the motion.

A _motion profile_ would typically look something like this:
```json
{
  "maxVelocity": 20,      // Degrees per second
  "maxAcceleration": 10,  // Degrees per second per second
  "points": [
      {
          "time": 0,          // seconds
          "position": 0,      // Degrees
          "velocity": 0,      // Degrees per second
          "acceleration": 0   // Degrees per second per second
      },
      {
          "time": 0.5,
          "position": 1,
          "velocity": 2,
          "acceleration": 2
      },
      {
          "time": 1,
          "position": 2,
          "velocity": 3,
          "acceleration": 8
      },
      // ... more points
  ]
}
```

And so, when a code consumes this _profile_ it will read this information and seek to follow it by, at each moment in time specified, reaching the requested _position_, _velocity_ and _acceleration_ as best as possible. Failing to do so will cause the motion to drift from the wanted _profile_.

This divides _motion profiling_ into two distinct and important parts:
- generating a _profile_, based on requested settings
- following that _profile_

> [!NOTE]
> It is also quite common to see generation of the profile on the fly during execution of motion. In these
> algorithms, the _profile_ is only every generated for the next motion set point.

### Profile Shapes

Generating _profiles_ requires the user to provide specifications for the wanted profile. This includes _max velocity_ and _max acceleration_, but also the wanted motion _type_ (_shape_). This _shape_ is a quick way to provide characteristics and behaviour for how the _profile_ should be generated. There is no inheritently better type, they each have advantages and disadvantages. They are typically named around the shape the make on a _velocity graph_.

#### Triangle Profile

A triangular _profile_ divides the motion into two parts of motion: _acceleration_ and _deceleration_. The system will accelerate until a designated maximum velocity is reached and will then immediatly start decelerating until stopping at the end. This gives the _velocity_ its _triangular_ shape.

<p align="center">
  <img width="472" height="349" alt="image" src="https://github.com/user-attachments/assets/c459fcc4-0716-4a20-9efd-b16648a8f9e0" />
</p>

The _acceleration_ and _deceleration_ phases are of the same length and acceleration values, and no time is spent in constant velocity. This makes the _profile_, generally faster and makes sense for quicker actions. Because the geometry of a triangle is very simple, it makes it easy to calculate the profile points.

Let's declare the acceleration used as $A$, the maximum velocity as $V_{max}$ and our target position as $X_f$, using physics formulas for linear motion we can calculate the expected states. Let's break it down into the two phases.

For phase 1 (_acceleration_) we start with $V_0 = 0, X_0 = 0$. We can describe each position and velocity in the phase as a function of time:

$$ V = V_0 + a * t $$

$$ V = A * t $$

$$ X = X_0 + V_0 * t + 0.5 * a * t^2 $$

$$ X = 0.5 * A * t^2 $$

We describe the time it takes for this phase with

$$ T = \frac{V_{max}}{A} $$

Since both phases use the same acceleration value, we can say that they both take $T$ time, for a total of $2T$.

For phase 2 (_deceleration_) we start with $V_0 = V_{max}, X_0 = X_{e1}$ where $X_{e1}$ is the position we ended phase 1 with. We can agin describe each position and velocity as a function of time, with our start time for the phase being $0$:
 
$$ V = V_0 + a * t $$

$$ V = V_{max} - A * t $$

$$ X = X_0 + V_0 * t + 0.5 * a * t^2 $$

$$ X = X_{e1} + V_{max} * t - 0.5 * A * t^2 $$

With this in hand, we can feed timestamps into the functions to provide which position and velocity we should follow for the profile. A pseudo code, can look like this

```
// Provide the wanted maximum velocity, maximum acceleration for the profile and the timestamp.
// Get the position and velocity of the profile at that timestamp.
calcProfile(maxVelocity, maxAcceleration, currentTime):
  accelerationTime = maxVelocity / maxAcceleration // the time needed to reach max velocity (T)
  if currentTime <= accelerationTime:
    // we are at phase 1
    position = 0.5 * maxAcceleration * currentTime * currentTime
    velocity = maxAcceleration * currentTime
  else:
    // we are at phase 2
    currentTime = currentTime - accelerationTime // we want to calculate with time starting at phase start
    x0 = 0.5 * maxAcceleration * accelerationTime * accelerationTime // we need the position we ended phase 1 with
    position = x0 + maxVelocity * currentTime - 0.5 * maxAcceleration * currentTime * currentTime
    velocity = maxVelocity - maxAcceleration * decelCurrentTime
```

You might have noticed that we haven't really addressed the target position here. And this is true, we haven't addressed how far we want to go, so we may just miss it, or not get there at all. One one end, maybe the distance is too short to reach $V_{max}$ with the given acceleration $A$. On the other end, maybe the distance is too long to complete it in our triangle with the given acceleration and velocity, and we need some extra motion.

We can declare the distance passed $D$ as the sum of distances passed in each phase, while these distances will be calculated as

$$ D_1 = 0.5 * A * T^2 $$

$$ D_2 = V_{max} * T - 0.5 * A * T^2 $$ 

$$ D = D_1 + D_2 $$

$$ D = (0.5 * A * T^2) + (V_{max} * T) - (0.5 * A * T^2) $$

$$ D = V_{max} * T $$

So we have a way to tell how much distance our profile will pass. Recall that $T = \frac{V_{max}}{A}$, so

$$ D = V_{max} * \frac{V_{max}}{A} $$

$$ D = \frac{2 * V_{max}}{A} $$

So given a known acceleration and wanted distance to pass, we can decide what our $V_{max}$ will be

$$ V_{max} = \frac{D * A}{2} $$

#### Trapezoid Profile

#### S-Curve Profile

### Generating a Profile

### Following a Profile

### Using with WPILib

### Using in-built Controller Capabilities

#### CTRE

#### REV
