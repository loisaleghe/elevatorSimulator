package elevatorSimulator;


import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author Ediomoabasi Emah
 */
public class Elevator implements Runnable {	

	//creates a queue to represent the elevator moving up 
	private FloorQueue upQueue; 

	//creates a queue to represent the elevator moving down
	private FloorQueue downQueue;

	//creates a floor object to specify the floor the elevator is on
	private Floor currFloor;

	//specifies the direction the elevator is moving
	private Direction currDirection;
	
	private ElevatorSubsystem elevatorSubsytem; // Represents the elevator subsystem controlling this elevator

	public Elevator() {
		this.upQueue = new FloorQueue();
		this.downQueue = new FloorQueue();
		this.currFloor = new Floor(1);
		this.currDirection = Direction.IDLE;
	}
	
	/*
	 * The constructor to define the elevator
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
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * Sleeps for one second to close elevator door
	 */
	public void closeElevatorDoor() {
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * add floors to either the elevator going up or the elevator
	 * going down
	 */
	public void addFloor(Floor f) {

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

	/*
	 * 
	 */
	public void pressButton(ArrayList<FloorData> floorData) {
		// Add floors to elevator queue
		for(FloorData fd: floorData) {
			this.addFloor(new Floor(fd.getCarButton()));
		}
	}

	/*
	 * dictates the movement of the elevator
	 */
	public void move() {

		//scenarios for the elevator moving up
		if (currDirection.equals(Direction.UP)) {

			//increments the floor if the current floor is less than the floor at index 0 
			if (currFloor.getNumber() < upQueue.peek().getNumber()) {
				currFloor.setNumber(currFloor.getNumber() + 1);
			}

			//removes the floor from the queue if the current floor is the same
			//as the floor at that specific index
			else if (currFloor.getNumber() == upQueue.peek().getNumber()) {
				upQueue.poll();

				if (!upQueue.isEmpty()) {
					//increments the current floor is the queue isn't empty
					currFloor.setNumber(currFloor.getNumber() + 1);				
				}
			}

		}

		//scenarios for the elevator moving down
		else if (currDirection.equals(Direction.DOWN)) {

			//decrements the floor if the current floor is greater than the floor at index 0 
			if (currFloor.getNumber() > downQueue.peek().getNumber()) {
				currFloor.setNumber(currFloor.getNumber() - 1);
			}

			//removes the floor from the queue if the current floor is the same
			//as the floor at that specific index
			else if (currFloor.getNumber() == downQueue.peek().getNumber()) {
				downQueue.poll();

				if(!downQueue.isEmpty()) {
					//decrements the current floor is the queue isn't empty
					currFloor.setNumber(currFloor.getNumber() - 1);
				}
			}
		}

		//	Reset elevator direction
		this.adjustElevatorDirection();
	}

	@Override
	public void run() {
		while(true) {
			this.elevatorSubsytem.receiveElevatorSignal(this);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

}
