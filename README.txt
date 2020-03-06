SYSC 3330 A3 - Group 9
*****************************

LINK TO REPOSITORY
****************************
https://github.com/loisaleghe/elevatorSimulator

Group Members
***********************
* Abdul-Rahmaan Rufai
* Ediomoabasi Emah
* Hilaire Djani
* Lois Aleghe 
* Mohammed-Yasir Suara

Classes
*************************

* Direction
  Specifies the directions that can be called from the floor (the UP and DOWN buttons).

* ElevatorSubsystem
  Receives data from the scheduler and sends data to the scheduler.

* FloorData
  The class houses the constructor that puts the data from each floor into the desired format.

* FloorSubsystem
  Reads the data from the file and sends it to the scheduler.

* Scheduler  
   Acts as a server. It ensures the communication between the clients, which are the Elevator Subsystem and the Floor Subsystem.
   
* Elevator 
   Represents the elevators in the system
   
* Floor
   Represents the floor in the system
   
* FloorQueue
   Stores and manages an array list of floors that an elevator will visit


	

Other Files and Folders:
*************************

* floorRequests.txt
  	File from which data will be read.

* floorRequestTest.txt
  	File used to text functionality for reading data from a file.

* group_roles.txt
  	File containing contributions of each team member

* UML_Diagrams
	Folder containing UML sequence and class diagrams

* state_machine_diagrams
	Folder containing the state machine for the Schedular and elevator Subsystem

Set-Up Instructions
*****************************

* Extract project zip folder
* Import the file to eclipse
* Ensure that the package is elevatorSimulator
* Run program through the Scheduler class

Flow
*************************

1. Elevator arrives on a floor
2. Elevator sends arrival signal to ElevatorSubsystem
3. Elevator subsystem notifies schedular about elevator arrival
4. Schedular fetches floor requests for elevator's current floor and sends these to the ElevatorSubsystem
5. If there are floor requests, ElevatorSubsystem instructs elevator to move to specified floors.
6. At the same time, FloorSubsystem continiously sends floor requests to the schedular.
7. Program keeps running until there are no more floor requests and elevator has visited all of it's floors

Test Set-Up Instruction
****************************

* To run the test cases, Open Eclipse and run the AllTests class found in the "elevatorSimulatorTests" package.
* All tests should pass.


