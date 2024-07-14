
_Cross The Road Electronics_ (_CTRE_) are one of the major suppliers for FRC. They develop, manufacture and sell a bunch of devices which we use with our robot. From the control system components like the PDP, VRM and PCM to motor controllers like TalonSRX and motors like Falcon500.
They've been around for a long time and were actually the first to produce a _smart_ motor controller. 

Because they make a lot of _smart_ devices, there is actually a lot to look at in terms of capabilities and control/config code. However, luckily they've tried to make most devices expose a shared abstract control interface, making most code similar despite the device being different.

_Pheonix_ is the name of the series of motor controllers and sensors they produce for FRC use. In the past, we where using the V5 version of the control software. In 2024 they've released the V6 version. 

We will be dividing the devices used into two categories: Phoenix V5 an V6. 

Pheonix V5 devices are older devices which simply did not receive the V6 update, these include:
- TalonSRX
- VictorSPX
- Pigeon 1

Phoenix V6 devices used to function with V5, but received an update which compels and requires users to switch to this new version. These devices are still supported by CTRE, and include:
- TalonFX
- CANCoder
- Pigeon 2

Writing code to and using Pheonix V5 and V6 is different, and as such we will be learning to use both. We will be mostly looking at Pheonix V5 motor controllers here, and not sensors or Phoenix V6 motor controllers. This basically leaves the _TalonSRX_ and _VictorSPX_.

The code for _TalonSRX_ and _VictorSPX_ shares a similar base. So writing code for them will look the same, though they do have some different capabilites. As such, we will be looking at only one of them, but what we do applies to both generally.

| *TalonSRX* | ![TalonSRX](https://www.vexrobotics.com/media/catalog/product/cache/d64bdfbef0647162ce6500508a887a85/2/1/217-8080.jpg) |
|:--:|:--:| 
| *VictorSPX* | ![VictorSPX](https://cdn.andymark.com/product_images/victor-spx-speed-controller/5cdc1cd6fe93c623522f4e9c/zoom.jpg) |
|:--:|:--:| 
| *TalonFX and Falcon500* | ![TalonFX](https://cdn11.bigcommerce.com/s-7cuph2j78p/images/stencil/1280x1280/products/134/643/3__19352.1673993701.png?c=1) |

### Phoenix Tuner

The [_Phoenix Tuner_](https://pro.docs.ctr-electronics.com/en/stable/docs/tuner/index.html) allows reviewing, configuring and controlling _Pheonix Devices_. 

It has two versions, the obsolete _Phoenix Tuner V1_ and the new _Phoenix Tuner X_. The _Tuner X_ should be the ones used normally and it is capable of supporting both V5 and V6 firmware. The _V1_ can be used with V5 devices only and should only be used with devices running old firmware such that they are not recognized by the _Tuner X_. 

To use the software, connect a USB-B cable from the controlling computer to the RoboRIO's USB-B port. It will allow control and access to all the _CTRE_ devices on the CANBus. Once connected, you will be able to select a specific device and configure or operate it.

![Phoenix Tuner X](https://pro.docs.ctr-electronics.com/en/stable/_images/highlighting-ip-area1.png)

### Firmware

The firmware is the software (code) running on the devuces. It is booted automatically (and quickly) when it receives power.

This firmware can and should be regularly updated, as _CTRE_ releases new versions with fixes and improvements. Use the _Phoenix Tuner_'s update functionality to do so.

![client update tab](https://pro.docs.ctr-electronics.com/en/stable/_images/selecting-firmware-version1.png)

> [!NOTE]
> For each season, CTRE typically releases new versions of the firmware. Make sure to check and update at the start and during the season.

> [!NOTE]
> Only Phoenix V6 devices will be updated from this year going forward.

### Devices and Capabilities

#### Motor Controllers

| Name | Supported Motors | Integrated Encoder | Firmware Version | Data Port Support | Remote Sensor Support | Code Class |
| ---- | ---------------- | ------------------ | ---------------- | ----------------- | ----------------------- | ---- |
| *TalonSRX* | Any Brushed Motor | No | V5 | Limit Switches, Quadrature Encoder, Analog Sensor | Yes, over CANBus | `com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX` |
| *VictorSPX* | Any Brushed Motor | No | V5 | _ | Yes, over CANBus | `com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX` |
| *TalonFX* | Falon 500 Brushless | Yes (Falcon500 encoder) | V6 | Limit Switches | Yes, over CANBus | `com.ctre.phoenix6.hardware.TalonFX` |

#### Encoders

| Name | Type | Medium | PPR | Connection |
| ---- | -----| ------ | --- | ---------- |
| *CANCoder* | Relative and Absolute | Magnetic | 4096 | CANBus |
| *SRX Magnetic Encoder* | Relative and Absolute | Magnetic | 4096 | Quadrature (relative) / Pulse Width (absolute); Uses a special cable (Gageteer) |
| *Falcon500 Integrated Encoder* | Relative and Absolute | Magnetic | 2048 | No External connection (directly connected to TalonFX) | 

### Configurations

There are several basic global configurations for the devices. These configurations apply to all and any operations of the devices. There are also more specific and in-depth configurations for certain features of each device. All of these features may be configured via either code or via the _Phoenix Tuner_.

The config tab in the _Phoenix Tuner_ features all the configurations available for each device.

![config tab](https://pro.docs.ctr-electronics.com/en/stable/_images/tunerx-configs1.png)

Device configurations can vary widely between different devices, as they may be of a different type and support different capabilities. Generally though, motor controllers share a mostly similar set of configurations.

All configurations are persistent, setting them to the device will save them and the device will keep these settings post reboot. This may be problematic when trying to test things out or when working with a device others worked with. Restoring the device settings to factory default will return all the settings to how they were originally (when the device was made).

As a standard operating procedure, we would usually want to restore settings to factory default before applying the wanted configuration.

#### Basic Configuration

The following are basic configurations available for all devices:
- `ID` is the CANBus identifier of the device. It is used to distinctly identify the device and communicate with it. This value should be unique per device. This value is a non-negative integer.

The following configurations apply only to motor controllers:
- `NeutralMode` describes the behaviour of the motor when entering idle motor. Idle motor refers to the motor output being at 0. Basically, when the motor is stopped. 
  - `Coast` the standard operating mode. In this mode, when entering idle mode, the motor simply stops receiving power and will do nothing. If the motor has momentum, it will continue rotating freely until such momentum is lost.
  - `Brake` In this mode, when entering idle mode, the motor controller shorts out the motor's power connections, resulting in the motor forcibly refusing to allow the stator to rotate. In affect, the motor will resist any attempt to rotate the shaft, and will immediatly negate any momentum the shaft had. This mode can be used to keep the shaft in place. Brake mode works in disabled mode due to the motor not actually requiring any outside current.
- `Inverted` determines if the motor output should be inverted (i.e. the motor will turn to the opposite direction, plus direction will act as minus direction and minus as plus).

> [!WARNING]
> Brake mode can seriously increase the temperature of the motor due to how power is generated. Be careful when using it.

#### Code Initialization and Basic Configuration

We've already initialized and used the TalonSRX and VictorSPX in the past, it is a simple matter of creating new instance of their classes:
```java
public class SomeSystem extends SubsystemBase {

  private final WPI_TalonSRX motorTalon;
  private final WPI_VictorSPX motorVictor;

  public SomeSystem() {
      motorTalon = new WPI_TalonSRX(0);
      motorVictor = new WPI_VictorSPX(1);
  }
}
```

For here we can configure and operate the motor controller as we like. As mentioned before, we should start by resetting the settings to factory default:
```java
public class SomeSystem extends SubsystemBase {

  private final WPI_TalonSRX motor;

  public SomeSystem() {
      motor = new WPI_TalonSRX(0);
      motor.configFactoryDefault();
  }
}
```

Only after restoring the settings should we start modifying the settings as wanted. Most settings should be set in the constructor when initializing the motor, so that the settings affect our operation of the motor throughout the robot code.
```java
public class SomeSystem extends SubsystemBase {

  private final WPI_TalonSRX motor;

  public SomeSystem() {
      motor = new WPI_TalonSRX(0);
      motor.configFactoryDefault();
      motor.setInverted(true);
      motor.setNeutralMode(NeutralMode.Brake);
  }
}
```

### Built-In Sensors

### Feedback Devices

### Limits

### Control Modes

### Links

- [Phoenix V6 Docs](https://pro.docs.ctr-electronics.com/en/stable/docs)

