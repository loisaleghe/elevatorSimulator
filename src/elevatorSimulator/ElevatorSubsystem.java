package elevatorSimulator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ElevatorSubsystem extends Thread {

	//	private Scheduler scheduler; //Scheduler object to interact with the elevator subsystem to get data

	private Elevator elevator; // The elevator that this Elevator Subsystem controls

	private DatagramSocket sendSocket; // socket that sends floor requests to the Scheduler

	private DatagramSocket receiveSocket; // socket to receive packets from the Scheduler

	private ArrayList<Integer> floorReq; 

	private byte[] array;

	private boolean stopSystem = false; // Specifies whether to stop the system from running

	/**
	 * Default constructor for elevator subsystem class
	 * @param elevatorNumber an int, represents the number of the elevator controlled by this elevator subsystem
	 */
	public ElevatorSubsystem(int elevatorNumber) {
		super("Elevator Subsystem");
		this.elevator = new Elevator(this, elevatorNumber);

		try {
			// Create the Datagram sockets used to send and receive packets
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(30);
		}catch (SocketException e) {
			System.err.println("== Elevator Subsystem: Could not create sockets");
			e.printStackTrace();
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

	/**
	 * Gets the elevator that this elevator subsystem controls
	 * @return an Elevator, representing the elevator that this elevator subsystem controls
	 */
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
		try {

			Direction currentDirection = this.elevator.getCurrentDirection();
			
			// Generate packet to send to scheduler as request
			byte [] elevatorRequestData = ElevatorData.seriliaze(new ElevatorData(this.elevator));
			DatagramPacket elevatorRequestPacket = new DatagramPacket(elevatorRequestData, elevatorRequestData.length, InetAddress.getLocalHost(), 20);

			// Notify the schedular of elevator's arrival on a floor
			System.out.println("== Elevator subsystem: Notifyng schedular of elevator arrival");

			this.sendSocket.send(elevatorRequestPacket);

			byte [] elevatorReplyData = new byte[10000];
			DatagramPacket elevatorReplyPacket = new DatagramPacket(elevatorReplyData, elevatorReplyData.length);

			// Receive response from scheduler
			System.out.println("== Elevator subsystem: Receiving floor requests from schedular");
			this.receiveSocket.receive(elevatorReplyPacket);

			// Parse the reply received to extract floor request
			SchedularElevatorData schedularResponse = SchedularElevatorData.deserialize(elevatorReplyPacket.getData());
			ArrayList<Floor> floorRequests = schedularResponse.getFloorRequests();
			if(floorRequests.isEmpty()) {
				System.out.println("== Elevator subsystem: No floor requests on this floor");
				if(this.elevator.isDoorOpen() && currentDirection.equals(Direction.IDLE)) // Close elevator door if open
					this.elevator.closeElevatorDoor();
			} else {
				// If there is a request on this floor, signal elevator to stop or move in a specific direction
				System.out.println("== Elevator subsystem: Floor requests received " + floorRequests);

				// If elevator is idle, notify the elevator to move in direction of request
				if(currentDirection.equals(Direction.IDLE)) {
					if(this.elevator.isDoorOpen()) // Close elevator door if open
						this.elevator.closeElevatorDoor();

					this.elevator.setCurrentDirection(floorRequests.get(0).getNumber() > this.elevator.getCurrentFloor().getNumber() ? Direction.UP : Direction.DOWN);
					this.elevator.addFloors(floorRequests);
					System.out.println("== Elevator subsystem: Instructing idle elevator to move " + this.elevator.getCurrentDirection());
				} else {
					this.elevator.stopElevator();  // Stop elevator if moving
					if(!this.elevator.isDoorOpen()) // Open elevator door if not open
						this.elevator.openElevatorDoor();

					this.elevator.pressButton(floorRequests);
					this.elevator.closeElevatorDoor();
				}
			}

			this.elevator.move();

		} catch (IOException e) {
			System.err.println("== Elevator SubSystem: An error occured while sending request to scheduler");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.err.println("== Elevator SubSystem: An error occured while sending request to scheduler");
			e.printStackTrace();
		}

		/*
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
		//		} else {
		//			this.stopSystem = true;
		//		}

		this.elevator.move();
		 */
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

			//			System.out.println("Elevator Subsystem: sending a packet that contains:\n" + floorReq);
			//
			//			try {
			//				sendPacket = new DatagramPacket(array,array.length,InetAddress.getLocalHost(),20);
			//			}catch(UnknownHostException e) {
			//				e.printStackTrace();
			//				System.exit(1);
			//			}
			//			System.out.println("Elevator Subsytem: Sending the packet to the Scheduler:");
			//			System.out.println("To host: " + sendPacket.getAddress());
			//			System.out.println("Destination host port: " + sendPacket.getPort());
			//			int len = sendPacket.getLength();
			//			System.out.println("Length: " + len);
			//			System.out.print("Containing String: ");
			//			System.out.println(new String(sendPacket.getData(),0,len));
			//
			//			try {
			//				sendSocket.send(sendPacket);
			//			} catch (IOException e) {
			//				e.printStackTrace();
			//				System.exit(1);
			//			}
			//
			//			//creates a byte array with a length of 100 bytes
			//			byte data[] = new byte[100];
			//			receivePacket = new DatagramPacket (data,data.length);
			//
			//			try {  
			//				receiveSocket.receive(receivePacket);
			//			} catch(IOException e) {
			//				e.printStackTrace();
			//				System.exit(1);
			//			}
			//
			//			System.out.println("Elevator Subsystem: Packet received:");
			//			System.out.println("From host: " + receivePacket.getAddress());
			//			System.out.println("Host port: " + receivePacket.getPort());
			//			int len1 = receivePacket.getLength();
			//			System.out.println("Length: " + len1);
			//			System.out.print("Containing: ");
			//
			//			String received = new String(data,0,len1);   
			//			System.out.println(received);
			//
			//			// We're finished, so close the socket.
			//			sendSocket.close();
			//			receiveSocket.close();
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
	
	public static void main(String [] args) {
		ElevatorSubsystem es = new ElevatorSubsystem(1);
		es.start();
	}

}