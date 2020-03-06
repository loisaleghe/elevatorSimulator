package elevatorSimulator;


import java.util.ArrayList;


/**
 * @author Ediomoabasi Emah, Hilaire Djani
 */
public class Elevator extends Thread {	

	//creates a queue to represent the elevator moving up 
	private FloorQueue upQueue; 

	//creates a queue to represent the elevator moving down
	private FloorQueue downQueue;

	//creates a floor object to specify the floor the elevator is on
	private Floor currFloor;

	//specifies the direction the elevator is moving
	private Direction currDirection;

	//	Specifies the current state of the elevator doors, i.e true for open, false for closed
	private boolean isDoorOpen;

	private ElevatorSubsystem elevatorSubsytem; // Represents the elevator subsystem controlling this elevator

	/**
	 * Creates a new elevator thread
	 */
	public Elevator() {
		super("Elevator thread");
		this.upQueue = new FloorQueue();
		this.downQueue = new FloorQueue();
		this.currFloor = new Floor(1);
		this.currDirection = Direction.IDLE;
		this.isDoorOpen = false;
	}

	/**
	 * The creates a new elevator and assigns the specified subsystem to control it
	 * @param elevatorSubsytem, an ElevatorSubsytem, specifies the ElevatorSubsytem that will be controlling this elevator
	 */
	public Elevator (ElevatorSubsystem elevatorSubsytem) {
		this();
		this.elevatorSubsytem = elevatorSubsytem;
	}

	/**
	 * Fetches the queue of floors that this elevator has to visit while going upwards
	 * @return a FloorQueue representing the floors this elevator has to visit up
	 */
	public FloorQueue getUpQueue() {
		return this.upQueue;
	}

	/**
	 * Fetches the queue of floors that this elevator has to visit while going downwards
	 * @return a FloorQueue representing the floors this elevator has to visit down
	 */
	public FloorQueue getDownQueue() {
		return this.downQueue;
	}

	/**
	 * Fetches the queue that this elevator is currently visiting
	 * @return a FloorQueue, upQueue if the elevator is going up and downQueue if it is going down
	 * This method returns null if the elevator is currently idle
	 */
	public FloorQueue getCurrentQueue() {
		if(this.currDirection.equals(Direction.UP))
			return this.upQueue;
		else if(this.currDirection.equals(Direction.DOWN))
			return this.downQueue;
		else return null;
	} 

	/**
	 * Fetches the floor on which this elevator is currently
	 * @return a Floor, representing the floor on which this elevator currently is
	 */
	public Floor getCurrentFloor() {
		return this.currFloor;
	}

	/**
	 * Sets the current floor on which this elevator will be
	 * @param currFloor, a Floor, represents the floor on which this elevator will be
	 */
	public void setCurrentFloor(Floor currFloor) {
		this.currFloor = currFloor;
	}

	/**
	 * Fetches the current direction for this elevator
	 * @return a Direction enum, representing on the current direction of this elevator
	 */
	public Direction getCurrentDirection() {
		return this.currDirection;
	}

	/**
	 * Sets the current direction for this elevator
	 * @param currDirection, a DIrection enum, representing the new direction for this elevator
	 */
	public void setCurrentDirection(Direction currDirection) {
		this.currDirection = currDirection;
	}

	/**
	 * Specifies the current status of this elevator's door
	 * @return a boolean, true if this elevator's door is currently open, false otherwise
	 */
	public boolean isDoorOpen() {
		return this.isDoorOpen;
	}

	/**
	 * Modifies the elevator's direction based on the state of it's queues
	 */
	private void adjustElevatorDirection() {
		if(this.upQueue.isEmpty() && this.downQueue.isEmpty())
			this.currDirection = Direction.IDLE;
		else if(this.upQueue.isEmpty())
			this.currDirection = Direction.DOWN;
		else if(this.downQueue.isEmpty())
			this.currDirection = Direction.UP;
	}

	/**
	 * Opens the door for this elevator
	 * Simulates door opening by sleeping for a short period of time
	 */
	public void openElevatorDoor() {
		try {
			System.out.println("== Elevator: Opening door on floor " +  + this.currFloor.getNumber());
			this.isDoorOpen = true;
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("== Elevator: An error occured while opening elevator door");
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Instructs this elevator to stop moving
	 * Simulates elevator stopping by sleeping for a short period of time
	 */
	public void stopElevator() {
		try {
			System.out.println("== Elevator: Elevator stopped on floor " +  + this.currFloor.getNumber());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("== Elevator: An error occured while opening elevator door");
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Closes the door for this elevator
	 * Simulates door closing by sleeping for a short period of time
	 */
	public void closeElevatorDoor() {
		try {
			System.out.println("== Elevator: Closing door on floor " + this.currFloor.getNumber());
			this.isDoorOpen = false;
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("== Elevator: An error occured while closing elevator door");
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Adds specified floor to appropriate elevator queue, based on elevator's current direction
	 * @param f, a Floor, represents the floor to add to the elevator's queues
	 */
	public void addFloor(Floor f) {
		System.out.println("== Elevator: Adding " + f);

		//gets the destination of the floor that is pressed 
		int floorNumber = f.getNumber();
		int currentFloorNumber = currFloor.getNumber();

		//adds the floors to the queue and sorts them for the elevator moving down
		if (floorNumber < currentFloorNumber) {
			downQueue.offer(new Floor(floorNumber));

			//is false to sort floors in descending order
			downQueue.sort(false);
		}else if (floorNumber > currentFloorNumber) {
			upQueue.offer(new Floor(floorNumber));

			//is true to sort floors in ascending order
			upQueue.sort(true);
		}

		// Reset elevator direction
		this.adjustElevatorDirection();
	}

	/**
	 * Adds several floors to the elevator's queues
	 * @param floors, an Arraylist of floors, represents the floors to add to the elevator's queues
	 */
	public void addFloors(ArrayList<Floor> floors) {
		for(Floor fl: floors)
			this.addFloor(fl);
	}

	/**
	 * Simulates pressing a floor destination button in the elevator
	 * Adds the floors specified to the elevator's floor queues
	 * @param floors
	 */
	public void pressButton(ArrayList<Floor> floors) {
		// Add floors to elevator queue
		for(Floor fl: floors) {
			System.out.println("== Elevator: Pressing button " + fl.getNumber());
			this.addFloor(new Floor(fl));
		}
	}

	/**
	 * Movies elevator one floor, either up or down depending on it's current direction
	 */
	public void move() {
		//scenarios for the elevator moving up
		if (currDirection.equals(Direction.UP)) {
			//	Move elevator up one floor
			System.out.println("== Elevator: Floors to visit " + this.upQueue);			
			this.currFloor.setNumber(currFloor.getNumber() + 1);
			System.out.println("== Elevator: Elevator moved to " + this.currFloor);

			//removes the floor from the queue if the current floor is the same
			//as the floor at that specific index
			if (currFloor.getNumber() == (upQueue.peek()).getNumber()) {
				this.stopElevator();
				this.openElevatorDoor();
				upQueue.poll();
			}

		}

		//scenarios for the elevator moving down
		else if (currDirection.equals(Direction.DOWN)) {
			//	Move elevator down one floor
			System.out.println("== Elevator: Floors to visit " + this.downQueue);	
			this.currFloor.setNumber(currFloor.getNumber() - 1);
			System.out.println("== Elevator: Elevator moved to " + this.currFloor);

			//removes the floor from the queue if the current floor is the same
			//as the floor at that specific index
			if (currFloor.getNumber() == downQueue.peek().getNumber()) {
				this.stopElevator();
				this.openElevatorDoor();
				downQueue.poll();
			}
		}

		//	Reset elevator direction
		this.adjustElevatorDirection();
	}

	@Override
	public void run() {
		//		Continue running until all floors are visited and there are no more requests
		while(!this.upQueue.isEmpty() || !this.downQueue.isEmpty() || !this.elevatorSubsytem.systemStopped()) {
			//	Notify scheduler about elevator movement through elevator subsystem
			System.out.println("== Elevator: Signaling Schedular through Elevator subsystem");
			this.elevatorSubsytem.notifySchedular();
			//			this.elevatorSubsytem.receiveElevatorSignal(this);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("== Elevator: An error occured");
				System.err.println(e.getMessage());
			}
		}
		System.out.println("== Elevator: Finished!");

	}

}
