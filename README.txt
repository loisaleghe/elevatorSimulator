SYSC 3330 A3 - Group 9
*****************************

LINK TO REPOSITORY
****************************
https://github.com/loisaleghe/elevatorSimulator

DEMO VIDEO LINK
*****************************
https://mediaspace.carleton.ca/media/Kaltura+Capture+recording+-+April+6th+2020%2C+3A03A50+pm/1_dt0b2hwl

Group Members
***********************
* Abdul-Rahmaan Rufai
* Ediomoabasi Emah
* Hilaire Djani
* Lois Aleghe 
* Mohammed-Yasir Suara

Classes and Enums
*************************

* Direction
  Specifies the directions that can be called from the floor (the UP and DOWN buttons).

* ElevatorSubsystem
  Receives data packets from the scheduler and sends floor requests to the scheduler.

* FloorData
  The class houses the constructor that puts the data from each floor into the desired format.

* FloorSubsystem
  Reads the data from the file and sends it via a DatagramSocket to the scheduler.

* Scheduler  
   Acts as a server. It ensures the communication between the clients, which are the Elevator Subsystem and the Floor Subsystem.
   This is done by sending and receiving DatagramPackets in form of bytes containing the floor data and elevator data.
   
* Elevator 
   Represents the elevators in the system
   
* Floor
   Represents the floor in the system
   
* FloorQueue
   Stores and manages an array list of floors that an elevator will visit
   
* ElevatorData
  Contains the information of the elevator that will be passed to the Scheduler
  
* SchedularElevatorData
  Represents the data sent between schedular and elevator subsystem

* SchedularFloorData
  Represents the data sent between floor subsystem and schedular

* CommunicationMessage
 Encodes messages exchange between the different components of the project


	

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
* Run the schedular class first as a java application
* Then run the elevator subsystem as a java application
* Then run the floor subsystem as a java application

Flow
*************************

1. Elevator arrives on a floor
2. Elevator notifies schedular of arrival through elevator subsystem
4. Scheduler fetches floor requests that this elevator can best handle and sends them ElevatorSubsystem
5. If there are floor requests, ElevatorSubsystem instructs elevator to move to specified floors.
6. At the same time, FloorSubsystem continously sends floor requests to the scheduler.
7. Program keeps running until there are no more floor requests and elevator has visited all of it's floors

Test Set-Up Instruction
****************************

* To run the test cases, Open Eclipse and run the AllTests class found in the "elevatorSimulatorTests" package.
* All tests should pass.


