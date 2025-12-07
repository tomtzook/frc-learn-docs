When applying a _control loop_ to control a system (using _PID_ as an example), our loop will typically attempt to reach its _setpoint_ as fast as possible. But at their core, the output 
calculation will provide us with a desired output according to the function, which will be a great output in theory, but this pays little attention to what is physically possible by the system.

After all, implementations, like the one in _PID_, look at the system via the state, with absolutely no knowledge of the real 3D mechanics in play. This may not seem like a problem, and some times it really doesn't matter much.

But consider: our system starts at position 0 and we want to go to position 5. We start our _pid_ loop which gives an output of `1`. So this means we are going to order our motor to go from 0% to 100%. This is a problem. What essentially happens is that we force the motor to accelerate at potentially dangerous speed, and the system it operates will have to go through it as well. This will put high pressure on the components and could easily damage some of them. The same occurs when going from 100% to 0% (doubly so with _brake_ mode), with the system abruptly stopping and throwing the chassis around if it has enough force.

For the reasons above, it is typically advised to slowly ramp up or down the motor. Going from 0% to 10%, 20%, 30%, etc. This essentially complicates our _control loop_, but for good reason.

We can descirbe the wanted behaviour of the motor (position, velocity, acceleration) under these requirements as such:

<p align="center">
  <img width="390" height="455" alt="image" src="https://github.com/user-attachments/assets/e0510d95-5883-44c1-99fd-235822835c21" />
</p>

- We start at 0 position, velocity and acceleration
- _Acceleration Phase_: Accelerate the motor until reaching maximum wanted velocity
- _Cruise Phase_: staying at the maximum velocity, contine moving
- _Deceleration Phase_: Decelerate the motor until stopping
