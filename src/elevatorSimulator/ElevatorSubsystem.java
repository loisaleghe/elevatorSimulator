package elevatorSimulator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class ElevatorSubsystem extends Thread {

	private Elevator elevator; // The elevator that this Elevator Subsystem controls
	
	private int number;

	private DatagramSocket sendSocket; // socket that sends floor requests to the Scheduler

	private DatagramSocket receiveSocket; // socket to receive packets from the Scheduler

	private boolean stopSystem = false; // Specifies whether to stop the system from running

	/**
	 * Default constructor for elevator subsystem class
	 * @param elevatorNumber an int, represents the number of the elevator controlled by this elevator subsystem
	 */
	public ElevatorSubsystem(int elevatorNumber) {
		super("Elevator Subsystem");
		this.number = elevatorNumber;
		this.elevator = new Elevator(this, elevatorNumber);

		try {
			// Create the Datagram sockets used to send and receive packets
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(3000 + elevatorNumber);
		}catch (SocketException e) {
			System.err.println("== Elevator Subsystem " + this.number + ": Could not create sockets");
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
	 * Notifies the schedular of the elevator's arrival on a floor
	 */
	public void notifySchedular() {
		try {			
			// Generate packet to send to scheduler as request
			byte [] elevatorRequestData = ElevatorData.seriliaze(new ElevatorData(this.elevator));
			DatagramPacket elevatorRequestPacket = new DatagramPacket(elevatorRequestData, elevatorRequestData.length, InetAddress.getByName("172.17.163.254"), 20);

			// Notify the scheduler of elevator's arrival on a floor
			System.out.println("== Elevator Subsystem " + this.number + ": Notifyng schedular of elevator arrival");
			this.sendSocket.send(elevatorRequestPacket);		
		} catch (IOException e) {
			System.err.println("== Elevator Subsystem " + this.number + ": An error occured while sending request to scheduler");
			e.printStackTrace();
		} 
	}

	@Override
	public void run() {
		//		Repeat until system is stopped. System is stop when there is no more data to read and elevator visited all of it's floors
		while(true) {
			Direction currentDirection = this.elevator.getCurrentDirection();

			byte [] elevatorReplyData = new byte[10000];
			DatagramPacket elevatorReplyPacket = new DatagramPacket(elevatorReplyData, elevatorReplyData.length);

			try {
				// Receive floor requests from scheduler
				System.out.println("== Elevator Subsystem " + this.number + ": Waiting for floor requests from scheduler...");
				this.receiveSocket.receive(elevatorReplyPacket);
				System.out.println("== Elevator Subsystem " + this.number + ": Floor requests received from scheduler");

				// Parse the reply received to extract floor request
				SchedularElevatorData schedularResponse = SchedularElevatorData.deserialize(elevatorReplyPacket.getData());
				ArrayList<FloorData> floorRequests = schedularResponse.getFloorRequests();

				if(floorRequests.isEmpty()) {
					System.out.println("== Elevator Subsystem " + this.number + ": No floor requests on this floor");
					if(this.elevator.isDoorOpen() && currentDirection.equals(Direction.IDLE)) // Close elevator door if open
						this.elevator.closeElevatorDoor();
				} else {
					// If there is a request on this floor, signal elevator to stop or move in a specific direction
					System.out.println("== Elevator Subsystem " + this.number + " Floor requests received " + floorRequests);

					// If elevator is idle, notify the elevator to move in direction of request
					if(currentDirection.equals(Direction.IDLE)) {
						if(this.elevator.isDoorOpen()) // Close elevator door if open
							this.elevator.closeElevatorDoor();

						this.elevator.setCurrentDirection(floorRequests.get(0).getFloor() > this.elevator.getCurrentFloor().getNumber() ? Direction.UP : Direction.DOWN);
						System.out.println("== Elevator Subsystem " + this.number + ": Instructing idle elevator to move " + this.elevator.getCurrentDirection());
					} 
					
					for(FloorData fr: floorRequests) {
						this.elevator.addFloor(new Floor(fr.getFloor()));
						this.elevator.pressButton(new Floor(fr.getCarButton()));
					}
				}

			} catch (IOException e) {
				System.err.println("== Elevator Subsystem " + this.number + ": An error occured...");
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				System.err.println("== Elevator Subsystem " + this.number + ": An error occured...");
				e.printStackTrace();
			}
		}
	}

	public static void main(String [] args) {
		ElevatorSubsystem es1 = new ElevatorSubsystem(1);
		ElevatorSubsystem es2 = new ElevatorSubsystem(2);
		ElevatorSubsystem es3 = new ElevatorSubsystem(3);
		es1.start();
		es2.start();
		es3.start();
	}

}