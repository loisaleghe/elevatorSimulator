SYSC 3330 A3-9 
Group Memebers: Abdul-Rahmaan Rufai, Ediomoabasi Emah, Hilaire Djani, Lois Aleghe, Mohammed-Yasir Suara

Classes:
-> Direction
 : Specifies the directions that can be called from the floor (the UP and DOWN buttons). 

-> ElevatorSubsystem
 : It receives data from the scheduler and sends data to the scheduler.
 
-> FloorData 
 : The class houses the constructor that puts the data from each floor into the desired format.

-> FloorSubsystem 
 : Reads the data from the file and sends it to the scheduler.

-> Scheduler  
 : It acts as a server. It ensures the communication between the clients, which are the Elevator Subsystem and the Floor Subsystem.

Set-Up Instructions:
* Import the file to eclipse
* Ensure that the package is elevatorSimulator
* Run the Scheduler.java class

Flow:
* FloorSubsystem sends the reads the data from an input file and sends it to the Scheduler.
* The ElevatorSubsystem gets the data from the Scheduler and sends it back to the Scheduler.
* The FloorSubsystem then gets the Data from the Scheduler.

Test Set-Up Instruction:
* To run the test cases, Open Eclipse and run the AllTests.java class.
* All tests should pass.
