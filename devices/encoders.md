## Encoders

Encoder sensors measure the position and rotation of a shaft. They are quite reliable and versatile and we'll see them quite often in FRC robots because of that.

Optical encoders use light transmitter and receiver and a disk attached to the shaft, the light sensor detect the rotation of the disk on the shaft and uses it to measure the rotation. 
Magnetic encoders use magnets attached to the shaft and measure the changes in magnetic fields to measure the rotation.

### Absolute Encoders

Absolute encoders are constructed to measure the absolute positioning of the shaft. That is, each position in the shaft is assigned a unique value which the sensor can detect while on. 
So no matter the position, how many rotations, the sensor will report a specific position accuractly as the same position. Even if we turn off the sensor, move the shaft and turn it on again, it will
still be able to provide the actual position of the shaft. These sensors usually come with an hard-coded zero position, and provide an angle to each part of the shaft relative to this zero. This position
will remain consistent for the operation of the sensor.

![optical absolute encoder](https://github.com/user-attachments/assets/9e63560f-c4a6-440a-b3d3-09f1a34a01de)

Optical absolute encoders use a disk placed on the shaft. Each part of the disk is encoded a unique position by placing holes along the radius of the disk. Each position will have a different combination of holes,
thus indicating a unique position. 

![optical absolute encoder disk](https://github.com/user-attachments/assets/46b85a6e-5c58-4da5-8120-7289cc5a4fec)

Along the radius of the disk we have a number of places (how many changes between encoders). Each of these places can either be a hole, or material (i.e. not a hole). We have leds above each of these places
and light detectors on the other side of the disk parallel to the leds. For each position, if the place has a hole, the sensor will see the led, if it doesn't, then the sensor won't see the led. This means that
each place is either _seen_ or _not_, so `1` or `0`. If we have 4 places along the radius, then we have $2^4$ combinations. We can divide those combinations around the disk, such that each position has a unique
combination. Then, by reading the light sensors, we can deduce the position according to the combination. 

If there are 16 combinations, they have to divided along the entire disk, so each combination will cover ${360 \over 16} = 22.5 \ degrees$. This is called the resolution, which determines how precise the encoder is.
The more combinations we have, the higher the precision. This measure is usually marked in bits, with each bit being equal to a place on the disk, since each place is either `1`, or `0`, like bits. So a 10
bit resolution will be with 10 places, and thus with $2^{10} = 1024$ combination, and thus ${360 \over 1024} = 0.3515625 \ degrees$. The image above is one of 3 bit resolution, as there are 3 places along the radius.

When we select a sensor, we will need to take this into consideration. This is important, because if our precision is too low, then we won't see changes in positining. For resolution of 22.5 degrees, we will only
see a change in position every 22.5 degrees from 0. So we will know when we are at 0, 22.5, 45, 67.5 and so on. 

![optical absolute encoder gif](https://www.akm.com/content/dam/site/akm/products/rotation-angle-sensor/tutorial/r1000-mv-encoder-base-fig3_6.gif)

We would typically use rotations or degrees as our measurement units when working with such encoders. Do note that due to the nature of the encoder, its value wraps around. In rotations the value will be limited to 0->1 (and 0->360 in degrees). 

We would normally use them to track exact position of a rotational system, like arm or turret (system where motion is limited to 0->360 at max).

These sensors usually communicate via parallel, serial or PWM. We will use them when we need to know the specific positioning of the shaft. Like for an arm rotating around a shaft. Magnetic ones operate
in a similar concept to the optical, but instead of a disk and light, it uses magnets and magnetic sensors. 

### Relative

Relative encoders  detect changes/motion of the shaft. They do not provide absolute positioning, but rather report on changes in position. That is, each time the shaft rotates a set
amount of degrees, the sensor will tell us of this. It is relative, because the sensor doesn't assign unique position to each part of the shaft, but rather just counts how it rotates. So when the sensor is turned on, the position will always be zero and counted from there, no matter how the shaft is oriented, thus the position is relative to how the shaft was when the sensor started.

![optical relative encoder](https://github.com/user-attachments/assets/419248ec-c657-407a-bb0f-8e01eae346b2)

Optical relative encoders use a disk placed on the shaft. Along the disk there are holes spaced evenly at the edge. A led is placed on one side of the holes and a light sensor on the other end. Each time the led is infront of an hole, the encoder recognizes a change in the rotation of the shaft. As the shaft rotates, the holes moves and such at one moment the led is infront of a hole, and later not and then infront of another hole. As such, we can count the amount of holes the sensor has seen, this will be equal to the change in position of the shaft. 

The amount of holes in the disk affects the resolution of the sensor. The more holes we have, the more precise the sensor is. Because the holes are evenly spaced, each hole seen is equal to a specific change in position. So having 10 holes, gives us a change of ${360 \over 10} = 36 \ degrees$ per hole. Resolution of such sensor is then measure by this amount. It is important to know the resolution of such sensors, as they determine how small of a change in motion we can see.

Typically, they communicate using pulses. When the light sensor sees the led, the pulse is turned to high, and when it no longer sees the led, the pulse is turned to LOW. So each pulse starts when the led is infront of the hole and ends when it leaves it. If we count the pulses, we can basically count the amount of holes we've seen and thus the changes in position. But we can also measure the time between pulses to determine the velocity of the motion. Because the pulses are linked to the motion, the shorter the pulses and the closer they are together, the faster the rotation. So measuring the time between them, or their length, will be equal to the velocity.

![channel A pulses](https://github.com/user-attachments/assets/52872c68-0bff-4195-ba1b-44b5ed53bf26)

The resolution of such encoders is measure in _Pulses Per Revolution_ (PPR), which is the amount of pulses (or holes) in a full revolution of the shaft. It is sometimes also measured in _Counts Per Revolution_. One count is equal to a change of voltage on the wire, so for one pulse, the count is 2. So for a single channel encoder, the counts per revolutions are 2 times the pulses per revolution. By measuring position changes on counts and not pulses, we get twice as much resolution because we see the start of the pulse and the end of the pulse.

Relative sensors, though, can be improved further. Frequently, they come in the shape of _Quadrature encoding_. Quadrature enoding uses two sets of holes on the disk. The outer holes are channel A and the inner are channel B. The holes are places not exactly below each other, but rather the inner holes are half a whole to the side. That way, when we move in one direction we'll see hole A first and then hole B. And in the other direction, we'll see hole B before hole A. So this way, we have also direction information. Both channel A and channel B function similarly by sending pulses when their respective sensor sees the leds. 

![quadrature disks](https://github.com/user-attachments/assets/6e24ce48-c8c2-405c-b064-1e72366a9f5f)

![quadrature pulses and direction](https://github.com/user-attachments/assets/7d559c04-4f6d-4572-8eb8-b885bbaa3bc6)

PPR for quadrature encoders measure the amount of pulses on one channel. So the PPR remains the same as with a single channel encoder. The counts per revolutions however, is increased, as there are 4 changes: 2 on channel A and 2 on channel B. So the counts is 4 times the pulses. If we use counts per revolutions measuring, we get 4 times the resolution.

Thanks to these techniques, relative encoders provide an excellent way to measure not only changes in position, but also speed of rotation and direction. We can also easily measure the amounts of rotations made, which isn't as simple to do with absolute encoders. If the shaft is attached to wheels and such, we get a great way to measure linear motion as well.

To work with such sensors, we count the amount of pulses on the channels. If the pulse comes on A before B, we increase the count by 1, if it comes on B before A, we decrease the count by 1. So when we rotate clockwise we get an increasing count, when rotating counter-clockwise, we get a decrease count. This gives us a direction along this count information. We can then convert this count into actual positioning and speed.

Because such encoders are quite versatile we may use them to measure the position of a shaft (in rotations/degrees), the linear distance passed by a wheel on the floor (in meters) or something else. This all depends on a specific use case. Do note that the value from such encoders does not wrap around and will continue to count as long as there is motion (only limited by memory size).

We would normally use them to measure velocity for shooters and wheels, or measure linear distance passed by wheeled systems. So they can be placed on drive systems, elevators collectors and shooters. However, they are also used for for arms (to measure position/angle) and other systems. 

### On Gears

When we place encoders, we should also take into consideration gear boxes. Gear boxes are mechanical devices we are used to either increase a motor's torque, or its speed. It does this by using toothed gears. If we connect the motor to a smaller gear, and the gear is tied to a larger one and we place the shaft on the larger gear, we get an increase in torque, since the radius of the rotating object increases the torque of the rotation. If we do the opposite, we get an increase of speed. 

![gears](https://upload.wikimedia.org/wikipedia/commons/thumb/9/9b/Animated_two_spur_gears_1_2.gif/330px-Animated_two_spur_gears_1_2.gif)

Gears are measured by the teeth ratio between the _driver_ gear (where the motor is connected) to the _driven_ gear (where the shaft is connected). A ratio of `6 : 1` means that for every 6 rotations of the motor, the shaft is rotate 1 full rotations. We get slower speed, but more torque. Each full rotation of the motor is equal to a 1/6 rotation of the shaft.

If we have an encoder connected to the motor, which is then connected to a gear box, then the encoder measures the motion of the motor, but not the output shaft. To measure the shaft, we must make convert the output of the encoder to consider the gear box. For a `6 : 1` ratio, we can convert by dividing the encoder output by `6`. So if the encoder has sent 1024 pulses out of 1024 PPR, then the rotation of the shaft is ${1024 \over 6}$.  

Relative encoders function fine through gears, because they can count over a full rotation. Howerver, absolute encoders do not. Consider: when the encoder measure the rotation of the small gear, with absolute encoder it resets after a full rotation (as it is limited to counting in a circle, range of 0->360). So when connected through a gear, the absolute encoder will only count the revolution of the small gear, but it doesn't translate well to the big gear (which operates the mechanism). Placing the absolute encoder _after_ the gears will work though.

### On Conversions and Representations

When reading encoder measurement we typically read it in _counts_ or _rotations_. But this measurement may be converted to different units, allowing us to represent the position or velocity of the shaft in other ways. 

Relative encoders typically measure with _counts_ or _pulses_. While absolute encoders encode the position with a number out of a maximum (say _position_ `512` out of `1024` indicates a half rotation). 

$rotations = {pulses \over PPR} = {counts \over CPR}$.

$angleDegrees = rotations \times 360$.

$rotationsPostGear = {rotationsPreGear \over gearRatio}$ with $gearRatio = {preGearRotations \over 1 \ postGearRotation}$

Although encoders measure the rotation/position of a shaft, this motion actually other parts of the robot/mechanism. As such, we can convert the measurement from an encoder to measure different types of motion.

If we connect a wheel to a shaft measure by an encoder, we can measure the linear distance and velocity of the wheel. That we, we can measure how much distance the wheel has passed on the floor, something only possible with relative encoder because we need to be able to measure multiple rotations.

A wheel passes its circumeference on the floor through a full revolution, so that $distance = rotations \times wheelCircumeference = rotations \times (2 \times \pi \times wheelRadius)$. 

For linear velocity of the wheel $velocity = rotationsPerSecond \times wheelCircumeference$.

### Comparison

 |  | Absolute | Relative
 |--|----------|-----------
 | Power On Value | Actual Position | 0 |
 | Value Range (in rotations) | 0 -> 1 | Undefined, limited by memory |
 | Best For | Absolute Position (rotational) of Shaft connected Mechanism | Position (linear) of shaft connected Mechanism, Velocity (rotational, linear) of shaft connected Mechanism |
 | Gear Box | Problematic, doesn't translate through properly | No Problem, simple value conversion necessary |

## Common FRC Encoders

### Through Bore

<p align=center>
<img src="https://github.com/user-attachments/assets/d9d3c00c-7fa4-4059-b1df-ee9f9d0ef29a" width="360">
</p>

The _Through-Bore_ encoder is a magnetic encoder, which outputs both absolute and relative measurements. It basically combines both capabilities and thus has many uses. We will use it as absolute or relative (or both) depending on the system/use we have for it.

#### Operation Mode: Absolute

The device will report us with the position of the shaft. Persistent data between power cycles. The zero position is a fixed factory defined position and will remain the same no matter what. When the shaft is in this position, the sensor will report a position of `0`. When mounting the sensor we need to take this position into consideration so that the sensor reports zero for a position we want to be considered zero.

The resolution of the sensor is 10 bit, with values from 0 to 1024. This gives a precision of ${306 \over 1024} = 0.35 \ degrees$. 

The sensor uses a PWM output, with 1us pulse being 0 degrees and 1024us being 360 degrees. The frequency of output is `975.6 Hz`, which means that the pulse is sent every `1.025ms.` For a 20ms loop (like our periodics) we'll get 19.5 updates from the sensors for each run of our loop. We'll use `DutyCycleEncoder` to read this info. 
```java
public class Robot extends TimedRobot {
  
  private DutyCycleEncodr encoder;

  @Override
  public void robotInit() {
	// encoder connected to DIO pin 0
	encoder = new DutyCycleEncoder(0);
  }
  ...
  @Override
  public void teleopPeriodic() {
    	double posRotations = encoder.getAbsolutePosition(); // 0->1, in rotation units
	double posAngles = posRotations * 360.0;
  }
  ...
}
```

#### Operation Mode: Relative

The device will report us with motions of the shaft, but not its absolute position. The information provided is not persistent and will reset through power-cycles. The zero position for relative sensors is their power-up state. There is no absolute zero, it is just what state we are when we power up.

The sensor has 2048 Pulses Per Revolution. So if the sensor reads a full revolution of 360 degrees, it would have sent 2048 pulses. Of course, even after a full rotation, we'll continue getting pulses, so the count of pulses isn't limited to 2048. A single pulse is sent for a change of `360/2048 = 0.175` degrees. So we can detect such a small change.

To read the encoder in this mode, we need to connect the sensor to two digital input pins, one for channel A and another channel B. We can use the `Encoder` class to read from it. It is based on the `Counter`, which is capable of counting pulses and their length.
```java
public class Robot extends TimedRobot {
  
  private Encoder encoder;

  @Override
  public void robotInit() {
	// channel A to pin 0, channel B to pin 1
	encoder = new Encoder(0, 1);
	// configure the encoder's PPR data. We'll make it configured to returning degrees.
	// Say the PPR is 2048, so to get degrees, we need to configure how many degrees we get per pulse: 360/2048.
	// We can also configure it to return other converted data, like distance of wheel on the floor, just need to manipulate the calculation accordingly.
	encoder.setDistancePerPulse(360.0/2048.0);
  }
  ...
  @Override
  public void teleopPeriodic() {
    	double degrees = encoder.getDistance();
	double degreesPerSecond = encoder.getRate();
  }
  ...
}
```

### SRX Magnetic Encoder

<p align=center>
<img src="https://github.com/user-attachments/assets/e33f40c2-827e-44a5-9041-4148e35de848" width="360">
</p>

The SRX Magnetic encoder is a magnetic encoder capable of both absolute and relative measurements. The names comes from it being directly connactable to the Talon SRX motor controller. This allows the motor controller to actually use this sensor in its operation. 

Because the sensor is connected to the talon, we do not read the sensor data from the DIO pins of the RoboRIO, but rather we used CANBus messages to get the information. The TalonSRX will periodically send us this information and we can access it via the `WPI_TalonSRX` class we use to control the motor controller.

#### Operation Mode: Absolute

The device will report us with the position of the shaft. Persistent data between power cycles. The zero position is a fixed factory defined position and will remain the same no matter what. When the shaft is in this position, the sensor will report a position of `0`. When mounting the sensor we need to take this position into consideration so that the sensor reports zero for a position we want to be considered zero.

The resolution of the sensor is 12 bit, with values from 0 to 4096. This gives a precision of ${360 \over 4096} = 0.087 \ degrees$. This is the smallest change we can see.

To read the encoder in this mode, we need to connect the sensor to a _TalonSRX_ and read the sensor data from the motor controller instance in our code.
```java
public class Robot extends TimedRobot {

  private static final double PULSE_WIDTH_TO_POSITION = 1 / 4096.0;

  private WPI_TalonSRX motor;

  @Override
  public void robotInit() {
	motor = new WPI_TalonSRX(0);
  }
  ...
  @Override
  public void teleopPeriodic() {
	int pulseWidth = motor.getSensorCollection().getPulseWidthPosition(); // this is the raw count from the PWM
	double degrees = pulseWidth * PULSE_WIDTH_TO_POSITION * 360.0;
  }
  ...
}
```

#### Operation Mode: Relative

The device will report us with motions of the shaft, but not its absolute position. The information provided is not persistent and will reset through power-cycles. The zero position for relative sensors is their power-up state. There is no absolute zero, it is just what state we are when we power up.

The encoder has 4096 CPR (in 4x decoding mode, which is the default used), giving it excellent precision, with a pulse being sent every ${360 \over 4096} = 0.087 \ degrees$.

To read the encoder in this mode, we need to connect the sensor to a _TalonSRX_ and read the sensor data from the motor controller instance in our code.
```java
public class Robot extends TimedRobot {

  private static final int PPR = 4096;

  private WPI_TalonSRX motor;

  @Override
  public void robotInit() {
	motor = new WPI_TalonSRX(0);
  }
  ...
  @Override
  public void teleopPeriodic() {
	double counts = motor.getSelectedSensorPosition(); // this is the raw count from the pulses
	double degrees = counts / PPR * 360.0;

	double rate = motor.getSelectedSensorVelocity(); // this is measured as counts per 100ms
    	double degreesPerSecond = rate / PPR * 360 * 10;
  }
  ...
}
```

### NEO Integrated Encoder

<p align=center>
<img src="https://github.com/user-attachments/assets/fba94f61-589c-4bcc-a9ed-234ca1ff2f49" width="360">
</p>

Every _NEO_-series motor comes with an integrated magnetic relative encoder which is should be directly connected to a _SparkMAX_ motor controller. It is normally used by the _SparkMax_ to control the motor because it is a brushless motor, and thus requires tracking of position changes. But we can also use it for our own purposes. 

Because it connects to the _SparkMAX_, we read it from the motor controller over CANBus messages. 

#### Operation Mode: Relative

The encoder has 42 _counts_ per revolution, which is quite a low resolution, as it can only see a change of ${360 \over 42} = 8.57 \ degrees$. 

For velocity measurements, this is alright, as shafts rotating at around 1000 RPM will count 42000. This shows that when it comes to speeds, we don't need good resolution due to the shaft accomplishing many rotations quickly.
For position measurements this can indeed be a problem. Although, usually there would be a gearbox to which the motor is mounted (instead of directly connecting it to the mechanism), this will increase our resolution and thus solve the issue.

The encoder would be connected to the _SparkMAX_ by the necessity of operating a _NEO_ motor. From there we can access the encoder information via the _SparkMAX_ instance.
```java
public class Robot extends TimedRobot {

  private CANSparkMax motor;
  private RelativeEncoder encoder;

  @Override
  public void robotInit() {
	motor = new CANSparkMax(0);
	encoder = motor.getEncoder();
  }
  ...
  @Override
  public void teleopPeriodic() {
    	double rotations = encoder.getPosition(); // the amount of rotation made by the sensor. This is converted from raw counts and sent to us
	double rpm = encoder.getVelocity(); // the speed of rotations in RPM. This is converted from raw counts and sent to us
  }
  ...
}
```
