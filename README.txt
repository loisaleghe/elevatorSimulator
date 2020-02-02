SYSC 3330 A3 - Group 9
*****************************

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
  It receives data from the scheduler and sends data to the scheduler.

* FloorData
  The class houses the constructor that puts the data from each floor into the desired format.

* FloorSubsystem
  Reads the data from the file and sends it to the scheduler.

* Scheduler  
   It acts as a server. It ensures the communication between the clients, which are the Elevator Subsystem and the Floor Subsystem.

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

Set-Up Instructions
*****************************

* Extract project zip folder
* Import the file to eclipse
* Ensure that the package is elevatorSimulator
* Run the Scheduler.java class

Flow
*************************

* FloorSubsystem reads the data from an input file (floorRequests.txt) and sends data to the Scheduler.
* The ElevatorSubsystem fetches the data from the Scheduler and sends it back to the Scheduler.
* The FloorSubsystem then refetches this data from the Scheduler.
* Program runs until all the data in the input file has been read.

Test Set-Up Instruction
****************************

* To run the test cases, Open Eclipse and run the AllTests.java class.
* All tests should pass.
