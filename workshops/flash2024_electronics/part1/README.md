In 24.4.2024 We met for the first part of the Electronics Workshop. This part is mostly theoratical, with some practical application of building a control system.

Presentations used:
- [Tom's Slideshow](https://docs.google.com/presentation/d/1-kQRPxuOjtqOZDmWHgj9rgo71iQC9VG4_2SRn7aQ0mo/edit?usp=sharing) - until Slide 66
- [Agam's Slideshow](https://docs.google.com/presentation/d/1dC1GV0fGQC_a8P3s0F6tjUUg_HeyRWFK/edit?usp=sharing&ouid=103040962347246613558&rtpof=true&sd=true)

Electronics plays quite a significant part in our robot, as any physical actions we do depends on one component or another. In code, outputs to the robot are transmited and executed via some electronics. 

In this part of the workshop we will look into the basics of electricity, the structure and devices that make up the Control System and the theory of communication via electronics.

## Electricity

Although not understood at first, electricity is, at its core, electrons. These subatomic particles are energy, and can be used to provide power to components. Unlike certain other particles, electrons can move through different materials, which means we can make them do things. Materials which allow electrons to move around freely through them are called conductive materials.

In nature, balance is everything. One commonly known fact, seen in many movies, is that in space, if one opens an airlock that holds an atmosphere on one side and vacuum on the other, the air will rush out into the vacuum. This is because the air is compressed densely on one side, but on the other there is no air, and as such, things are unbalanced. To balance things out, the air rushes out into the vacuum. 
The same is true for other particles. Say we have material that is positively charged, meaning its atoms lack electrons. and a material that is negatively charged, meaning its atoms have too many electrons, we create an imbalance. If we connect the two materials with a conductive material, the electrons will rush from the negative to the positive materials, seeking to balance the materials. They will stop when the material's charge is balanced. 

<img width="512" height="144" alt="electrons in coductor" src="https://github.com/user-attachments/assets/de889133-7501-442b-967f-48ce68f914fe" />

Batteries and other power sources do exactly that. You may notice that they are marked with “plus” and “minus” signs, corresponding to the positive and negative ends of the power source. As long as there is a charge imbalance, with a route between the positively and negatively charged materials and the connecting material allows electrons to flow through it, we will have a flow of electricity.

With the ability to make electrons move around, we can force them to do things. If we place a lamp connected to the conductive material between “plus” and “minus” of the battery, the electrons will be forced to pass through said lamp. The lamp can use the electrons passing through as sources of energy to create light. 

<img width="421" height="276" alt="basic circuit" src="https://github.com/user-attachments/assets/913498f4-67d4-439d-b3e8-68c305371739" />

These are the basics of electronic circuits. We can create many different circuits, and force electrons to do much: calculations, information storage, heat, light and so much more. All built around the principles we just discussed. Of course there is much more to electricity, but these are the basics.

## Electronic Circuits

> [!IMPORTANT]
> The circuits described here are Direct Current (DC) circuits, which are the ones we see in FRC

An electronic circuit is made up of a power source, devices and conductive wires connecting them all. One can describe the electricity in the circuit with:
- Voltage: the driving force of electricity. Higher voltage = more electricity flow. Voltage measures the charge difference between the positive and negative parts of the power source. It is measured in volts. $1 \ Volt = {1 \ Joule \over 1 \ Coulomb}$. Where $1 \ Coulomb = 6.24 * 10^{18} \ electrons$
- Current: the speed of the flow of electrons in the circuit. It is measured in Ampere. $1 \ Ampere = 1 \ Coulomb \over 1 \ Second$
- Resistance: the tendency of the material to constrict the flow of electrons. Higher resistance reduces the flow of electricity. Measured in Ohms. 1 Ohm = resistance in a circuit that has 1 Volt potential and 1 Ampere current.


_Ohm's law_ describes the relationship of these measurements: $Resistance = {Voltage \over Current}$.

There are several characteristics the define the behaviour of electricity in a circuit that we should know:

Most components of a circuit will have a certain resistance to them. When electrons flow through them, they will resist the flow. This causes a slow down of the current, and a loss of energy. This loss of energy is generally converted into heat, heating up the component and wires. Because of this loss of energy, the voltage of circuit than drops (as voltage is the measure of energy potential). We can calculate the voltage lost using _Ohm's law_: $voltage \ lost = {Resistance \ of \ component \over Current \ flowing \ through \ component}$.

<img width="351" height="260" alt="circuit split" src="https://github.com/user-attachments/assets/de442c79-3c4d-4338-9b78-82ed235bc3d6" />

Electricity will prefer the path of least resistance. Meaning, if there are several paths to take from positive to negative, current will mostly flow through the path where the resistance is lowest. Since this path will have less resistance, the current through it will be higher, compared to if it had to take the path with the lamp in the way.

<img width="416" height="255" alt="circuit split with resistance" src="https://github.com/user-attachments/assets/babcaf61-042a-4082-afe6-4d68286968ed" />

In this example, we have two paths with lamps on each path. If the lamps are the same, and the resistance is the same, the current will actually split between the paths equally. If one lamp has higher resistance, the current will split not equally, with a higher current going through the path with least resistance.

### Conventional Current

The fact that electrons flow from the _negatively charged_ part of the power supply to the _positively charged_ part was described much after electricity was discovered. Originally it was though that the flow  was from _+_ to _-_, and since atoms weren't all that understood, it wasn't clear what exactly occurs. Because of this, electronic circuits were originally designed with the flow being _+_ to _-_, this is called _Conventional Current_ and is the standard for circuit design.

## FRC Control System

The control system is the set of components which are used to control and operate the robot systems. These include a few must-have components, and other components which connect to them and differ between specific robots. All the components are wired together for communication with each other and power supply.

<img width="2592" height="1728" alt="Control System - REV" src="https://github.com/user-attachments/assets/914dedd6-4ee8-46c4-bae6-53d0b037817f" />

### Power Distribution

The power distribution component connects to the robot battery and supplies power to all other components of the robot. It is a must-have component, but it comes in several variants. 

The PDH (Power Distribution Hub) is the REV-made version and is newer, while PDP (Power Distribution Panel) is the CTRE-made version. Both perform the same function, but they do have some differences, like the cable terminals on the PDH being easier to work with.

<img width="400" height="704" alt="PDH" src="https://github.com/user-attachments/assets/779c3872-5759-42e5-b0eb-3beddcd095e1" />

The PDH is made up of two dozen power terminals for different components with different power supply:
- 20 High Current (40A) terminals for motors
- 4 Lower Current (20A) terminals for other components

This allows connecting and powering many different items with it. Which is necessary, as it is responsible for providing power to everything on the robot. It is similar to the breakout board of a home, controlling and stabilising the power supply to all outlets. Each terminal is protected by a single fuse and operates separately from all others, so if one terminal does not work, the others will. The fuses help protect connected components from burning due to a high current. 

The power itself is provided by a standard FRC battery connected via a special terminal to the board. This power is then routed to all other terminals.

The board also has a CANBus connection to allow the robot to communicate with the board, query sensors and check for problems.

### The Brain

The so-called brain of the robot, which provides processing and control over component functionality is the RoboRIO - a specially made Single Board Computer from National Instruments.

<img width="341" height="335" alt="RoboRIO" src="https://github.com/user-attachments/assets/b9e3b4ad-b97c-48bb-be6d-92673ef9c060" />

It may be small, but it is a computer nonetheless. It contains most components one might find in a normal PC (like a central processor, volatile and non volatile memory and such) but with a few extra parts for its specific purpose. It is not exceptionally powerful either, but it is capable enough to do its intended job.

Being a computer, it runs a variant of a Real Time Linux operating system and as such, provides similar capabilities seen in most operating systems, making it able to run more complex programs (like the Java Virtual Machine).

It has a set of I/O ports (easily spotted on the image) that enable it to electronically communicate with different robot components (like motor controllers or sensors). GPIO, Analog Input, I2C and CANBus are some of the ports featured on it, with associated hardware to allow operating them. For example, it can use its PWM ports to control rudimentary motor controllers.

The RoboRIO is powered from the power distribution board, from a 12V 10A port.
It also has the CANBus connected with the PDP/PDH to communicate with it.  The same goes with other main components.

### DC Motors

Motors are our primary motion generators for the robot. They take in electrical power and create a rotational motion. This motion can then be mechanically converted to do many different things.

There are many different motos that we can use. Each has different characteristics and performance. So choose your motos based on what they are used for.

DC Motors comes in two forms: brushed and brushless. Brushed motors are easy to operate, but have shorter life span, lower torque and speed. Brushless motors are harder to control (generally requiring a more complex algorithm and a hall effect sensor), but they generate more torque and speed.

Motors need electric power to work and thus are connected to the PDH/PDP for 12V 20A-40A (depending on the motor). However, because we need to control them, they are not connected directly to PDP/PDH, but rather via a motor controller. This controller allows the roboRIO to control the power and direction of the motor rotation.

<img width="512" height="512" alt="NEO V1.1" src="https://github.com/user-attachments/assets/1d540fe6-25a1-417a-a846-2ce8fab416c7" />

### Motor Controllers

Motor controllers are speciality devices intended for the control of electric motors. They can be very basic - only controlling the power and direction of the motor; or very sophisticated - capable of running special control algorithms for smart motion. Each motor controller is capable of controlling a single motor only.

Generally, in FRC, one can deduce the capability of the motor controller by its communication interface: basic ones use a PWM input, only capable of transferring power and direction requests. The complex ones use CANBus, which can transfer any kind or amount of data wanted. PWM controllers can be connected directly to the PWM ports of the RoboRIO, while CANBus ones can be daisy-chained to the other CANBus components.

The type or amount of controllers is selected depending on the specific robot, as they should match the requirements of the robot itself. Some robots have dozens of motors and as such need a matching amount of motor controllers.

<img width="700" height="700" alt="TalonSRX" src="https://github.com/user-attachments/assets/90b44087-83d2-40d3-9ab4-cdc40fe5d2b6" />

Each motor controller has 2 power inputs (GND, V+) and 2 power outputs (M-, M+) for brushed motors or 3 power outputs (A, B, C) for brushless. The inputs are connected to a PDH/PDP high power port (12V 40A/30A dependent on motor). The outputs connect to the motor.

Most smart controllers also have a port for connecting sensors. Generally this is optional. However, for brushless motors, the encoder connection is a must. Connecting the sensors to the motor controller allows the motor controller to run algorithms with the sensors internally, instead of requiring us to run one on the roboRIO. This includes PID loops, and automatic limit switch stopping.

To control the motor controller (and thus the motor), the device has some data input/output ports. Most modern FRC controllers communicate over CANBus, so they have CANBus connectors. Older ones use a PWM input. Thanks to the flexibility of CANBus communication, we can also query sensor information from the motor controller.

## Electronic Communication

When two devices exist in the same electronic circuit, how can they communicate? How does one device makes the other do something?
This question is essential for the operations of many devices. Take our motors and their controllers: how can the RoboRio tell them to operate in a specific manner? After all, in our code we are the ones to request the motor to rotate, and we specifiy both speed and direction.
The same goes for our sensors: how can we get information from them?

In this section, we will learn how this is done.

### Basics of Data Transfer

We've learned that in electronic circuits, there are 3 measures:
- Voltage
- Current
- Resistance 

Assume that one device controls the voltage over a wire, while a second device "reads" this voltage. In such a circuit, we then have the ability to say something by adjusting the voltage to certain levels. And this is relatively easy to do. By placing a variable resistor on the wire we can change the resistance and thus also the voltage. 
This is the base to Voltage-Driven control, where the voltage level is encoded with specific data.
This play with the voltage level is simple to perform. What actually occurs is that we change the energy of the electrons and this can easily be read. And because electrons are fast, the "reader" will see the changes almost instantly. This works fine for short distances.

<img width="512" height="214" alt="Voltage Comm Basic Circuit" src="https://github.com/user-attachments/assets/17fdb8e7-58d6-4d1b-911b-b0b526cebd89" />

The above circuit illustrates sending digital data over a 3rd wire. As normal we have the "+" and "-" connections, which create a circuit over which voltage and current flow. You will see that they are marked as _VCC_ and _GND_. Generally, we can think of the as equivelent for "+" and "-" though this may not necessarily be the case. In reality the _VCC_ has the _higher voltage_ (say 5V) while the _GND_ has the _lower voltage_ (say 0V), but whether they are the "+" or "-" is circuit-dependent. Of course, when we connect them together, we get a flow of current as in any circuit. Now for the third wire, we can use it to create another circuit connecting the _VCC_ and _GND_ to each other and thus get voltage through it. On the controller device, we connect the _VCC_ to this wire via a switch. When the switch is open, the circuit is open and there is no voltage on the wire; when it is closed, the circuit is closed and the voltage will be the same as _VCC_. On the reading device, we connect this wire and the _GND_. This leads to a circuit from _VCC_ on the controlling device, through the third wire, and back to the _GND_. If we place a voltemeter on the reading device between the third wire and _GND_, we get a measure of voltage on the wire. So the device can now read this voltage and determine its value.
This is not exactly accurate though, and there are many different circuit configurations, but it is the basic concept of the data transfer.

### Electronic Noise

Noise is an unwanted disruption to a signal. In electronic circuits these disturbances manifest themselves via changes to the voltage on the wire.

<img width="489" height="345" alt="noise example" src="https://github.com/user-attachments/assets/24bda7b1-2d07-45d8-9f02-d607f102df4e" />

Noises are generally unavoidable, but usually are pretty small in effect. So when we have a 5V voltage on a wire, in reality it is never exactly stable, jumping wildely around 5V both up and down (illustrated by the image above).

There are many causes for noises, eminating from outside sources projecting electro-magnetic fields that affect the transfer of electrons in the conductor, or properties of the material itself that disrupt the electrons.

### Signal Types

Generally, there are two types of signals that can be used to transmit data: analog and digital. This is true for many mediums, not just electricity, but these are the only ones used in electronics. We can use different circuits to generate these signals as a way to transfer data from one device to another.

#### Analog

Analog signals are continous time-varying signals constrained within a range. These signals use a medium property (i.e. voltage) to encode data. So at any given moment, this property (say voltage) represents a certain quantity, and is thus _analogous_ to this quantity.  

<img width="1448" height="496" alt="analog signal" src="https://github.com/user-attachments/assets/2d045808-507c-472b-ad02-683236039ec3" />

For a voltage-driven system, we can, for example, use the voltage to represent a certain sensor info, like temperature. So the signal of voltage (over time) will represent the temperature at any given moment. 

One sensor may define a range of 0 degrees C to 20C. To encode this into the signal, we simply convert the temperature reading into voltage and set the voltage on the line to it. If we have a range of voltage between 0V and 5V, the conversion would be `voltage = temperature / 20 * 5`. This changes between sensors and devices.

To provide this control over voltage we can use a variable resistor. With such a resistor, we can change the amount of resistance and thus change the voltage over the line, because the voltage lost to resistance is `V = I*R`, so assuming a constant current, the change will `R` will increase or decrease the voltage drop, changing the voltage on the line (after the resistor).

<img width="613" height="273" alt="analog output circuit basic" src="https://github.com/user-attachments/assets/c219f790-d4f9-4d14-a135-4573ee37735d" />
<img width="410" height="253" alt="Tinkercad Analog Signal" src="https://github.com/user-attachments/assets/103290d5-e430-4223-b6cc-458489aa5a6e" />

At their core, electronic circuits are analog. As they used varying voltages to create different behaviours. Another analog data is audio information: AM (Aplitude Modulation) transmitions, for example, change the amplitude of the signal to encode specific audio information.

<img width="363" height="275" alt="audio signal" src="https://github.com/user-attachments/assets/19bd14dc-3ba2-41bb-b8db-9474e98b06dd" />

Although mathematically, 0-5 range has an infinite amount of numbers, realistically, it is a finite range. This is due to limitations in the ability to adjust the voltage and the ability to read the voltage. The _Arduino Uno_ analog pins, for instance, has a resolution of `4.9 mV`. So if the voltage changes by, say, `3 mV`, it will not be able to detect this. How many values we can play with is determined by the resolution of the components which make up the circuit.

Noise is another issue for analog signals, as a small noise causing a change of `10 mV` can change the reading we get from the voltage. Depending on the resolution we use and the strength of noise, this can dramatically affect our reading. In our earlier example of a temperature analog signal, a change of `0.25V` (${20 \over 5}$) will change our temperature reading by `1C`.

#### Digital

Digital signals are a sequence of discrete values. Binary signals, for example, can only be either `0` or `1`. The values are not continous to one another in the medium, so for a binary signal we switch between `0V` for a value of `0` and jump to `5V` for a value of `1`.

<img width="1262" height="467" alt="digital binary signal" src="https://github.com/user-attachments/assets/8f4a800e-bbdd-4554-8c69-5e4ebaa42e40" />

For a voltage-drive system, we can use the voltage to represent `0` or `1` by changing the voltage between `LOW` (close to `0V`) and `HIGH` (close to `5V`). This is considered a _logical signal_ and is used to sent logic data (in binary). Of course this means we have only two value options at any given moment, so we can only report a _boolean_ at a moment, unlike analog signals which have a range of possible values.

<img width="512" height="214" alt="digital output circuit basic" src="https://github.com/user-attachments/assets/0b38ed5f-fea6-429b-b75b-c0aeaa0cea6b" />

<img width="324" height="301" alt="Tinkercad Digital Signal" src="https://github.com/user-attachments/assets/c7c2fdac-b399-4128-8463-6e6131e090a5" />

To control the voltage, we can use either a switch to close the circuit (for _HIGH_ voltage) and open it (for _LOW_ voltage), or we can use one _high-resistance_ resistor (for _LOW_ voltage) and _low-resistance_ resistor (for _HIGH_ voltage).

Processors and computers are considered digital systems, as they represent data internally with digital signals. This is evident by the use of binary numbers in processors.

Binary digital signals, are usually represent in a range of voltage. That is, instead of `1` being exactly `5V`, it is actually a range (like `2V - 5V`), and any voltage within this range will be considered `1`. Same for `0` values, being a range (like `0V - 2V`). `TTL` represents specs for such voltage levels:
- `2V` - `5V` is considered _HIGH_ voltage, i.e. `1`
- `0V` - `0.8V` is considered _LOW_ voltage, i.e. `0`
- The range in the middle (`0.8V` - `2V`) is ignored, keeping the last value (if was `0` and voltage is now `1V`, value stays as `0`).

There are other configurations used, not always with `5V` voltage level. `3.3V` is also popular.

Unlike analog signals, digital signals are generally less susceptible to noise, thanks to massive difference of voltage between each value. For _TTL_, we need a change of around `1.2V` to switch from _LOW_ to _HIGH_. 

#### Analog vs Digital

Analog and digital signals both have their advantages and disadvantages and are thus used in different circuits depending on what is wanted.

 Analog    | Digital (binary)
------------------------------------------|----------------------
Range of values. Dependent on voltage level and resolution | A set of discrete values. 0, 1 for binary
Susceptible to small noises | Requires a big noise to be affected
Circuit is a bit more complex to control and read, but still simple | Simple and easy circuit
Integrates well with circuitry, but problematic with digital components | Integrates well with analog and digital, but has limited use in analog circuits

As mentioned in the table, digital components (processors/computers) have a difficult time working with analog signals, as they do not understand them natively. For that, we have special conversion ciruits which converted between the two domains. _Analog-Digital Converters_ (ADC) convert analog signals to digital. _Digital-Analog Converters_ (DAC) convert digital signals to analog. These components are described by their resolution, which explains how sensitive is the circuit to read/sending different analog signals. 12 bit resolution indicate a range of values from 0 to $2^{12}$ (4096). This means that the smallest change the circuit can work with is `1.22 mV` (${5V \over 4096}$).

#### Pulse-Width Modulation (PWM)

_PWM_ is a type of digital signal which has special characteristics when used in an analog circuits. It is generated by changing the voltage to a _HIGH_ voltage for a certain amount of time (also called length), and then returning the voltage to _LOW_. This is called a _pulse_. By changing the length of the pulse, we can create a changing signal.

<img width="376" height="134" alt="PWM signal" src="https://github.com/user-attachments/assets/fa1d01bb-5d86-45f1-833f-82b519da14ba" />

PWM signals are characterized by several properties:
- _Period_ defines a set amount of time. Inside this time frame, we can play with the length of the pulse. When the period ends, we switch to the next pulse.
- _High Time_ defines the amount of time in a period that the signal is of a _HIGH_ voltage.
- _Duty Cycle_ defines the precentage of the _Period_ of _High Time_ $Duty Cycle = {High Time \over Period}$. 
- _Frequency_ is defined as ${1 \over Period}$ and is measured in `Hz` (1 `Hz` = a period of 1 second).

<img width="704" height="564" alt="Varying Duty Cycles" src="https://github.com/user-attachments/assets/0fe91713-7777-43f3-bbd0-8e4fafeca832" />

PWM signals have some interesting properties and advantages over analog signals. For example, they can be easily generated by digital devices, as they do not require a _DAC_, the voltage is either _HIGH_ or _LOW_. Normally a dedicated clock is used to automatically generate this signal. The clock will be configured for a specific _Frequency_ and can be told to create signals of varying _Duty Cycle_. 

One use can be to transfer values from 0->100 (represented by the _Duty Cycle_, as 0% to 100%). But actually PWM is noramlly used due to what effects it can cIf we reate in an analog circuit.

Consider motor controllers: When connected to the PDP, they receive 12V voltage. To control DC motors, they need to output a varying voltage depending on the wanted speed (6V for 50% speed). So how can they control this output? One way is using a variable resistor, which causes a voltage drop from 12V to the wanted voltage. However, this is not perfect, as resistance generates heat, and at currents of 40A (which isn't weird for a motor), the heat generated is quite high. Moreover, it causes a huge loss of energy because of this heat. So its not very efficent.

But what about PWM? We can connect the power supply from the PDP through a special switch. The output from the switch goes to the motor. When we supply _HIGH_ voltage through the PWM wire, the switch closes and pass voltage and current to the motor. When the voltage is _LOW_, the switch is open and nothing goes through. So what happens with a PWM duty cycle of 50%? The switch is closed half of the time, transferring 12V, and open the other half, transferring 0V. If the PWM frequency is high, this occurs quickly, turning the motor to full power and then back off. This leads to the voltage averaging at 6V and gives an end result of 50% velocity. Because there is little resistance, we don't loss much energy. This is the basis for PWM motor control.

<img width="860" height="244" alt="PWM Motor Controller - Basic" src="https://github.com/user-attachments/assets/467ccfe7-6426-425c-bbca-98cd3f20c092" />

### Sending Multiple Bits

When connecting two digital components (say computers) we sometimes need to transmit a lot of information between them. For example, when uploading an image to a server. Images are big, they are generally measured in the Kilo-Byte (kB) range ($1 \ kB = 1000 Bytes = 8000 Bits$). How can we transmit such a huge quantity of information? 

#### Serial

Consider: one wire with a digital signal can, at any given moment, be either `0` or `1`. But what if we change the signal after a few moments to a different value? So we can actually send several values, one after another.

<img width="730" height="261" alt="multiple bits on wire" src="https://github.com/user-attachments/assets/d60a13c0-ac0f-4e4b-9ced-01e8be15d559" />

The graph above shows the state of voltage on a wire throughout time. Every 1 second we change the voltage to indicate a different value. The reader can read every one second to read each bit at a time. After several seconds we have a few bytes of information accumulated. Though, with this speed we would only read 60 bits per second, which is very slow. But computers are fast nowadays, so we can speed up the bit writing to something like 1 bit per 1 millisecond, or even faster.

This is what's called serial communication, where bits are written one by one to the wire (one after another = in series). This integrates quite well with computer memory, as we can simply send the bits as they are stored in memory to a second computer, which can then store it in its memory directly.

This method is quite popular for data transfer. Protocols like USB and PCI/e are all based on serial communications, and can reach speeds of 1 Giga-Bits per second (1 billion bits per second).

<img width="591" height="140" alt="Serial Communication" src="https://github.com/user-attachments/assets/6f6c8f77-aaa7-48b5-8584-eca912225e25" />

##### Synchronization

Sending serial information over one wire is nice and all, but actually it has a problem: how does the reader know when to read? In our graph earlier, we wrote each bit for one second before switching to the next bit. But if the reader doesn't know when to read, it could accidently read the same bit twice or three times, or maybe it will miss reading a bit. So how can we tell the reader when to read to catch all the bits?

Well, in theory, we can tell the reader to read once every second, so that it reads each bit (because we write them every second). But this is still not good enough, for two reasons:
- The reader needs to know when to actually start reading. When starting to read, the reader reads the first bit and then waits 1 second for the second bit and so forth. But it can't actually know when it needs to start automatically. One could decide to start reading when the voltage on the line becomes _HIGH_, but that only works if the first bit sent is `1`, otherwise we will miss the first `0`. But this idea does have some merit.
- there is actually no gurantee that the writer will write exactly every second, and that the reader will reader exactly every second. This is because computer clocks are not necesarily accurate, they easily drift. Moreover, the reader and writer have different clocks, ticking at different times (depending on when the clock started, how fast it is, and how precise it is). So even if they started at the same time, they will quickly drift apart and lose synchronization.

To solve this, we can take several different approaches, and different protocols do implement different solutions. But there are two classic approaches to this.

Our first option is to synchronize the clocks by "transmitting" the writer clock to the reader. However this is done, it helps synchronize the reader to the writer, telling it exactly when to read. One way to do this is to connect another wire and used it to transmit the clock information. This information will tell the reader exactly when it should read the data information. So we keep the clock wire normally _LOW_. When we send data, we first set the data wire voltage, and then update the clock wire to _HIGH_ for the duration of the bit and return the clock wire to _LOW_ and switching to the next bit. What we'll get are short pulses on the clock wire that turn _HIGH_ when the bit should be read. So when the reader sees the clock wire switching to _HIGH_, it reads the bit.

<img width="495" height="215" alt="Clock Line" src="https://github.com/user-attachments/assets/083161d6-db06-4d8c-aff3-cec25a31e575" />

Our second option is to synchronize the clocks by adding what we call _start_ and _stop_ bits to the data wire. These bits will indicate to the reader that the "message" has started and it should start reading bits at a constant interval. If the data wire is normally _LOW_, when can start the data transfer by setting it to _HIGH_ for one bit duration. This will indicate to the reader that we are transferring data, so when the reader sees the voltage turn to _HIGH_, it can wait one bit duration and then start reading bits at a pre-configured interval. When the message ends, we need to make sure the wire returns back to _LOW_ voltage so that we can turn it to _HIGH_ later for the start bit. This is the stop bit. Of course this still has the clock drift problem. But if we transfer a small amount of data, the drift should be low enough for the reader to read currectly.

<img width="850" height="345" alt="Start/Stop Bits" src="https://github.com/user-attachments/assets/ff121d18-740b-4c40-919c-4a9b776ad49e" />

#### Parallel

Instead of transferring bits 1 by 1 on a wire, parallel communication uses multiple wires, one to transfer each bit. So with 8 wires, we can transfer a full byte at once.

<img width="588" height="258" alt="Parallel Communication" src="https://github.com/user-attachments/assets/9e89755f-bcd2-465c-bf4c-84a766267ea6" />

Of course this requires one port for each wire so we can control the voltage on the wires, but in theory, we can effectively multiply our speed, because we are sending multiple bits at once. And we can combine it with serial on each wire, so at any moment, we can write a single byte and then another byte and another after that.

But this isn't perfect. First of all, it suffers from the same synchronization problems as normal serial communication. And it can easily be solved in the same way, through a clock line or start/stop bits. However, because we have multiple wires, we must synchronize all those wires. If we have specialized hardware, it can write the bits to all the wires at the same time. But if we don't and we have to write to each wire individually, then we have a problem where each wire isn't synchronized with the other wire. Consider that our computer can only perform one thing at a moment, so if we can only write to one wire, it means that there is a delay between writing to each wire. This causes a delay between them, making it more difficult to synchronize.

This doesn't mean parallel can't be used, but it has its difficulties. Some uses of parallel bypass this by only transfering 1 bit per wire. This still lets us transfer multiple bits quickly, especially if we have the hardware to write to all the wires at once.

Another approach could be to treat each wire as a seperate serial wire. This will let us send several messages (one per wire) individually and then combine them. This again increases the speed. But because each wire is seperate, we do not need to synchronize them to each other, as they can be read seperatly.

#### Serial vs Parallel

 Serial    | Parallel
------------------------------------------|----------------------
Simpler to synchronize, as there is only one data wire normally. | Multiple wires to synchronize complicate synchronization
1 bit at a time | multiple bits at a time (1 bit per wire)
data will take time to transmit and receive | data can be transferred immediatly if the size of the data is equal to the amount of wires

So both are used for different situations, and are combined some of the time. A lot of protocols use serial communication over multiple wires in order to speed up communication. RS-232 for example uses two wires: one to send serial data from device A to B and another to send data from device B to A. So the devices can send data to each other at the same time. This is called full-duplex: communication can go both ways at the same time.

### Communication Bus

We know that we can use serial communication to send information from one device to another. But what if we have to communicate between more than two devices? This is actually a common problem, as complex machinary (like cars and planes) have many many devices and computers that make up different functionalities. 

Here we are introduced to the bus topology. 

<img width="849" height="252" alt="bus topology" src="https://github.com/user-attachments/assets/91f19a88-359b-4839-9a34-743b5a6b5644" />

The bus topology involves using a single line of communication (amount of wires depends on the protocol) which is shared among all the devices. This way, when communicating over the line, all the devices can talk to one another, since they are all connected over the same wires. For this to work, we need several things:
- All the devices must be able to read/write to the wires, that way they can all talk to each other. This is normally done by making sure all devices have ports connected to the wires.
- We need a way to send data for just one device. Normally, we only want to communicate with one device at a moment, since each device expects different data. Consider: when we talk with a distance sensor we talk about some stuff, but when its a motion sensor we talk about different stuff. So not all devices should actually use all the data, because it is not _meant_ for them. There are several ways to mark data for a specific device. One approach is to add some bits at the start of the data that indicates which device it is meant for. So when a message is sent, all devices read it, but after they see the message isn't meant for them, they ignore it.
- Since all the device communicate on the same line, at the base, only one device can communicate at one moment. If two tried to send data on the same wire, they would interfere with each other. So there must be a way to prevent this from happening, a way to make sure a device doesn't write data to the wires while another device does the same.

But Buses have problems too:
- There is a physical limit of how many bits can be sent over a single line. This is called the bandwidth. Because all devices communicate on the same line, it means there is a total limit of data that all the devices can send to each other every minute. And because there are several devices, more data is normally transferred, so while this limit is true to all communication, it is more relevant for buses.
- Because all devices communicate over a single shared line, if something happens to it, we loss communication with multiple devices and not just one. So we have a serious failure point.
- Due to the complexity of bus communication, it generally requires more complex hardware than just normal digital or analog ports.

##### Registers

Communications on buses sometimes take the same of read/write operations from/to registers. Registers are reserved spaces in memory for information/data, basically like a variable. Devices will allocate a set amount of space for these registers, where each register will store information regarding control/state of the device itself. For example, an accelerometer can have a register which stores the measure of acceleration measured by the accelerometer, or a control register which defines how the accelerometer should operate.

Some buses frame their communication around read and write operations with these registers. Say I wish to read the acceleration from an accelerometer. For that purpose, I can send a read request to device requesting the contents of the register which contains this information. This register (like all registers) will be identified by a pre-defined address (basically a number). In return, the value of the register is returned and then can be used. Another example is writing to a register. Say the accelerometer has register which says whether the device should be in sleep-mode or operate normally. If I wish to put the device to sleep, all I have to do is to send a write request to that register address with a value saying "enter sleep mode". The device will later query the value of the register, seeing that it should enter sleep mode and will do so.

#### I2C

The _Inter-Integrated Circuit_ (I2C) bus was originally conceived as a way for two integrated circuits to communicate with each other (hence, the name). Being a bus, it allows multiple devices to communicate over shared communication lines. 

<img width="353" height="143" alt="I2C scheme of nodes" src="https://github.com/user-attachments/assets/52f5b706-22e6-4219-a531-8ca82bb5ac1d" />

It follows the master-slave architecture (now renamed to controller-peripheral). In this architecture, the communcation is controlled and managed by a single device, called the controller, while the other devices are known as peripherals. This means that only the controller device can initiate communication while the peripherals merely respond to the controller's request. So, say we have a keyboard connected as a device on the bus and we want to know which keys are pressed. Well, the keyboard is not allowed to tell us by itself, since it is simply a peripheral. The controller (us, the computer), has to send a message to the keyboard requesting that information, to which the keyboard will respond. This scheme provides a solution to preventing two devices from communicating at the same time, since because only the controller can initiate the communication, it can make sure only one device will communicate - either it or the peripheral it wishes to speak to. However, I2C does have support for _multi-master_ operation, where there are multiple _controllers_ over the bus.

Data transfer occurs over two different wires: the _SDA_ (data line) and _SCL_ (clock line). It is a serial communication which uses a clock line. The clock line is controlled by the controller, which sets the pace of the communication. When the controller writes, it writes at the same pace as the clock it data it outputs. When the peripheral writes, it writes according to the clock data as requested by the controller.

Each device has a 7 bit address pre-programmed into it. The _MPU-6050_ IMU has the address `0x68`. So for the controller to communicate with this device on the bus, it will use this address. Of course this has a problem were attaching two devices of the same type on the same bus will lead to an address collision. To solve this, some devices have an hardware switch to allow changing between two pre-set addresses, although this limits to two or three devices of the same model.

Data is transferred over the bus in pre-defined frames, each of this frames has a specific format. This allows the reader to know exactly how to interpret the data sent. Each frame is 8 bits long. A normal communication is made up of:
- Start: indicates to the devices on the bus that communication started and the bus is busy
- Address Frame: each communication has one address frame which indicates who the communication is done with
  - Address: the address the frame is intended for (which peripheral), with the most significat bit being the first bit.
  - Read/Write bit:
    - `0` means the controller wants to read data from the peripheral
    - `1` means the controller wants to write data to the peripheral
  - ACK/NACK: once the device has received the frame it will either report ACK or NACK to indicate that the data was received successfully
- Data Frame: each communication can have many data frames. Each will contain one byte of the data
 - Byte: byte of the data, with the most significat bit being the first bit.
 - ACK/NACK: report on successfully receiving the last byte of data 
- Stop : indicates to the devices on the bus that communication is done and the bus is free

<img width="1280" height="323" alt="frame format" src="https://github.com/user-attachments/assets/98fdc32a-7175-4739-8837-d3ac606114e6" />

The ACK/NACK bit is at the end of each frame. It is intended to allow the recipent of the data to report whether it has received that data. For the sender of the data, this bit is left un-touched (idle state, which is HIGH), this value means NACK (not acknowledged). The recipent has to manually pull the line to _LOW_ to indicate ACK (acknowledged). So, after the frame is sent, the recipient has to report ACK if it has properly received the data, or leave the line as is to report NACK. If ACK is reported, the sender will know (by reading the line) that the recipent has received the data successfully. If the line is left as NACK, the sender will know that the data wasn't received properly and should resend it.

Frames operate for register read/write. The read/write bit in the address frame indicate if reading or writing to the register is wanted. The first data frame indicates the address of the register. The subsequent data frames differ depending on whether it is a read or write operation: for read operations the data frames are provided from the device and they are the value of the register. For write operations these frames are provided by the controller and are the value for that register. The amount of frames depend on the size of the register: 1 frame for 1 byte, 2 frames for 2 bytes and so forth. 

#### SPI

The _Serial Peripheral Interface_ (SPI) is a rather simple general bus built around a straight-forward 2 way serial communication. It is considered the de facto for serial communication for many devices, which lead to it having many variants.

<img width="640" height="508" alt="SPI nodes" src="https://github.com/user-attachments/assets/530d6103-1509-42d7-b251-4f4a49dac0da" />

Unlike _I2C_, it normally has 3 standard communication lines:
- MISO: Master In, Slave Out: line for the slave to write and master to read
- MOSI: Master Out, Slave In: line for the master to write and slave to read
- SCK: The clock line

This means that it is capable of full-duplex communication, because the master and slave can read at the same time (thanks to there being a line dedicated to each direction). This can increase the speed of communication as more data can be transferred over the bus. The clock line is controlled by the master and indicates the timing of data transmition. At each rising edge of the clock, the MISO and MOSI lines can have 1 bit transferred.

As the names suggest, SPI follows a master-slave architecture like _I2C_, so the master has to initiate communication with a selected slave. However, because there are 2 communication lines, the slave only has to wait for the master to trigger it, but does not have to wait for the master to send data. Though it is typical for the slave to wait for the master to request it to do something before sending any information. 

Unlike _I2C_ though, selection of the peripherial is done not via address info sent over the communication line, but rather through a seperate set of lines named CS (chip-select). Each CS line is connected to a particular peripheral (so one CS per peripheral). When the CS line is brought to LOW voltage, this indicates to the slave that communication with it has started, and when returned to HIGH, the communication has stopped.

So in total, SPI has at least 4 wires (MISO, MOSI, SCK, CS) with an additional CS line for each subsequent slave. The MISO, MOSI and SCK lines are shared among all slaves.

Unlike _I2C_, _SPI_ does not have a uniform format for data (as was with the _I2C frame_). Rather different devices have their own formats for communication. Accelerometers, for example, may use a register based communication as done in _I2C_. However, RF transmitters will generally just expect to receive bursts of data which they should transmit.

<img width="500" height="425" alt="SPI data" src="https://github.com/user-attachments/assets/dd4214f5-b041-4e8a-bacc-d336e1e8a88b" />

#### CANBus

The _Controlled Area Network_ (CAN) bus is a standard for vehicle designs, connecting together many components which make up a vehicle. It is a rather more complex protocol, but also quite capable. This bus uses serial data transfer with a message-based protocol.

<img width="749" height="319" alt="CANBus nodes" src="https://github.com/user-attachments/assets/71975364-686f-4909-a035-a77064eef423" />

It is made up of 2 lines of communication: _CAN HIGH_ and _CAN LOW_, both of which are used to transmit data is a twisted-pair configuration. It does not have a clock line (unlike the previous protocols we've looked at). In this configuration, to send a bit of data, both lines are changed to a value indicating that bit. It uses differential voltages in the wires such that the subtraction between the voltages of the wires (_voltage HIGH_ - _voltage LOW_) is equal to the actual value we wish to transfer. 

<img width="1296" height="900" alt="differential data" src="https://github.com/user-attachments/assets/cfd946b4-3fb9-4a2a-a992-560ba6541f18" />

The bus has two voltage states:
- There is the recessive state. This state has both lines at a voltage of `2.5v` so that the differance between them is `0v`. This is the idle state of the bus. So when no data is transferred, this is the state this bus is in. This state also indicates a logical `1` when data _is_ transferred. It's considered _recessive_ because it is the default state of the bus. So when the devices on the bus do nothing, then it occurs, meaning that it can be overriden.
- There is the dominant state. The state of line HIGH is `3.75v` and of the LOW is `1.25v`, so their subtraction leads to a `2.5v`. This state indicates a logical `0` when data _is_ transferred. This state is dominant because it requires an active action from a device to be achieved and it overrides the default state. So when a device wants the state to be recessive, it has to do nothing, but when it wants it as dominant, it to actively change the voltage on the bus.

Data transferred over the CANBus is sent in specific format:
- Start bit: indicates the start of the frame
- ID: indicates an unique ID for each message to identify itself. So the receiver can know what the message coontains
- Control: the control portion contains the length of the data being sent
- Data: the data portion is between 0–64 bits of data, with the length being stated in the control portion of the message.
- CRC (Cyclic Redundancy Check): a number used for checking the validaty of the data received (in case of data corruption due to noises).
- EOF (End of Frame): indicates that the frame has ended and that a new message may be sent

<img width="880" height="279" alt="CANBus frame" src="https://github.com/user-attachments/assets/c62a91d8-d2ef-477d-8099-4ee43820150b" />

This bus does not use master-slave configuration, meaning that all devices may initiate communication with any other device on the bus. This of course introduces complications of how to make sure the devices do not talk over each other, but it does allow devices to update each other freely without the intervantion of a controlling device. The conflicts of communication is solved by a process of arbitration. When two devices wish to communicate at the same time, they may both initiate communication and start sending their data. While sending their data, they also pay attention to what happens on the wire. While there messages are exactly the same, both of them communicate over the bus. However, the moment their messages differ, then arbitration actually occurs. When the messages differ there are two options, as the bus can only be a logical `0` or `1`. The device which attempts to send a `0` will try to put the bus in a dominant state. Because the dominant state overrides the recessive state, than the dominant state will be the one the bus takes. The devices trying to write `1` (recessive state) will see that the bus state is different from what it wanted and realize that another device is communicating and thus stop the communication. Because devices sending `0` win over devices sending `1` and _ID_ is the first info sent, then normally messages with lower _ID_ will win arbitration.

<img width="512" height="216" alt="CANBus arbitration" src="https://github.com/user-attachments/assets/49e23dcd-a6d3-4473-a4e9-b64fa7d919e8" />

#### Comparing Buses

Aspect | I2C  | SPI  | CAN
------|--------|------|------
Clock | Synchronized (Clock line controlled by master) | Synchronized (Clock line controlled by master) | Asynchronous (no clock line)
Speed | 100 Kbps - 1 Mbps | Up to 65 Mbps | Up to 1 Mbps
Max Wire Length | Up to 1 Meter for 100 Kbps | Up to 10 Meters | Up to 40 Meters for 1 Mbps
Error Handling | ACK/NACK | Nothing pre-defined, users will have to implement their own | CRC and specialized frames for errors
Number of Wires | 2 | Minimum 4, depending on amount of CS lines | 2

