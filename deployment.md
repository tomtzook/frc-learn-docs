## Deploying to Robot

To make your code run on the robot, you must first _deploy_ this code to the robot. Make sure the robot is on and connect your computer to the robot, either via a cable (Ethernet, USB-B) or via the Radio (connect to the WIFI). Once connected you can deploy the code via _Intellij_ in several methods or outside of _Intellij_ if necessary.

### Via the Terminal in Intellij IDEA

Open the _Terminal_ tab in _Intellij IDEA_

![terminal tab button](https://github.com/user-attachments/assets/95e65d8c-89ec-479b-a980-c6ccb20a1acf)

![terminal tab](https://github.com/user-attachments/assets/12a47653-bfca-4aab-9886-441cb7da85de)

Once open, you can enter commands here. On Windows, enter the following command: 
```
`.\gradlew.bat deploy`
```
and press _ENTER_. 

![deploy task command](https://github.com/user-attachments/assets/09c25414-ba65-43b5-bb15-6c97ff1d22e5)

This will run the deployment task, which may take a while to run, but you should see outputs detailing the process.

![example of output](https://github.com/user-attachments/assets/bdf2542a-7557-443e-a2fd-9af42190425f)

### Via Gradle Tab in Intellij IDEA

Open the _Gradle_ tab in _Intellij IDEA_, then click on _Execute Gradle Task_ and select the _deploy_ task. 

![gradle tab button](https://github.com/user-attachments/assets/e02c959b-73eb-4930-b748-e988d3c40115)

![gradle tab](https://github.com/user-attachments/assets/5409d5f2-59dc-4ecb-a81b-f4d6163e8d09)

![gradle run task deploy](https://github.com/user-attachments/assets/25790227-f2fc-4d65-8e0c-0d1a38a8bb4b)

This will run the deployment task, which may take a while to run, but you should see outputs detailing the process.

![example of output](https://github.com/user-attachments/assets/ac7019aa-1f3b-4c1d-98b4-acb8a52588e1)

### Understanding the Deployment Task

In FRC Java projects we use the _Gradle Build Tool_. This tool is used to compile and perform tasks with our code. Among those tasks, we have the configured _deploy_ task. This task is meant to upload the compiled code to the robot and run it. After it is complete, the robot will have the new code running. This code will remain on the robot, and post reboot, the robot will start this code again.

First, this task requires a compiled code. This is done by first running the _build_ task, which compiles the code and stores it locally to be used. The _build_ task is rather "smart", meaning that it will only build the code if there were changes to the source code or if no build has occurred. Note that first time build may require internet connection to download dependencies, so for first build you should connect to the internet and run the _build_ task, similar to how you run the _deploy_ task.

Once the task has the compiled code to deploy, it uses the _ssh_ protocol to stop the old code, then it uploads the new code (along with some dependencies) via the _scp_ protocol, and starts the new code again with _ssh_. Once this process is complete, the new code should be running on the robot. But this can be a long process, so be patient and wait for it to finish completely.

![successful build](https://github.com/user-attachments/assets/a0ab0cbf-e0ef-473e-83ae-0f1b71227657)

A successful deployment will print `BUILD SUCCESSFUL`. 

Do note that the process may fail for a variety of reasons. First, the _build_ task may fail due to compliation errors. In that case you should address them and fix your code. Other then that, the upload and commands may fail too.

![deployment failure example](https://github.com/user-attachments/assets/deef6782-fa53-4f50-9b9f-d6d11804484c)

The `BUILD FAILED` message indicates that there was a failure along the way. For that we know that our code isn't running on the robot. The question that remains is why. Above the `BUILD FAILED` message we will find details of both what the task did and where it failed. To understand the message then, we would have to read and analyze this information. Some typical causes for failure are:
- The computer not being able to connect to the RoboRIO. This can be caused by serveral things
  - The computer is not connected to the robot (either over WIFI, Ethernet or USB-B).
  - The RoboRIO not being on (perhaps the robot is off, the RoboRIO is not connected to a power supply, or there is a fault)
- The compilation of the code failed.
  - Either the _build_ task failed to download dependencies (they either don't exist, or the computer is not connected to the internet)
  - Or the code actually has compilation errors in it (in that case, fix the errors. A list of them will be shown in the output of the task)
  - And there could be other kinds of problems.

The example below shows a failure to connect to the RoboRIO

![discover roborio failure](https://github.com/user-attachments/assets/90a1780a-48cd-454f-93f3-92cc8e54a7a5)


### Deploying with Debugger

The debugger is quite a useful tool. And generally, any Java process can have a debugger attached to it given the process being run with certain configurations for the JVM to allow a debugger.

For an FRC code, to allow attaching a debugger, we must deploy the code with the correct configuration. 

### Via the Terminal in Intellij IDEA

Open the _Terminal_ tab in _Intellij IDEA_ and run the deployment command with the `-PdebugMode` parameter:
```
`.\gradlew.bat deploy -PdebugMode`
```
(press ENTER to run)

This will run the deployment task but this time it will deploy to the robot with the JVM configured to allow debugger attachment

### Via Gradle Tab in Intellij IDEA

To configure the deployment task to deploy with debugger capability, we must create a new run configuration. Click on the configurations selection and click on _Edit Configurations_

![configuration selection](https://github.com/user-attachments/assets/d96fa28e-e779-4c85-9095-8fce2236d86f)

![edit config](https://github.com/user-attachments/assets/9fc1ac93-8152-4028-900c-c2b4bb7311db)

Create a new configuration of type _Gradle_

![create new gradle config](https://github.com/user-attachments/assets/e150ddcf-7aa7-4f40-ba36-c6a063b5e8c2)

Now select this configuration and edit the _name_ (select a recognizable name so you could find it later) and edit the run config to `deploy -PdebugMode` (run deploy task in debug mode)

![edit gradle task config](https://github.com/user-attachments/assets/ded4c1b2-8b6a-43f8-8c96-7281418680d0)

Click on _Apply_ and _OK_ to save this new configuration and exit the configurations dialog.
The current configuration should now be the newly created configuration. Make sure that it is (should show the name). If it isn't simply click on the box and select the configuration you created.

![configuration](https://github.com/user-attachments/assets/b20ff90e-8aad-41c9-ac06-8ccf4c1fb1b5)

Click on the green _Run_ arrow to run this new configuration. This will run the deployment task but this time it will deploy to the robot with the JVM configured to allow debugger attachment.

### Attaching Debugger

