## Deploying to Robot

To make your code run on the robot, you must first _deploy_ this code to the robot. Make sure the robot is on and connect your computer to the robot, either via a cable (Ethernet, USB-B) or via the Radio (connect to the WIFI). Once connected you can deploy the code via _Intellij_ in several methods or outside of _Intellij_ if necessary.

### Via the Terminal in Intellij IDEA

Open the _Terminal_ tab in _Intellij IDEA_

![terminal tab button](https://github.com/user-attachments/assets/95e65d8c-89ec-479b-a980-c6ccb20a1acf)

![terminal tab](https://github.com/user-attachments/assets/12a47653-bfca-4aab-9886-441cb7da85de)

Once open, you can enter commands here. On Windows, enter the following command: `.\gradlew.bat deploy` and press _ENTER_. 

![deploy task command](https://github.com/user-attachments/assets/09c25414-ba65-43b5-bb15-6c97ff1d22e5)

This will run the deployment task, which may take a while to run, but you should see outputs detailing the process.

![example of output](https://github.com/user-attachments/assets/bdf2542a-7557-443e-a2fd-9af42190425f)

### Via Gradle Tasks in Intellij IDEA

Open the _Gradle_ tab in _Intellij IDEA_, then click on _Execute Gradle Task_ and select the _deploy_ task. 

![gradle tab button](https://github.com/user-attachments/assets/e02c959b-73eb-4930-b748-e988d3c40115)

![gradle tab](https://github.com/user-attachments/assets/5409d5f2-59dc-4ecb-a81b-f4d6163e8d09)

![gradle run task deploy](https://github.com/user-attachments/assets/25790227-f2fc-4d65-8e0c-0d1a38a8bb4b)

This will run the deployment task, which may take a while to run, but you should see outputs detailing the process.

![example of output](https://github.com/user-attachments/assets/ac7019aa-1f3b-4c1d-98b4-acb8a52588e1)

### Understanding the Deployment Task


Deployment failure example

![image](https://github.com/user-attachments/assets/deef6782-fa53-4f50-9b9f-d6d11804484c)

### Deploying with Debugger
