
The Java Debugger tool packaged with _Intellij IDEA_ is quite a powerful and capable tool. But it can be quite intimidating for first time users, as it has much information and controls. In this document, we will look at some of the basic features and how to use them.
As debuggers are cornerstone tools for debugging and solving problems, proper usage of a debugger can save us a ton of problems.

Debugging a program is built upon two basic parts:
- interface: runs in the program (or part of the operating system or chipset) and allow you to interact with it - issuing command and receiving information about the program. 
- debugger: runs as a seperate program and communicates with the interface. Through it we send commands and receive information about the program.  

The interface part is generally just exporting basic capabilities to the debugger. The debugger must do most of the heavy lifting of how to use these simple functionalities to provide the user with strong debugging capabilities.

## Concept

The idea of a debugger is to allow a remote user to view the execution of code in real time. And by view, we are refering to viewing several pieces of information:
- The actual line of code being executed at any given moment.
- The state of the memory
  - values of variables (local/memebers/globals)
- The execution stack
  - which method calls which and in what order
- The state of threads running the application  

Using these pieces of information, we can construct a picture of what is going on in the code and why. We can, for example see if the code entered an `if` condition or its `else` and such.

But code executes quickly, and there is generally a lot of code to look at. This makes it difficult to observe the code in real time. As such, debugging is usually done in two modes:
- running: in this mode the code just runs normally. We usually put the program in this mode when we don't care about what it is doing at the moment, perhaps because we are waiting for a specific thing to occur. 
- paused: in this mode, we the users, run the code line by line. This mode is used for the parts of the code that we actually want to analyze. Only here can we actually analyze and read information about what is going on.

Consider the following code:
```java
switch (index) {
    case 1: {
        Account account = readAccount(in);
        if (account == null) {
            return false;
        }

        bank.addAccount(account);
        return false;
    }
    case 2: {
        Account account = selectAccount(bank, in);
        if (account == null) {
            System.out.println("BAD INPUT");
            return false;
        }

        printAccount(account);

        return false;
    }
}
```

We want to observe and check the code under `case 2`. So we will let the code run until it reaches this code, and then we will pause the code so we can analyze what is going on step by step.

## Working with the Intellij Debugger

### Start Up

To use a debugger, we must first have a process (code) to debug, and have the process allow itself to be debugged. We can either run a new process, configuring it to be debuggable (allow attaching a debugger); or use an already running process (which allows debugger attaching) and attach to it.

Regardless of the circumstance, which depends on the specific use and thus will not be discussed, we end up with a debugger attached to a process. This allows the debugger (in intellij) to send commands and receive information to and from the debugger agent (in the process). Using this
we can track and manipulate the execution of the code, allowing us to find and understand problems or the code in general.

When debugging a process, we should make sure we have the source code if this process in our currently open intellij windows. Otherwise we won't be able to view and analyze the source code. Typically, for debugging, either deploy the code
for debugging from the project first, or run the process for debugging in Intellij.

Typically, the debugger is started via the debug button (green bug) in IntelliJ

![debug button](https://github.com/user-attachments/assets/7facee2d-1092-4b33-9c28-8aa8f3402c2f)

### Breakpoints

Breakpoints define specific lines of code where we want the debugger tool to pause the program and allow us to analyze the execution. This is one of the primary methods of switching between running and paused modes. 

It's actually a pretty great mechanism because we just need to specify a specific line of code and the debugger will automatically pause the program for us. Doing so manually is nearly impossible.

To place a breakpoint on a line, simply click next to the line number and a red circle will appear (click again to remove). Now the code will stop at the next time it reaches this point. Do note that you can place breakpoints before
and during debugging. 

![breakpoint](https://github.com/user-attachments/assets/8540e67b-6226-44c0-b6c4-d16d38ce6093)

Now that the line was reached, the program was paused and the debugger allows us to control it

![break into debugger](https://github.com/user-attachments/assets/43dc7036-e08b-49d6-a3cc-635b508e771d)

### Paused Control

When paused, the program is stopped on a specific line of code at any given moment. The program does not resume or continue execution on its own, it must receive instructions from us about what to do. 
So when paused, we can start collecting and examining information. Let's see how.

The blue line is the one we are currently paused on:

![paused line](https://github.com/user-attachments/assets/1e9b9f86-1a98-4dfa-9ad3-1fa885029418)

The following is the debugger tab in Intellij

![debugger tab](https://github.com/user-attachments/assets/d8080d38-9ee1-4989-94c0-73c347cf7f6d)

This tab is made up of many many controls, some of the main ones are:
1. variable view: allows viewing and changing values of variables. This will show variables available for the current line the debugger is paused on
2. execution flow control: allows to move the code forward, basically run it, but in specific steps
3. stack/thread view: shows the current method stack and allows switching between working threads
4. execution buttons: additional set of buttons to control program execution and do some other things

#### Controlling Flow

![image](https://github.com/user-attachments/assets/2780d240-bc06-4697-89b8-294fd867170c)

Once the code is paused, it isn't running anymore. But leaving it paused doesn't help to debug a program, we need to be able to see what's going on line-by-line. As such, debugger provide us with several commands to control
the execution flow of the program. There are several basic commands:
1. Resume: unpauses the program and allows it to resume running normally (until it is paused again)
2. Pause: pauses the execution of the program from normal run
3. Stop: stops the program exeuction forcibly (kills the program)
4. Step Over: execute the current line of code and step to the next one.
5. Step Into: execute the current line of code and step to the next one. If the current line is a method call, instead, enter the method and wait on the first line.
6. Step Into My Code: execute the current line of code and step to the next one. If the current line is a method call, instead, enter the method and wait on the first line. If the method code is not from the current project (e.g. from a library), resume until reaching a line from the current project.
7. Step Out: continue the execution of the code until exiting the current method (returning from it)

Let's take a look at the _Step Over_ command. I've placed a breakpoint at a specific line in the program.

![image](https://github.com/user-attachments/assets/fc38e281-9bee-43da-91e4-7df1bc8f51c0)

If I press on _Step Over_, it should execute the current line and move forward. 

![image](https://github.com/user-attachments/assets/15451631-3552-4f84-9851-9496c4dcb48b)

It executed the line and moved to the next. The previous line was an `if` checking on the `index` variable. We know that its value was `2`, and thus the `if` condition was not met, meaning that the next line of code is after that.
Let's step again.

![image](https://github.com/user-attachments/assets/0ddba91b-bbe9-4723-a21e-6edbb66e5ddf)

We are now several lines ahead. This is because the previous line was a switch on `index`, so it jumped us to the `case 2` code. Let's do another one.

![image](https://github.com/user-attachments/assets/3165945f-cbae-4b8c-9dbb-dab05eb68e36)

Now we are after the method call. This is the basic idea of stepping, and specifically _Step Over_. The other stepping types are similar in concept, but with specific difference. _Step Into_, for example, would've entered the method instead of waiting for it to finish

![image](https://github.com/user-attachments/assets/a9f52820-b684-4fec-8cca-798715ec16f1)

#### Viewing Variables

As we know from writing code: variables in code are only available for certain parts of the code. A private variable in class can only be accessed from code in the class, for example. The same is not really true for debuggers. 
Debuggers will generally be able to access any variable, assuming it still exists. When we are paused on a line, will generally see the local variables and member variables (if paused in a class instance). This gives us a pretty flexible 
control, and it makes sense to have this, since it can help us understand what is going on.

![image](https://github.com/user-attachments/assets/69816827-89e7-435c-bdee-61cb832153b8)

In this example, the line of code has access to 3 local variables
- `bank`: referencing an instance of `Bank`
- `in`: refrencing an instance of `Scanner`
- `index`: holding the value `2`

We can expand the information about the `bank` variable and see what the instance contains

![image](https://github.com/user-attachments/assets/68f60bc3-9f66-4659-b7d4-6aaf671c1bf2)

Seems it contains an empty `ArrayList` member called `accounts`. This member is `private`, but we can still see it through the debugger.

There's actually a whole set of things we can do with a variable

![image](https://github.com/user-attachments/assets/c8b85ff6-0d30-4cef-b3bf-8cdbb36158b2)

But most of the time we will just be examining their value. The other options allow for more advanced debugging, including actually manipulating the flow of a program to cause it to do something. But let's just take a look at something call we can do.

I've re-started the program and stopped it on an earlier line

![image](https://github.com/user-attachments/assets/b763349a-77dd-4245-8115-c4fbed606543)

In the previous line it read a value from the user, indicating some index in a menu. The user provided the index `2`. Let's see what happens if we change it.

![image](https://github.com/user-attachments/assets/a413f7ee-93ba-414e-a9b8-f717f9abcc45)

![image](https://github.com/user-attachments/assets/e0ab3f0d-279d-489a-9f27-ccb21e06b82f)

Now, let's resume the execution of the program

![image](https://github.com/user-attachments/assets/028b7325-9261-4a87-89b4-9e8e24f48b5a)

And now we get

![image](https://github.com/user-attachments/assets/2dd14b8d-a62e-4d3d-9bf2-6bf592e3be9d)

Because the index was changed to an invalid values, so the `if` caught it and printed that there is a _bad input_. Pretty cool what we can do here.

## Links

- [Intellij - Debugging](https://www.jetbrains.com/help/idea/debugging-code.html)
- [Debugging Java code in IntelliJ IDEA](https://www.youtube.com/watch?v=V5iQ1FyRtBo)
