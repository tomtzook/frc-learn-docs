and although they can come in all sort of configurations, they still retain similar basics; And among those, automated shooting is a typical goal.
But how can such thing be done?

The shooting process involves propelling an item at a certain direction, typically with the aim of scoring a hit or goal. 

## Firing Solution

The _firing solution_ defines the state required by the system for a hit to be scored. Essentially _set-points_ for each relevant controller that operate
the system, that if reached, should score a goal. This also means that we must have the capability to manipulate certain system states to get
capable shooters - the less states we can control, the less capable our automated shooting is.

What states the _firing solution_ must contain depend entirely on what can be controlled by the system, but should only be states which
can affect the result of the shooting in some way - a relationship must exist between scoring a hit and the system control.

The base relationship that can be used is based on the kinematics of an object thrown horizontally. Let us define that
- $x$ is the axis defining the items motion horizontally from the shooter to the goal.
- $x_0$ is the starting position along that axis
- $y$ is the axis defining the items motion vertically from the shooter to the goal.
- $y_0$ is the starting position along that axis
- $V_0$ is the velocity at which the item exits our shooter.
- $\alpha$ is the angle from $x$ axis at which the item exits our shooter.
- $g$ is the acceleration of the item along the vertical axis - it is the gravitational constant $9.7 \text{m/s}^2$ 

We can thus calculate the item's position at any given time $t$:

$$ x = x_0 + V_0 \cos(\alpha) t $$
$$ y = y_0 + V_0 \sin(\alpha) t - \frac{1}{2} g t^2 $$

In order to score a goal, the item must reach the goal's entry position with $x$ and $y$ at the same time.
**DRAWING OF THIS**

With the height of the goal entry being $H$, our shooter height $h$ and the horizontal distance from our shooter to the goal as $d = x - x_0$:

$$ d = V_0 \cos(\alpha) t $$
$$ H = h + V_0 \sin(\alpha) t - \frac{1}{2} g t^2 $$

From this, we can see several parameters influencing our ability to score a goal:
- $d$: The target's distance.
- $H - h$: The height difference between the shooter and the target.
- $V_0$: The initial velocity of the item being launched from the shooter.
- $\alpha$: The initial angle at which the item is being fired from the shooter.

Controlling any of these will provide us with the ability to perform automated shooting. 
One option could be controlling only $d$, forcing the shooter to be at a specific distance before shooting, and keeping the rest of the variables as constant. 
Another could be having all of the parameters as constant, forcing the shooter into a very specific state before shooting.

### Kinematics Based

#### Velocity Control

#### Angle Control

#### Velocity and Angle Control

#### Air Resistance

### Interpolation

### Integrating with a Turret
