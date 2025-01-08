
### Motors

Name | Image | Manufacturer | Type | Motor Controller to Use | Is Motor Controller Integrated? | Integrated Sensors
------|------|--------------|------|----------|--------|------
CIM | ![cim](https://github.com/user-attachments/assets/836f685a-71db-4661-a21b-3ab4dc2597c1) | VEX | Brushed | Any | No | -
NEO 1.1 | ![neo 1.1](https://github.com/user-attachments/assets/7dd6e612-4f04-427c-bb1a-5b4422e5ebf7) | REV | Brushless | SparkMax | No | Encoder, Temperature Sensor
NEO 500 | ![neo 500](https://github.com/user-attachments/assets/c2d17baa-9d4e-437c-a19c-4e46a7ec6538) | REV | Brushless | SparkMax | No | Encoder, Temperature Sensor
NEO Vortex | ![neo vortex](https://github.com/user-attachments/assets/f138cced-2e52-451c-921a-40504765f5c1) | REV | Brushless | SparkMax | No | Encoder, Temperature Sensor
Falcon 500 | ![falcon 500](https://github.com/user-attachments/assets/358d1c18-8c02-471c-942a-9a6a5428b047) | VEX/CTRE | Brushless | TalonFX | Yes | Encoder, Temperature
Kraken | ![kraken](https://github.com/user-attachments/assets/489b8de1-4880-417d-9797-7876897e46e4) | VEX/CTRE | Brushless | TalonFX | Yes | Encoder, Temperature

### Motor Controllers

Name | Image | Manufacturer | Interface to RoboRIO | Supported Motors | Sensor Ports | Support For Remote Sensors | Required Vendordep | Code Class
-----|-------|--------------|----------------------|------------------|--------------|----------------------------|--------------------|-----------
Talon SRX | ![talonsrx](https://github.com/user-attachments/assets/45fc5daa-1417-4049-ab0f-4923489d99c9) | CTRE | CANBus | Any Brushed Motor | Quadrature Encoder, Limit Swiches, Analog Sensor | Yes, Other CTRE Devices | _Phoenix5_ | `com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX`
Victor SPX | ![victorspx](https://github.com/user-attachments/assets/c3ac7dfe-d861-44ea-b9d9-6017831332d9) | CTRE | CANBus | Any Brushed Motor | -  | Yes, Other CTRE Devices | _Pheonix5_ | `com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX`
Talon FX | ![talonfx + falcon500](https://github.com/user-attachments/assets/1dfcd907-9af3-4b16-8e6f-a483924640ec) | CTRE | CANBus | _Falcon500_/_Kraken_ (Integrated) | Integrated Encoder, Limit Switches | Yes, Other CTRE Devices | _Phoenix6_ | `com.ctre.phoenix6.hardware.TalonFX`
Spark Max | ![sparkmax](https://github.com/user-attachments/assets/3ba2521e-372c-4dc0-a55b-1e0762d4e500) | REV | CANBus | Any Brushed Motor, _NEO_-Series Motors | Quadrature Encoder, Absolute Enoder, Limit Switches, Analog Sensor | No | _REVLib_ | `com.revrobotics.spark.SparkMax`

### Encoders

Name | Image | Method | Type | Resolution (Relative) | Resoluion (Absolute) | Interface (Relative) | Interface (Absolute) | Typically Connected To 
-----|-------|--------|------|-----------------------|----------------------|----------------------|----------------------|-----------------------
Through-Bore Encoder | ![throughbore](https://github.com/user-attachments/assets/d9d3c00c-7fa4-4059-b1df-ee9f9d0ef29a) | Magnetic | Relative, Absolute | 2048 PPR | 1024 | Quadrature | Pulse Width | -
SRX Magnetic Encoder | ![srx](https://github.com/user-attachments/assets/e33f40c2-827e-44a5-9041-4148e35de848) | Magnetic | Relative, Absolute | 1024 PPR (4096 CPR) | 4096 | Quadrature | Pulse Width | Talon SRX
NEO Integrated Encoder | ![neo](https://github.com/user-attachments/assets/fba94f61-589c-4bcc-a9ed-234ca1ff2f49) | Magnetic | Relative | 42 CPR | - | Quadrature | - | Spark Max, Spark Flex (only)
Falcon 500 Integrated Encoder | | Magnetic | Relative, Absolute | 2048 PPR | 2048 | - | - | Talon FX (only)
CAN Coder | ![canbus](https://github.com/user-attachments/assets/63e87f90-0b54-42ee-92c7-87f2f271e5af) | Magnetic | Relative, Absolute | 4096 PPR | 4096 | CANBus | CANBus | -

### IMUs

Name | Image | Manufacturer | Sensors | Features | Connection | Required Vendordep | Code Class
-----|-------|--------------|---------|----------|------------|--------------------|------------
Pigeon 2 | ![pigeon2](https://github.com/user-attachments/assets/ae9e3d40-638c-4e96-b38b-7ad7867122d4) | CTRE | 3-Axis Accelerometer, 3-Axis Gyroscope, 3-Axis Magnetometer | Pre-Calibrated, Full AHRS, Quaternion Output, Gravity Vector Output, Continous Yaw | CANBus | _Phoenix6_ | `com.ctre.phoenix6.hardware.Pigeon2`







