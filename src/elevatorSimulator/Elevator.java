package elevatorSimulator;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Ediomoabasi Emah
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

	private boolean isDoorOpen;

	private ElevatorSubsystem elevatorSubsytem; // Represents the elevator subsystem controlling this elevator

	public Elevator() {
		super("Elevator thread");
		this.upQueue = new FloorQueue();
		this.downQueue = new FloorQueue();
		this.currFloor = new Floor(1);
		this.currDirection = Direction.IDLE;
		this.isDoorOpen = false;
	}

	/*
	 * The creates a new elevator and assigns the specified subsystem to control it
	 */
	public Elevator (ElevatorSubsystem elevatorSubsytem) {
		this();
		this.elevatorSubsytem = elevatorSubsytem;
	}

	public FloorQueue getUpQueue() {
		return this.upQueue;
	}

	public FloorQueue getDownQueue() {
		return this.downQueue;
	}

	public FloorQueue getCurrentQueue() {
		if(this.currDirection.equals(Direction.UP))
			return this.upQueue;
		else if(this.currDirection.equals(Direction.UP))
			return this.downQueue;
		else return null;
	} 

	/*
	 * @return returns the floor the elevator is currently at
	 */
	public Floor getCurrentFloor() {
		return this.currFloor;
	}

	public void setCurrentFloor(Floor currFloor) {
		this.currFloor = currFloor;
	}

	/*
	 * @return returns the direction the elevator is currently moving
	 */
	public Direction getCurrentDirection() {
		return this.currDirection;
	}

	public void setCurrentDirection(Direction currDirection) {
		this.currDirection = currDirection;
	}
	
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

	/*
	 * Sleeps for one second to open elevator door
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

	public void stopElevator() {
		try {
			System.out.println("== Elevator: Elevator stopped on floor " +  + this.currFloor.getNumber());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			System.err.println("== Elevator: An error occured while opening elevator door");
			System.err.println(e.getMessage());
		}
	}

	/*
	 * Sleeps for one second to close elevator door
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

	/*
	 * add floors to either the elevator going up or the elevator
	 * going down
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
	
	public void addFloors(ArrayList<Floor> floors) {
		for(Floor fl: floors)
			this.addFloor(fl);
	}

	/*
	 * 
	 */
	public void pressButton(ArrayList<Floor> floors) {
		// Add floors to elevator queue
		for(Floor fl: floors) {
			System.out.println("== Elevator: Pressing button " + fl.getNumber());
			this.addFloor(new Floor(fl));
		}
	}

	/*
	 * dictates the movement of the elevator
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
		while(true) {
			//			Send signal to elevator subsystem about elevator movement
			System.out.println("== Elevator: Signaling Elevator subsystem");
			this.elevatorSubsytem.receiveElevatorSignal(this);

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("== Elevator: An error occured");
				System.err.println(e.getMessage());
			}
		}

	}

}
