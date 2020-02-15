package elevatorSimulator;

import java.util.ArrayList;

public class ElevatorSubsystem extends Thread {

	private Scheduler scheduler; //Scheduler object to interact with the elevator subsystem to get data

	private int elevatorSize; //elevatorSize contains the number of elevators

	private Elevator availableElevator = null; // This represents an elevator that is currently available on a given floor
	private boolean elevatorPresent = false; // Specifies whether the available elevator can be changed

	private ArrayList <Elevator> elevators; //elevators would contain an arraylist of elevators

	/**
	 * Default constructor for elevator subsystem class
	 * @param scheduler, argument passed through default constructor for the elevator subsystem
	 */
	public ElevatorSubsystem(Scheduler scheduler, int elevatorSize) {
		super("Elevator Subsystem");
		this.scheduler = scheduler;
		this.elevatorSize = elevatorSize;
		this.elevators = new ArrayList<>();
		//add elevators to the arraylist based on the size
		for(int i = 0; i < elevatorSize; i++) {
			this.elevators.add(new Elevator(this));
		}
	}

	/**
	 * Starts the elevator subsystem thread alongside it's elevators
	 */
	@Override
	public void start() {
		super.start();
		for(Elevator e: this.elevators) {
			e.start();
		}
	}

	/**
	 * Fetches signal from elevators when an elevator moves one floor
	 * @param elevator, an Elevator, represents the elevator that sent the signal
	 */
	public synchronized void receiveElevatorSignal(Elevator elevator) {
		// Wait until there is no available elevator
		while(this.elevatorPresent) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				System.err.println("== An error occured: elevator subsystem could not receive signal from elevator");
				System.err.println(e.getMessage());
			}
		}

		// Make the elevator be the available elevator and notify all waiting threads
		System.out.println("== Elevator subsystem: Elevator available on floor " + elevator.getCurrentFloor().getNumber());
		this.availableElevator = elevator;
		this.elevatorPresent = true;
	}

	@Override
	public void run() {
		while(true) {
			try {
				// Wait until there is an elevator available
				while(!this.elevatorPresent) {
					Thread.sleep(1000);
				}

				Floor currentFloor;
				Direction availableElevatorDirection = this.availableElevator.getCurrentDirection();
				
				if(this.availableElevator.getCurrentDirection().equals(Direction.IDLE)) {
					currentFloor = null;
				} else {
					currentFloor = this.availableElevator.getCurrentFloor();
				}

				// Get floor data from scheduler and instruct elevator to move
				System.out.println("== Elevator subsystem: Fetching floor requests from schedular");
				ArrayList<Floor> requestedFloors = this.scheduler.getData(currentFloor);

				// If there are requested floors, signal elevator to stop
				if(!requestedFloors.isEmpty()) {
					System.out.println("== Elevator subsystem: Floor requests received " + requestedFloors);
					if(availableElevatorDirection.equals(Direction.IDLE)) {
						this.availableElevator.addFloors(requestedFloors);
					} else {
						this.availableElevator.stopElevator();  // Stop elevator if moving
						if(!this.availableElevator.isDoorOpen()) // Open elevator door if not open
							this.availableElevator.openElevatorDoor();
						
						this.availableElevator.pressButton(requestedFloors);
						this.availableElevator.closeElevatorDoor();
					}
				} else {
					System.out.println("== Elevator subsystem: No floor requests on this floor");
					if(this.availableElevator.isDoorOpen() && currentFloor != null) // Open elevator door if not open
						this.availableElevator.closeElevatorDoor();;
				}
				
				this.availableElevator.move();

				this.elevatorPresent = false;

				Thread.sleep(1000);
			} catch(InterruptedException e) {
				System.err.println("== Elevator subsystem: An error occured");
				System.err.println(e.getMessage());
			}
		}
	}

}
