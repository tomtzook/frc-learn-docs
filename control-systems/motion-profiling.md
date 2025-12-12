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

### Generating a Profile

Generating _profiles_ requires the user to provide specifications for the wanted profile. This includes _max velocity_ and _max acceleration_, but also the wanted motion _type_ (_shape_). This _shape_ is a quick way to provide characteristics and behaviour for how the _profile_ should be generated. There is no inheritently better type, they each have advantages and disadvantages. They are typically named around the shape the make on a _velocity graph_.

When provided with specification, we can produce both wanted _position_ and _velocity_ for each given moment in time. These can then be followed by the robot. 

_Profiles_ may be generated ahead of time, or during execution. This is usually dependent on the complexity of the _profile_, and whether the specifications are known ahead of time. Complex _profiles_ are usually generated ahead of time, due to the amount of work needed to make them, which may slow down execution. But simple _profiles_ can be easily created during use.

Let's look at different _profile_ shapes to understand how to generate them.

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

A trapezoidal _profile_ divides the motion into three parts of motion: _acceleration_, _cruise_ and _deceleration_. The system will accelerate until a designated maximum velocity is reached and will then stay on that velocity for awhile, and then start decelerating until stopping at the end. This gives the _velocity_ its _trapezoid_ shape.

<p align="center">
  <img width="505" height="367" alt="image" src="https://github.com/user-attachments/assets/f5c56faa-fb7c-4192-86c5-b08708294b93" />
</p>

The _acceleration_ and _deceleration_ phases are of the same length and acceleration values, and the remaining time is spent in constant velocity. This makes the _profile_, simple, but more flexible than a _triangle_ one. Thanks to the _cruise_ phase, even if the _max velocity_ is reached, the profile still allows us to continue moving, reaching longer distances.

Let's declare the acceleration used as $A$, the maximum velocity as $V_{max}$ and our target position as $X_f$, using physics formulas for linear motion we can calculate the expected states. Let's break it down into the three phases.

For phase 1 (_acceleration_) we start with $V_0 = 0, X_0 = 0$. We can describe each position and velocity in the phase as a function of time:

$$ V = V_0 + a * t $$

$$ V = A * t $$

$$ X = X_0 + V_0 * t + 0.5 * a * t^2 $$

$$ X = 0.5 * A * t^2 $$

We describe the time it takes for this phase with

$$ T = \frac{V_{max}}{A} $$

For phase 2 (_cruise_) we start with $V_0 = V_{max}, X_0 = X_{1}$, where $X_1$ is the distance we passed in the first phase. Because this phase is with a constant velocity, our forumlas are simplified. We can agin describe each position and velocity as a function of time, with our start time for the phase being $0$:

$$ X = X_0 + V_0 * t $$

$$ X = X_1 + V_{max} * t $$

For phase 3 (_deceleration_) we start with $V_0 = V_{max}, X_0 = X_{2}$ where $X_{2}$ is the position we ended phase 2 with. We can agin describe each position and velocity as a function of time, with our start time for the phase being $0$:

$$ V = V_0 + a * t $$

$$ V = V_{max} - A * t $$

$$ X = X_0 + V_0 * t + 0.5 * a * t^2 $$

$$ X = X_{2} + V_{max} * t - 0.5 * A * t^2 $$

We can declare the distance passed $D$ as the sum of distances passed in each phase, while these distances will be calculated as

$$ D_1 = 0.5 * A * T^2 $$

$$ D_2 = V_{max} * T_2 $$

$$ D_3 = V_{max} * T - 0.5 * A * T^2 $$ 

$$ D = D_1 + D_2 + D_3 $$

$T_2$ is the time we spend in the second phase, and may or may not be the same time as the other phases. There is nothing dictating it needs to be.

```
// Provide the wanted distance, wanted maximum velocity, maximum acceleration for the profile and the timestamp.
// Get the position and velocity of the profile at that timestamp.
calcProfile(distance, maxVelocity, maxAcceleration, currentTime):
  accelerationTime = maxVelocity / maxAcceleration // the time needed to reach max velocity (T)
  distancePassedInAcceleration = 0.5 * maxAcceleration * accelerationTime * accelerationTime // D1 and D3 values
  distancePassedDuringCruise = distance - 2 * distancePassedInAcceleration // D2
  cruiseTime = distancePassedDuringCruise / maxVelocity // T2

  if currentTime <= accelerationTime:
    // we are at phase 1
    position = 0.5 * maxAcceleration * currentTime * currentTime
    velocity = maxAcceleration * currentTime
  else if currentTime <= (accelerationTime + cruiseTime):
    // we are at phase 2
    currentTime -= accelerationTime
    position = distancePassedInAcceleration + maxVelocity * currentTime
    velocity = maxVelocity
  else:
    // we are at phase 3
    currentTime -= accelerationTime - cruiseTime
    position = (distancePassedInAcceleration + distancePassedDuringCruise) + maxVelocity * currentTime - 0.5 * maxAcceleration * currentTime * currentTime
    velocity = maxVelocity - maxAcceleration * currentTime    
```

### Following a Profile

Once we are presented with a _profile_, no matter which or how it was generated, we must make the system follow it, otherwise it is pointless. We can expect two outputs from a _profile_: wanted _position_ and _velocity_; While there is typically two basic inputs: our _target position_ and the current timestamp. A such, we can consider a _profile_ generally as two functions $P(D, t)$ which provides us with the _position_ and $V(D, t)$ which provides us with the _velocity_, where $D$ is the _target position_ and $t$ is the timestamp.

It is important to understand that we still need a _control loop_, which will ensure the system follows the output _position_ and _velocity_ from the _profile_. So following a _profile_ is a practice of taking the outputs from the _profile_ and feeding it into a calibrated _control loop_. We also need a way to follow the time, which the _profile_ is based on.

We can use _PID_ and _FF_ together as our _control loop_. _PID_ can be responsible for following the _position_, while _Feed Forward_ can allow us to follow the _velocity_. As such, a pseudu code for following could look like this:

```
profile = createProfile(targetPosition, maxVelocity, maxAcceleration)
startTime = getClockTime()
while (startTime <= profile.endTime):
  currentTime = getClockTime() - startTime
  setPoint = profile.get(currentTime)

  pidOutput = pid.calculate(getCurrentPosition(), setPoint.position)
  ffOutput = ff.calculate(getCurrentVelocity(), setPoint.velocity)

  system.set(pidOutput + ffOutput);
}
```

There is one problem though. If only at time _t_, do we tell the system to go to the _position_ and _velocity_ required for time _t_, we will be delayed in our profile following. Consider that it takes time for a _control loop_ to get a system to a state. So, the system will end up falling behind the profile in this case. To solve this, we typically look ahead in the _profile_. Instead of working with the current time _t_, we will always ask the _profile_ for the next time's state (`t+1`) .

We can modify the pseudu code, so that `currentTime` is bumped forward, say
```
nextTimestampDiff = 20ms
currentTime = getClockTime() - startTime + nextTimestampDiff
```

As long as the _control loop_ is able to keep up with the required _position_ and _velocity_, the system will follow the _profile_. Because the system is following the _position_ and _velocity_ specified, the _acceleration_ will also be as specified by the _profile_, despite not being directly controlled by the _control loop_. This should be expected since the _profile_ used physics to calculate each set point, and following these set points, inheritenly requires that the system follows the same physics.

### Using with WPILib

_WPILib_ provides us with generation for a _trapezoid profile_ in the form of the `TrapezoidProfile` class. The following `Command` shows an example of using this to control a system. We'll use _WPI_'s _PID_ and _FF_ controllers for our _control loop_, but one can use other's, like a motor controller's integrated loops.

```java
class TestCommand extends Command {

  private static final double KP = ...;
  private static final double KI = ...;
  private static final double KD = ...;
  private static final double KS = ...;
  private static final double KV = ...;
  private static final double KA = ...;
  private static final double PROFILE_MAX_VELOCITY = ...;
  private static final double PROFILE_MAX_ACCELERATION = ...;
  private static final double POSITION_MARGIN = ...;
  private static final double VELOCITY_MARGIN = ...;

  private final TestSystem system;

  private final PIDController pidController;
  private final SimpleMotorFeedForward ffController;

  private final TrapezoidProfile profile;
  private final TrapezoidProfile.State profileGoal;
  private TrapezoidProfile.State profileSetPoint;

  public TestCommand(TestSystem system, double targetPosition) {
    this.system = system;
    this.pidController = new PIDController(KP, KI, KD);
    this.ffController = new SimpleMotorController(KS, KV, KA);
    this.profile = new TrapezoidProfile(new TrapezoidProfile.Constraints(PROFILE_MAX_VELOCITY, PROFILE_MAX_ACCELERATION));
    this.profileGoal = new TrapezoidProfile.State(targetPosition, 0);

    addRequirements(system);
  }

  @Override
  public void initialize() {
    pidController.reset();

    // configure our current position, velocity
    profileSetPoint = new TrapezoidProfile.State(system.getPosition(), system.getVelocity());
  }

  @Override
  public void execute() {
    // as the profile for the next set point, specifying that 20ms have passed.
    // profileSetPoint should be the last set point we were at, and profileGoal is where we want to go
    profileSetPoint = profile.calculate(0.02, profileSetPoint, profileGoal);

    double pidOut = pidController.calculate(system.getPosition(), profileSetPoint.position);
    double ffOut = ffController.calculateWithVelocities(system.getVelocity(), profileSetPoint.velocity);
    system.set(pidOut + ffOut);
  }

  @Override
  public void end(boolean interrupted) {
    system.stop();
  }

  @Override
  public boolean isFinished() {
    // here we need to decide when to stop. We could wait until the profile ends, but it is more consistent to just
    // check if we are where we want to be.
    double currentPosition = system.getPosition();
    double currentVelocity = system.getVelocity();
    return Math.abs(profileGoal.position - currentPosition) < POSITION_MARGIN && Math.abs(profileGoal.velocity - currentVelocity) < VELOCITY_MARGIN;
  }
}
```

### Using in-built Controller Capabilities

Both **CTRE** and **REV** motor controllers now come with built-in motion profiling capabilities. This can be found in controllers like _TalonFX_, _SparkMax_ and _SparkFlex_. This feature can be used with ease instead of using the _WPI_ implementation.

#### CTRE

**CTRE** provides their _MotionMagic_ control mode for motion profiling. Most of the changes need to be made in the subsystem to configure the motor appropriately and allow use of this control mode.

```java
class TestSystem extends SubsystemBase {

  private static final int MOTOR_ID = ...;
  private static final double GEAR_RATIO = ...;
  private static final double KP = ...;
  private static final double KI = ...;
  private static final double KD = ...;
  private static final double PROFILE_MAX_VELOCITY = ...;
  private static final double PROFILE_MAX_ACCELERATION = ...;
  private static final double MOTOR_ROTATIONS_TO_POSITION = ...;
  private static final double POSITION_MARGIN = ...;
  private static final double VELOCITY_MARGIN = ...;

  private final TalonFX motor;

  private final StatusSignal<Angle> positionSignal;
  private final StatusSignal<AngularVelocity> velocitySignal;

  private final MotionMagicDutyCycle motorMagicOut = new MotionMagicDutyCycle(0);
  private final NeutralOut motorNeutral = new NeutralOut();

  public TestSystem() {
    motor = new TalonFX(MOTOR_ID);

    // we need to configure PID, FF and MotionMagic.
    // remember that we need to use the encoder measurement units.
    TalonFXConfiguration talonConfiguration = new TalonFXConfiguration();
    talonConfiguration.Feedback.SensorToMechanismRatio = GEAR_RATIO;
    talonConfiguration.Slot0.kP = KP;
    talonConfiguration.Slot0.kI = KI;
    talonConfiguration.Slot0.kD = KD;
    talonConfiguration.MotionMagic.MotionMagicCruiseVelocity = PROFILE_MAX_VELOCITY / MOTOR_ROTATIONS_TO_POSITION;
    talonConfiguration.MotionMagic.MotionMagicAcceleration = PROFILE_MAX_ACCELERATION / MOTOR_ROTATIONS_TO_POSITION;
    motor.getConfigurator().apply(talonConfiguration);

    positionSignal = motor.getPosition();
    velocitySignal = motor.getVelocity();
  }

  public double getPosition() {
    return positionSignal.getValue().in(Units.Rotations) * MOTOR_ROTATIONS_TO_POSITION;
  }

  public double getVelocity() {
    return velocitySignal.getValue().in(Units.RotationsPerSecond) * MOTOR_ROTATIONS_TO_POSITION;
  }

  public boolean isAtTarget(double targetPosition, double targetVelocity) {
    return Math.abs(targetPosition - getPosition()) < POSITION_MARGIN && Math.abs(targetVelocity - getVelocity()) < VELOCITY_MARGIN;
  }

  public void setPosition(double targetPosition) {
      // run motion magic, only needs to be done once, initialize
      motorMagicOut.Position = targetPosition / MOTOR_ROTATIONS_TO_POSITION;
      motorMagicOut.Slot = 0;
      motor.setControl(motorMagicOut);
  }

  public void stop() {
    motor.setControl(motorNeutral);
  }

  @Override
  public void periodic() {
    BaseStatusSignal.refreshAll(positionSignal, velocitySignal);
  }
}

class TestCommand extends Command {

  private final TestSystem system;
  private final double targetPosition;

  public TestCommand(TestSystem system, double targetPosition) {
    this.system = system;
    this.targetPosition = targetPosition;

    addRequirements(system);
  }


  @Override
  public void initialize() {
    system.setPosition(targetPosition);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    system.stop();
  }

  @Override
  public boolean isFinished() {
    return system.isAtTarget(targetPosition, 0);
  }
}
```

#### REV

**REV** actually provides two forms of _motion profiling_: the **old** _smart motion_ and the **new** _max motion_. We would prefer to use the new _max motion_, since it is
better (they implemented it differently). We'll do most of the work in the subsystem to configure and run it.

```java
class TestSystem extends SubsystemBase {

  private static final int MOTOR_ID = ...;
  private static final double GEAR_RATIO = ...;
  private static final double KP = ...;
  private static final double KI = ...;
  private static final double KD = ...;
  private static final double PROFILE_MAX_VELOCITY = ...;
  private static final double PROFILE_MAX_ACCELERATION = ...;
  private static final double MOTOR_ROTATIONS_TO_POSITION = ...;
  private static final double POSITION_MARGIN = ...;
  private static final double VELOCITY_MARGIN = ...;

  private final SparkMax motor;

  private final RelativeEncoder encoder;
  private final SparkClosedLoopController closedLoopController;

  public TestSystem() {
    motor = new SparkMax(MOTOR_ID, SparkLowLevel.MotorType.kBrushless);

    // we need to configure PID, FF and MotionMagic.
    // remember that we need to use the encoder measurement units.
    SparkMaxConfig config = new SparkMaxConfig();
    config.encoder.positionConversionFactor(1 / GEAR_RATIO)
                .velocityConversionFactor(1 / GEAR_RATIO);
    config.closedLoop.p(KP);
    config.closedLoop.i(KI);
    config.closedLoop.d(KD);
    config.closedLoop.feedbackSensor(ClosedLoopConfig.FeedbackSensor.kPrimaryEncoder);
    config.closedLoop.maxMotion.positionMode(MAXMotionConfig.MAXMotionPositionMode.kMAXMotionTrapezoidal);
    config.closedLoop.maxMotion.maxVelocity(PROFILE_MAX_VELOCITY);
    config.closedLoop.maxMotion.maxAcceleration(PROFILE_MAX_ACCELERATION);
    motor.configure(config, SparkBase.ResetMode.kNoResetSafeParameters, SparkBase.PersistMode.kNoPersistParameters);

    encoder = motor.getEncoder();
    closedLoopController = motor.getClosedLoopController();
    ffController = new SimpleMotorController(KS, KV, KA);
  }

  public double getPosition() {
    return encoder.getPosition() * MOTOR_ROTATIONS_TO_POSITION;
  }

  public double getVelocity() {
    return encoder.getVelocity() / 60 * MOTOR_ROTATIONS_TO_POSITION;
  }

  public boolean isAtTarget(double targetPosition, double targetVelocity) {
    return Math.abs(targetPosition - getPosition()) < POSITION_MARGIN && Math.abs(targetVelocity - getVelocity()) < VELOCITY_MARGIN;
  }

  public void setPosition(double targetPosition) {
      // run smart motion, only needs to be done once, initialize
      pidController.setReference(
            targetPosition / MOTOR_ROTATIONS_TO_POSITION,
            SparkBase.ControlType.kMAXMotionPositionControl,
            ClosedLoopSlot.kSlot0);
  }

  public void stop() {
    motor.stop();
  }
}

class TestCommand extends Command {

  private final TestSystem system;
  private final double targetPosition;

  public TestCommand(TestSystem system, double targetPosition) {
    this.system = system;
    this.targetPosition = targetPosition;

    addRequirements(system);
  }


  @Override
  public void initialize() {
    system.setPosition(targetPosition);
  }

  @Override
  public void execute() {}

  @Override
  public void end(boolean interrupted) {
    system.stop();
  }

  @Override
  public boolean isFinished() {
    return system.isAtTarget(targetPosition, 0);
  }
}
```
