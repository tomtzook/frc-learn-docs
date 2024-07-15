
_Cross The Road Electronics_ (_CTRE_) are one of the major suppliers for FRC. They develop, manufacture and sell a bunch of devices which we use with our robot. From the control system components like the _PDP_, _VRM_ and _PCM_ to motor controllers like _TalonSRX_ and motors like _Falcon 500_.
They've been around for a long time and were actually the first to produce a _smart_ motor controller. 

Because they make a lot of _smart_ devices, there is actually a lot to look at in terms of capabilities and control/config code. However, luckily they've tried to make most devices expose a shared abstract control interface, making most code similar despite the device being different.

_Pheonix_ is the name of the series of motor controllers and sensors they produce for FRC use. In the past, we where using the _V5_ version of the control software. In 2024 they've released the _V6_ version. 

We will be dividing the devices used into two categories: Phoenix _V5_ an _V6_. 

Pheonix _V5_ devices are older devices which simply did not receive the _V6_ update, these include:
- TalonSRX
- VictorSPX
- Pigeon 1

Phoenix _V6_ devices used to function with _V5_, but received an update which compels and requires users to switch to this new version. These devices are still supported by CTRE, and include:
- TalonFX
- CANCoder
- Pigeon 2

Writing code to and using Pheonix _V5_ and _V6_ is different, and as such we will be learning to use both. Luckily, devices in the same framework version share many base similarities, so this should make working with them a bit easier.

> [!WARNING]
> CTRE devices typically have the ability to work with each other over the CANBus.
> It is not certain if this functionality works well between devices with different firmware versions (_V5_ and _V6_).

| *TalonSRX* | ![TalonSRX](https://www.vexrobotics.com/media/catalog/product/cache/d64bdfbef0647162ce6500508a887a85/2/1/217-8080.jpg) |
|:--:|:--:| 
| *VictorSPX* | ![VictorSPX](https://cdn.andymark.com/product_images/victor-spx-speed-controller/5cdc1cd6fe93c623522f4e9c/zoom.jpg) |
|:--:|:--:| 
| *TalonFX and Falcon 500* | ![TalonFX](https://cdn11.bigcommerce.com/s-7cuph2j78p/images/stencil/1280x1280/products/134/643/3__19352.1673993701.png?c=1) |

### Phoenix Tuner

The [_Phoenix Tuner_](https://pro.docs.ctr-electronics.com/en/stable/docs/tuner/index.html) allows reviewing, configuring and controlling _Pheonix Devices_. 

It has two versions, the obsolete _Phoenix Tuner V1_ and the new _Phoenix Tuner X_. The _Tuner X_ should be the ones used normally and it is capable of supporting both _V5_ and _V6_ firmware. The _V1_ can be used with _V5_ devices only and should only be used with devices running old firmware such that they are not recognized by the _Tuner X_. 

To use the software, connect a USB-B cable from the controlling computer to the RoboRIO's USB-B port. It will allow control and access to all the _CTRE_ devices on the CANBus. Once connected, you will be able to select a specific device and configure or operate it.

![Phoenix Tuner X](https://pro.docs.ctr-electronics.com/en/stable/_images/highlighting-ip-area1.png)

> [!NOTE]
> Phoenix Tuner X now supports connecting to the robot over Wifi (via the Radio). Simply connect the computer to the robot Radio and launch the Tuner.

### Firmware

The firmware is the software (code) running on the devices. It is booted automatically (and quickly) when it receives power.

This firmware can and should be regularly updated, as _CTRE_ releases new versions with fixes and improvements. Use the _Phoenix Tuner_'s update functionality to do so.

![client update tab](https://pro.docs.ctr-electronics.com/en/stable/_images/selecting-firmware-version1.png)

> [!NOTE]
> For each season, CTRE typically releases new versions of the firmware. Make sure to check and update at the start and during the season.

> [!NOTE]
> Only Phoenix V6 devices will be updated from this year (2024) going forward.

### Configurations

There are several basic global configurations for the devices. These configurations apply to all and any operations of the devices. There are also more specific and in-depth configurations for certain features of each device. All of these features may be configured via either code or via the _Phoenix Tuner_.

The config tab in the _Phoenix Tuner_ features all the configurations available for each device.

![config tab](https://pro.docs.ctr-electronics.com/en/stable/_images/tunerx-configs1.png)

Device configurations can vary widely between different devices, as they may be of a different type and support different capabilities. Generally though, motor controllers share a mostly similar set of configurations.

All configurations are persistent, setting them to the device will save them and the device will keep these settings post reboot. This may be problematic when trying to test things out or when working with a device others worked with. Restoring the device settings to factory default will return all the settings to how they were originally (when the device was made).

As a standard operating procedure, we would usually want to restore settings to factory default before applying the wanted configuration.

The specific configurations of each device vary, as each device has a different set of functionality. Though similar devices (motor controllers) do tend to share some functionality and thus configuration.

### Devices and Capabilities

_CTRE_ devices has pretty impressive set of capabilities. Motor controllers include features like PIDF control loops, motion profiling, remote sensor connectivity and more. 

#### Motor Controllers

| Name | Supported Motors | Integrated Encoder | Firmware Version | Data Port Support | Remote Sensor Support | Code Class |
| ---- | ---------------- | ------------------ | ---------------- | ----------------- | ----------------------- | ---- |
| *TalonSRX* | Any Brushed Motor | No | V5 | Limit Switches, Quadrature Encoder, Analog Sensor | Yes, over CANBus | `com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX` |
| *VictorSPX* | Any Brushed Motor | No | V5 | _ | Yes, over CANBus | `com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX` |
| *TalonFX* | Falon 500 Brushless / Kraken Brushless | Yes (Falcon 500 / Kraken encoder) | V6 | Limit Switches | Yes, over CANBus | `com.ctre.phoenix6.hardware.TalonFX` |

#### Encoders

| Name | Type | Medium | PPR | Connection | Code Access | 
| ---- | -----| ------ | --- | ---------- | ----------- | 
| *CANCoder* | Relative and Absolute | Magnetic | 4096 | CANBus | Via `com.ctre.phoenix6.hardware.CANCoder` |
| *SRX Magnetic Encoder* | Relative and Absolute | Magnetic | 4096 | Quadrature (relative) / Pulse Width (absolute); Uses a special cable (Gageteer) | Accessed via the feedback devices code of device the sensor is connected to (e.g. _TalonSRX_) |
| *Falcon 500 Integrated Encoder* | Relative and Absolute | Magnetic | 2048 | No External connection (directly connected to TalonFX) | Access via the _TalonFX_ feedback devices code |
