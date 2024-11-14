## Command-Based Programming

Command-Based programming is an FRC programming paradigm meant to allow easier control and implementation of complex operations on the robot, by breaking down the operations of the robot into smaller parts and managing them through a scheduler. 

Normally, we are limited to implementing the logic for multiple systems, each with different capabilities and operations, into our robot class. However, this is difficult as the code becomes more and more complex. 

Consider a system that collects balls from the floor: it has an operating motor and a sensor to detect that the ball is held in the system. So it would need 3 operation modes: do nothing, collect ball, spit ball out. These modes are controlled by the HID and the sensor state. Now this is just one system, a robot would normally have multiple. So the code quickly becomes more complicated. Instead than, we use command-based programming to help us handle this.

#### Systems

In general, this paradigm follows the concept of dividing the robot code into specific _systems_, each with seperate functionalities and control code. This helps us manage the code better as everything will be divided into smaller blocks. But it also helps us create association between physical devices (like motors) and the systems they belong to, which is important and will be discussed more later.

To create a system, we create a new class and fill it with code necessary to that system's operation. To define a normal class as a _subsystem_ it must extend the `SubsystemBase` class. We'll follow an example of coding a tank drive system:
```java
public class DriveSystem extends SubsystemBase {

}
```

Like all classes, subsystem classes are composed of
- members: our memebers will generally be components that make up the system. Like sensors or motor controllers. But also any other member necessary for the functionality of that class.
- constructors: to initialize the members and the class for use. 
- methods: to operate our system by operating its components.

For this system we'll have four _TalonSRX_ motor controllers arrayed in a 4x4 tank configuration. So we'll start by defining the members:
```java
public class DriveSystem extends SubsystemBase {

    private final WPI_TalonSRX frontLeft;
    private final WPI_TalonSRX backLeft;
    private final WPI_TalonSRX frontRight;
    private final WPI_TalonSRX backRight;
}
```

We'll continue with our constructor to initialize the members. We can either receive the members in the constructor as parameters or just initialize them inplace. We'll do the latter here, but the choice is yours:
```java
public class DriveSystem extends SubsystemBase {

    private final WPI_TalonSRX frontLeft;
    private final WPI_TalonSRX backLeft;
    private final WPI_TalonSRX frontRight;
    private final WPI_TalonSRX backRight;

    public DriveSystem() {
        frontLeft = new WPI_TalonSRX(0);
        backLeft = new WPI_TalonSRX(1);
        frontRight = new WPI_TalonSRX(2);
        backRight = new WPI_TalonSRX(3);
    }
}
```

Next come the methods. We need to supply methods which allow operation of the system. Which methods we have and what they do differs greatly between systems and programmers, as it relates to what the system should be able to do. In our system, we just have four motors which are operated together, so we need a method to give them commands together. Additionaly, we need a method to make the motors stop moving:
```java
public class DriveSystem extends SubsystemBase {

    private final WPI_TalonSRX frontLeft;
    private final WPI_TalonSRX backLeft;
    private final WPI_TalonSRX frontRight;
    private final WPI_TalonSRX backRight;

    public DriveSystem() {
        frontLeft = new WPI_TalonSRX(0);
        backLeft = new WPI_TalonSRX(1);
        frontRight = new WPI_TalonSRX(2);
        backRight = new WPI_TalonSRX(3);
    }

    public void move(double speedLeft, double speedRight) {
        frontLeft.set(speedLeft);
        backLeft.set(speedLeft);
        frontRight.set(speedRight);
        backRight.set(speedRight);
    }

    public void stop() {
        frontLeft.stopMotor();
        backLeft.stopMotor();
        frontRight.stopMotor();
        backRight.stopMotor();
    }
}
```

It should be clear that the subsystem doesn't actually operate our system, but rather lets something else do that. It should expose functionalities for others to use. We'll see soon what uses those methods.

With our system class ready, we need to create an instance for it in our robot class, so it could be used:
```java
public class Robot extends TimedRobot {

    private DriveSystem driveSystem;

    @Override
    public void robotInit() {
        driveSystem = new DriveSystem();
    }
    ...
}
```

Only one instance can be created for a single subsystem. Creating more than one will create problem with controlling and operating the system properly.

#### Commands

Subsystems describe and expose functionality of the system. But commands are the true power here. They are the ones that will operate our systems to perform different tasks. Any task we want to do in the robot will be done via a command. Each command is a managed piece of code that uses one or more subsystems.

One command can do anything from rotating a motor to following a curved path. It is up to us to decide and implement what we want to do. They are managed and executed by the `CommandScheduler`. 

Each command is also a class, which extends `Command` and has to implement a set of methods which are called by the scheduler to run the command: `initialize`, `execute`, `isFinished` and `end`. We can of course add more methods to it, but these methods are a must for us to have any sort of operation run. 

a basic template of a command looks like this:
```java
public class SomeCommand extends Command {

	public SomeCommand() {
	}

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean wasInterrupted) {
	}
}
```

![command flow](https://github.com/user-attachments/assets/653fd69e-815e-4b12-8cda-d09ab8b10ae5)

Each command has a very specific flow since it starts:
- The initialization phase runs with `initialize` being called. We use this part to prepare for the execution of the Command. This can be anything from initializing variables to changing hardward configuration. Each Command is unique and will do different things (or do nothing).
- Then the execution phase runs with `execute` being called periodically (every ~20ms). This is the primary part of the Command where we perform our main logic. It is periodic as to allow us to perform different things as the Command continues running. This can be changing the speed of motors or more. 
- This continues until the Command stops running. It can be stopped gracefully or be interrupted from an external source.
- When the Command does stop (for whatever reason) `end` is called. Here we need to stop the work of the Command. This can be stopping motors, changing hardware configuration, or more. In the command above we stop the rotation of the motor.

Once we've implemented a command code, we can use it by creating an instance of the class and starting the command. This will place the command for execution in the scheduler until it is stopped for whatever reason:
```java
public class Robot extends TimedRobot {
    ...
    @Override
    public void teleopInit() {
        SomeCommand command = new SomeCommand();
        command.schedule();
    }
    ...
}
```

The call to `schedule` starts the command. We do this in `teleopInit` and not periodic because we don't want to start the command again and again, we want to start it just once.

Let's make a command for our drive system to move our robot forward:
```java
public class DriveForward extends Command {

    	private final DriveSystem driveSystem;
    	private final double speed;

	public DriveForward(DriveSystem driveSystem, double speed) {
        	this.driveSystem = driveSystem;
        	this.speed = speed;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void execute() {
        driveSystem.move(speed, speed);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean wasInterrupted) {
        driveSystem.stop();
	}
}
```

We use `execute` to give our subsystem the move command and in `end` we stop our system from moving. We don't touch `isFinished` or `initialize` in this example.

So to start this we can create an instance of the command and run it:
```java
public class Robot extends TimedRobot {
    ...
    @Override
    public void autonomousInit() {
        Command command = new DriveForward(driveSystem, 0.5);
        command.schedule();
    }
    ...
}
```

##### Command - Stops

As mentioned, commands can either stop gracefully or be interrupted.

For graceful stop, the command must report that it wants to stop. This is usually because the command has decided it should end, probably because it finished. To do this, it uses the `isFinished` method. This method returns a `boolean` value, so if it returns `false` the command doesn't want to finish, and return `true` when it does want to finish.

So:
```java
	@Override
	public boolean isFinished() {
		return false;
	}
```
makes a command that _never_ ends gracefully. This is usually done for commands which don't have a specific end reason. For example, a command that just rotates our drive system doesn't really have a normal reason to stop.

But: 
```java
	@Override
	public boolean isFinished() {
		return true;
	}
```
makes a command that will end immediately. This will cause `initialize` and `execute` to both run once, and then the command will finish. That gives us a command that runs one logic set and ends. Which is definetly useful if we want to perform a logic that just does a quick thing and exits. 

For an actual condition:
```java
	@Override
	public boolean isFinished() {
		return getDistancePassedMeters() > 2.0;
	}
```
This will make the command stop after we have passed 2 meters (probably measured with an encoder). So it will be relevent for commands that perform a specific objective and use sensors to see if they finished accomplishing that. For an example from our 2024 robot: when we collect a note, we rotate the collector until a sensor detects we have collected the note and then we finish.

So this is a normal finish. For interruption, it refers to any stop of a command which isn't caused from `isFinished` returning `true`. This can be:
- due to a requirements conflict
- or via a call to .cancel

It is usually done to influence the command from an outside source (not from the command itself). For example, if we want the command to stop once we've stopped pressing a button, we can check if the button is no longer pressed and then simply cancel it by calling `command.cancel`.
```java
	Command command;

    @Override
    public void teleopInit() {
    	command = new DriveForward(driveSystem, 1.0);
    	command.schedule();
    }
    
    @Override
    public void teleopPeriodic() {
    	
    }

    @Override
    public void teleopExit() {
    	command.cancel();
    }
```
This code will start the command when we enter `teleop` and stop it when we exit `teleop`.

##### Command - Requirements

On requirements conflict - lets first discuss about requirements.
Requirements are _dependencies_ of commands. That is, in order to run a command is using one or more subsystems. What happens if we have 2 commands (or more) which try to use the same subsystem at the same time? Well it depends on what the commands do, but if we take an example of one command that raises an arm and another that lowers the arm - what will happen? The motor will go crazy! That is because one command is telling it to rotate to one side (to lift the arm) while the other is telling it to rotate to the other side (to lower it) and thus the motor keeps getting conflicting orders from the 2 commands.

This is dangerous! as it could easily burn the motor. But also, we end up with an arm that doesn't work. So to solve this, we save in each command a set that includes all the systems it uses. That way, the scheduler of the commands can make sure that a subsystem is only used by one command at each time. This is done in the command by calling `addRequirements(system)` for each system used in the constructor of the command.
```java
public class DriveForward extends Command {

	private final DriveSystem driveSystem;
	private final double speed;

	public DriveForward(DriveSystem driveSystem, double speed) {
		this.driveSystem = driveSystem;
		this.speed = speed;

		addRequirements(driveSystem); // absolutely necessary
	}

}
```

So what happens when two commands want to use the same subsystem? Well usually this is done when one command is already running and the other command starting to run and requesting to use the same subsystem. In such a case, the new command will be given priority (by default) and the old command will be _interrupted_ and stopped.

##### Command - Instances

How are different instances of the same command class handled? Well, each instance is treated as its own command. So lets look at an example
```java
    @Override
    public void teleopInit() {
    	Command command = new DriveForward(driveSystem, 1.0);
    	command.schedule();

	    command.schedule();
    }
```
Well in this case, we call `schedule` twice. And what happends depends on the state of the command:
- the first `schedule` starts the command
- if the command is still running when we call `schedule` for the second time: then nothing will really happen, as the command is already running.
- if the command finished running when we call `schedule` for the second time: then it will start running again. That means that all the steps of the command will start again. So `initialize` will run again, and `execute` will run again. This means that a command can be reused freely.

But when we deal with two instances of the same command:
```java
    @Override
    public void teleopInit() {
    	Command command1 = new DriveForward(driveSystem, 1.0);
    	command1.schedule();

    	Command command2 = new DriveForward(driveSystem, 1.0);
    	command2.schedule();
    }
```
Well, in such a case, it is as we started two different commands. Even though the logic of the commands is the same, as they are instances of the same class, they still are treated as different commands.

##### Command - Modes

Commands normally are not allowed to run in `disabled` mode. This is a safety measure, to make sure we don't do things during `disabled`, as we are not supposed to. It is possible to allow special commands to run, but we will not discuss how for now.

What happends to the running commands when we change (due to a request from the driverstation) from one run mode to another; e.g. from `teleop` to `disabled`. This depends on which modes we change between. If we change to `disabled` mode, all non-approved commands will stop running because commands are not normally allowed in disabled mode. So in such a case, commands will stop running.

But when changing from `autonomous` to `teleop`? In such a case, commands will continue running, as they have no reason to stop running. This matters! as your commands may continue running without you realizing this. Luckily, during competition matches and normal use of the Driver Station, it is necessary to go to `disabled` mode before going to any other mode. So commands will be stopped because of that.

##### Examples for some Commands

Command that drives the system forward for 2 seconds:
```java
public class DriveForward extends Command {

    	private final DriveSystem driveSystem;
    	private final double speed;

	private double startTime;

	public DriveForward(DriveSystem driveSystem, double speed) {
        	this.driveSystem = driveSystem;
        	this.speed = speed;

		addRequirements(driveSystem);
	}

	@Override
	public void initialize() {
		startTime = Timer.getFPGATimestamp();
	}

	@Override
	public void execute() {
        	driveSystem.move(speed, speed);
	}

	@Override
	public boolean isFinished() {
		return Timer.getFPGATimestamp() - startTime >= 2;
	}

	@Override
	public void end(boolean wasInterrupted) {
        	driveSystem.stop();
	}
}
```


Command that lets the user operate the drive system with the xbox controller:
```java
public class DriveForward extends Command {

    	private final DriveSystem driveSystem;
    	private final XboxController xbox;

	public DriveForward(DriveSystem driveSystem, XboxController xbox) {
        	this.driveSystem = driveSystem;
        	this.xbox = xbox;

		addRequirements(driveSystem);
	}

	@Override
	public void initialize() {
	}

	@Override
	public void execute() {
		double left = -driveSystem.getLeftY();
		double right = -driveSystem.getRightY();
        	driveSystem.move(left, right);
	}

	@Override
	public boolean isFinished() {
		return false;
	}

	@Override
	public void end(boolean wasInterrupted) {
        	driveSystem.stop();
	}
}
```

##### Attaching Commands to Buttons

Sometimes (actually quite frequently) we would want to run commands when the robot operator presses a button. One simple example from 2024 season is when the operator wants to collect a note. Pressing a button to start the note collection is a simple way to operate the robot.

Buttons are basically binary, so they can either be pressed or not. That way we can make commands to run when they change between the _pressed_ and _non-pressed_ states. We can do this by using the `JoystickButton` class.

The following will attach the command `DriveForward` to run when the button _Y_ on an `XboxController` is pressed. 
```java
public class Robot extends TimedRobot {

	private DriveSystem driveSystem;
	private XboxController xbox;

    @Override
    public void robotInit() {
    	driveSystem = new DriveSystem();
    	xbox = new XboxController(0);

    	JoystickButton button = new JoystickButton(xbox, XboxController.Button.kY.value);
    	button.onTrue(new DriveForward(driveSystem, 1.0));
    }
}
```
So now, when the operator pressed on _Y_ (once), the command `DriveForward` while start running. The command won't stop on its own as its `isFinished` returns `false`. So really, it will never finish this way.
What happens when we press on _Y_ twice? It will try to start the command again, but because it is already nothing, nothing will happen.

We can also make a command run _while_ the button is pressed.
```java
public class Robot extends TimedRobot {

	private DriveSystem driveSystem;
	private XboxController xbox;

    @Override
    public void robotInit() {
    	driveSystem = new DriveSystem();
    	xbox = new XboxController(0);

    	JoystickButton button = new JoystickButton(xbox, XboxController.Button.kY.value);
    	button.whileTrue(new DriveForward(driveSystem, 1.0));
    }
}
```
In this case, the command will start when we _start pressing_ on _Y_. When we release the _Y_ button, the command will be stopped because its `.cancel` will be called automatically when the button is released.


## Further Reading

- [Command-based Programming - FRC Docs](https://docs.wpilib.org/en/stable/docs/software/commandbased/index.html)
