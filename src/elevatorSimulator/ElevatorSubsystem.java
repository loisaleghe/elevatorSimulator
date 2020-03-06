package elevatorSimulator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class ElevatorSubsystem extends Thread {

	private Scheduler scheduler; //Scheduler object to interact with the elevator subsystem to get data

	private Elevator elevator; // The elevator that this Elevator Subsystem controls
	
	private DatagramPacket sendPacket;

	private DatagramPacket receivePacket;
	
	private DatagramSocket sendSocket; // socket that sends floor requests to the Scheduler
	
	private DatagramSocket receiveSocket; // socket to receive packets from the Scheduler
	
	private ArrayList<Integer> floorReq; 
	
	private byte[] array;

	

	//	private int elevatorSize; //elevatorSize contains the number of elevators

	//	private Elevator availableElevator = null; // This represents an elevator that is currently available on a given floor
	//	private boolean elevatorPresent = false; // Specifies whether the available elevator can be changed
	private boolean stopSystem = false; // Specifies whether to stop the system from running

	//	private ArrayList <Elevator> elevators; //elevators would contain an arraylist of elevators

	/**
	 * Default constructor for elevator subsystem class
	 * @param scheduler, argument passed through default constructor for the elevator subsystem
	 */
	public ElevatorSubsystem(Scheduler scheduler /*, int elevatorSize*/) {
		super("Elevator Subsystem");
		this.scheduler = scheduler;
		this.elevator = new Elevator(this);
		//		this.elevatorSize = elevatorSize;
		//		this.elevators = new ArrayList<>();
		//add elevators to the arraylist based on the size
		//		for(int i = 0; i < elevatorSize; i++) {
		//			this.elevators.add(new Elevator(this));
		//		}
		
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(30);
		}catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Stops the system from running
	 * @param stopSystem
	 */
	public void stopSystem(boolean stopSystem) {
		this.stopSystem = stopSystem;
	}

	/**
	 * Specifies the current running state of the system, i.e. whether it is stopped or not
	 * @return a boolean, true if the system is currently stopped, false otherwise
	 */
	public boolean systemStopped() {
		return this.stopSystem;
	}
	
	public Elevator getElevator() {
		return this.elevator;
	}

		/**
		 * Starts the elevator subsystem thread alongside it's elevator
		 */
		@Override
		public void start() {
			super.start();
			this.elevator.start();
			//		for(Elevator e: this.elevators) {
			//			e.start();
			//		}
		}

	/**
	 * Fetches signal from elevators when an elevator moves one floor
	 * @param elevator, an Elevator, represents the elevator that sent the signal
	 */
	//	public synchronized void receiveElevatorSignal(Elevator elevator) {
	//		// Wait until there is no available elevator
	//		while(this.elevatorPresent) {
	//			try {
	//				Thread.sleep(1000);
	//			} catch (InterruptedException e) {
	//				System.err.println("== An error occured: elevator subsystem could not receive signal from elevator");
	//				System.err.println(e.getMessage());
	//			}
	//		}
	//
	//		// Make the elevator be the available elevator and notify all waiting threads
	//		System.out.println("== Elevator subsystem: Elevator available on floor " + elevator.getCurrentFloor().getNumber());
	//		this.availableElevator = elevator;
	//		this.elevatorPresent = true;
	//	}

	/**
	 * Notifies the schedular of the elevator's arrival on a floor
	 */
	public void notifySchedular() {
		// If there is more data being read or there are more floor requests
		if(this.scheduler.getMoreData() || this.scheduler.moreFloorRequests()) {

			Floor currentFloor;
			Direction elevatorDirection = this.elevator.getCurrentDirection();

			if(elevatorDirection.equals(Direction.IDLE)) {
				currentFloor = null;
			} else {
				currentFloor = this.elevator.getCurrentFloor();
			}

			// Get floor data from scheduler and instruct elevator to move
			System.out.println("== Elevator subsystem: Fetching floor requests from schedular");
			ArrayList<Floor> requestedFloors = this.scheduler.getData(currentFloor);

			if(!requestedFloors.isEmpty()) { // If there are requested floors, signal elevator to stop or move in a specific direction
				System.out.println("== Elevator subsystem: Floor requests received " + requestedFloors);

				// If elevator is idle, notify the elevator to move in direction of request
				if(elevatorDirection.equals(Direction.IDLE)) {
					if(this.elevator.isDoorOpen()) // Close elevator door if open
						this.elevator.closeElevatorDoor();

					this.elevator.setCurrentDirection(requestedFloors.get(0).getNumber() > this.elevator.getCurrentFloor().getNumber() ? Direction.UP : Direction.DOWN);
					this.elevator.addFloors(requestedFloors);
					System.out.println("== Elevator subsystem: Instructing idle elevator to move " + this.elevator.getCurrentDirection());
				} else {
					this.elevator.stopElevator();  // Stop elevator if moving
					if(!this.elevator.isDoorOpen()) // Open elevator door if not open
						this.elevator.openElevatorDoor();

					this.elevator.pressButton(requestedFloors);
					this.elevator.closeElevatorDoor();
				}
			} else {
				System.out.println("== Elevator subsystem: No floor requests on this floor");
				if(this.elevator.isDoorOpen() && currentFloor != null) // Open elevator door if not open
					this.elevator.closeElevatorDoor();;
			}
		} else {
			this.stopSystem = true;
		}

		this.elevator.move();
	}
	
	/*
	 * @param fd: creates a FloorData object
	 * @return returns an arraylist of destination floors that have beemn requested
	 */
	public ArrayList<Integer> floorRequest(FloorData fd) {
        floorReq.add(fd.getCarButton());
		return floorReq;
	}
	/*
	 * @return returns an array of byte
	 * converts the Arraylist of Floor request integer to an array of bytes
	 */
	public byte[] toArray () {
		for (int i = 0; i < floorReq.size(); i++) {
			array[i] = (byte) (int) floorReq.get(i);
		}
		return array;
	}

	@Override
	public void run() {
		//		Repeat until system is stopped. System is stop when there is no more data to read and elevator visited all of it's floors
		while(!this.stopSystem) {
			
			System.out.println("Elevator Subsystem: sending a packet that contains:\n" + floorReq);
			
			try {
				sendPacket = new DatagramPacket(array,array.length,InetAddress.getLocalHost(),20);
			}catch(UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}
			System.out.println("Elevator Subsytem: Sending the packet to the Scheduler:");
		    System.out.println("To host: " + sendPacket.getAddress());
		    System.out.println("Destination host port: " + sendPacket.getPort());
		    int len = sendPacket.getLength();
		    System.out.println("Length: " + len);
		    System.out.print("Containing String: ");
		    System.out.println(new String(sendPacket.getData(),0,len));
		    
		    try {
		         sendSocket.send(sendPacket);
		    } catch (IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		    }
		    
		    //creates a byte array with a length of 100 bytes
		    byte data[] = new byte[100];
		    receivePacket = new DatagramPacket (data,data.length);
		    
		    try {  
		         receiveSocket.receive(receivePacket);
		    } catch(IOException e) {
		         e.printStackTrace();
		         System.exit(1);
		    }
		    
		    System.out.println("Elevator Subsystem: Packet received:");
		    System.out.println("From host: " + receivePacket.getAddress());
		    System.out.println("Host port: " + receivePacket.getPort());
		    int len1 = receivePacket.getLength();
		    System.out.println("Length: " + len1);
		    System.out.print("Containing: ");

		    String received = new String(data,0,len1);   
		    System.out.println(received);

		      // We're finished, so close the socket.
		    sendSocket.close();
		    receiveSocket.close();
			//			try {				
			//				// Wait until there is an elevator available
			//				while(!this.elevatorPresent) {
			//					Thread.sleep(1000);
			//				} 
			//
			//				// If there is more data being read or there are more floor requests
			//				if(this.scheduler.getMoreData() || this.scheduler.moreFloorRequests()) {
			//
			//					Floor currentFloor;
			//					Direction availableElevatorDirection = this.availableElevator.getCurrentDirection();
			//
			//					if(this.availableElevator.getCurrentDirection().equals(Direction.IDLE)) {
			//						currentFloor = null;
			//					} else {
			//						currentFloor = this.availableElevator.getCurrentFloor();
			//					}
			//
			//					// Get floor data from scheduler and instruct elevator to move
			//					System.out.println("== Elevator subsystem: Fetching floor requests from schedular");
			//					ArrayList<Floor> requestedFloors = this.scheduler.getData(currentFloor);
			//
			//					if(!requestedFloors.isEmpty()) { // If there are requested floors, signal elevator to stop or move in a specific direction
			//						System.out.println("== Elevator subsystem: Floor requests received " + requestedFloors);
			//
			//						// If elevator is idle, notify the elevator to move in direction of request
			//						if(availableElevatorDirection.equals(Direction.IDLE)) {
			//							if(this.availableElevator.isDoorOpen()) // Close elevator door if open
			//								this.availableElevator.closeElevatorDoor();
			//
			//							this.availableElevator.setCurrentDirection(requestedFloors.get(0).getNumber() > this.availableElevator.getCurrentFloor().getNumber() ? Direction.UP : Direction.DOWN);
			//							this.availableElevator.addFloors(requestedFloors);
			//							System.out.println("== Elevator subsystem: Instructing idle elevator to move " + this.availableElevator.getCurrentDirection());
			//						} else {
			//							this.availableElevator.stopElevator();  // Stop elevator if moving
			//							if(!this.availableElevator.isDoorOpen()) // Open elevator door if not open
			//								this.availableElevator.openElevatorDoor();
			//
			//							this.availableElevator.pressButton(requestedFloors);
			//							this.availableElevator.closeElevatorDoor();
			//						}
			//					} else {
			//						System.out.println("== Elevator subsystem: No floor requests on this floor");
			//						if(this.availableElevator.isDoorOpen() && currentFloor != null) // Open elevator door if not open
			//							this.availableElevator.closeElevatorDoor();;
			//					}
			//				} else {
			//					this.stopSystem = true;
			//				}
			//
			//				this.availableElevator.move();
			//
			//				this.elevatorPresent = false;
			//
			//				Thread.sleep(1000);
			//			} catch(InterruptedException e) {
			//				System.err.println("== Elevator subsystem: An error occured");
			//				System.err.println(e.getMessage());
			//			}
		}
		System.out.println("== Elevator Subsystem: Finished!");
	}

}
