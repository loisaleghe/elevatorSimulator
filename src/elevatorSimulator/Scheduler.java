package elevatorSimulator;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * The scheduler class represents the server in the system
 */

public class Scheduler extends Thread {

	//creates an object of type floorData
	//private FloorData floorData;

	private List<FloorData> floorData ;	//	Arraylist containing floor data from requests from the same floor. Iteration 2
	private List<ElevatorData> elevators;	// List of elevators
	private DatagramSocket sendSocket;
	private DatagramSocket fsReceive;
	private DatagramSocket esReceive;
	//the scheduler stops when stopSystem is true
	private boolean stopSystem;


	/**
	 * The constructor to define the instance variables 
	 * of the scheduler
	 */
	public Scheduler() {
		super("Schedular");
		this.stopSystem = false;
		this.floorData = Collections.synchronizedList(new ArrayList<>());	
		this.elevators = Collections.synchronizedList(new ArrayList<>());

		try {
			sendSocket = new DatagramSocket();
			esReceive = new DatagramSocket(20);
			fsReceive = new DatagramSocket(10);
		}
		catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public boolean systemStopped() {
		return this.stopSystem;
	}

	public void stopSystem() {
		this.stopSystem = true;
	}

	public boolean moreFloorRequests() {
		return !this.floorData.isEmpty();
	}

	/**
	 * Adds the specified elevator to the list of elevators handled by this scheduler
	 * @param e an Elevator, represents the elevator to be added to the list of elevators handled by this scheduler
	 */
	public void addElevator(ElevatorData e) {
		System.out.println("== Schedular: Adding elevator " + e.getNumber() + " to list of elevators");
		this.elevators.add(e);
	}

	/**
	 * Checks if a given elevator is present in this schedular's list of elevators
	 * Updates the elevator's position if present
	 * @param elevator an ElevatorData, represents the elevator being checked
	 * @return a boolean, true if the given elevator is present in the list of elevators, false otherwise
	 */
	public boolean isElevatorPresent(ElevatorData elevator) {
		for(ElevatorData e: this.elevators) {
			if(e.getNumber() == elevator.getNumber()) {
				e.setFloor(elevator.getFloor());
				return true;
			}
		}

		return false;
	}

	/**
	 * This method is called by the Floor and Elevator Subsystems
	 * to continuously get data from the scheduler
	 * 
	 * Modified to return an arraylist of floordata. Iteration 2
	 */

	public ArrayList<FloorData> getData(Floor f) {
		ArrayList<FloorData> temp = new ArrayList<>();
		ArrayList<FloorData> newFd = new ArrayList<>();

		int fn;

		// get all requests that are same as the given floor
		if(!this.floorData.isEmpty()) {
			fn = f.getNumber();
			for(FloorData fd: this.floorData) {
				if(fd.getFloor() == fn) {
					temp.add(fd);
				} else {
					newFd.add(fd);
				}
			}
			this.floorData = newFd;
		}		

		return temp;
	}

	/**
	 * Checks which elevator is best suited for a given request and sends floor requests to that elevator
	 */
	public void checkBestAndSendData() {
		// Creak out if there are no available elevators or no floor requests to process
		if(this.elevators.isEmpty() || this.floorData.isEmpty()) {
			return;
		}

		ArrayList<FloorData> floorRequests = this.getData(new Floor(this.floorData.get(0).getFloor()));
		// Determine which elevator is best suited for these requests
		ElevatorData bestElevator = this.elevators.get(0);
		int wait1 = Math.abs(bestElevator.getFloor().getNumber() - floorRequests.get(0).getFloor());
		int wait2;

		for(ElevatorData el: this.elevators) {
			wait2 = Math.abs(el.getFloor().getNumber() - floorRequests.get(0).getFloor());
			if(wait2 < wait1) {
				bestElevator = el;
				wait1 = wait2;
			}
		}

		try {
			// Send requests to best elevator
			byte [] elevatorResponseData = SchedularElevatorData.seriliaze(new SchedularElevatorData(CommunicationMessage.ELEVATOR_CONTINUE, floorRequests));
			DatagramPacket elevatorResponsePacket = new DatagramPacket(elevatorResponseData, elevatorResponseData.length, InetAddress.getByName("172.17.48.203"), 3000 + bestElevator.getNumber());

			// Send floor requests to elevator subsystem
			System.out.println("== Schedular: Sending floor requests to elevator subsystem...");
			sendSocket.send(elevatorResponsePacket);
			System.out.println("== Schedular: Floor requests sent to elevator subsystem");
		} catch (IOException e) {
			System.err.println("== Schedular: Could not send floor requests to elevator subsystem");
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Starts the scheduler and all of its sub threads
	 */
	@Override
	public void start() {
		super.start();
		new SchedularToESThread().start();
		new SchedularToFSThread().start();
		new FloorRequestProcessingThread().start();
	}

	@Override
	public void run() {
		while(true) {

		}
	}

	/**
	 * This represents the thread used to process floor requests and send them to the best suited elevator
	 * @author djani
	 */
	private class FloorRequestProcessingThread extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					checkBestAndSendData();
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					System.err.println("== Schedular: An error occured while processing floor requests");
					System.err.println(e.getMessage());
				}
			}
		}
	}

	/**
	 * This represents the thread used to communicate between scheduler end elevator subsystems
	 * @author djani
	 */
	private class SchedularToESThread extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					byte [] elevatorRequestData = new byte[5000];
					DatagramPacket elevatorRequestPacket = new DatagramPacket(elevatorRequestData, elevatorRequestData.length);

					// Receive requests from elevator subsystem
					System.out.println("== Schedular: Waiting for request from elevator subsystem...");
					esReceive.receive(elevatorRequestPacket);
					System.out.println("== Schedular: Requests received from elevator subsystem");

					ElevatorData ed = ElevatorData.deserialize(elevatorRequestPacket.getData());

					// Check if this elevator is present in the list of elevators. If yes, update position, if not add it
					if(!isElevatorPresent(ed)) {
						addElevator(ed);
					}
				} catch (IOException e) {
					System.err.println("== Scheduler: Could not send/receive data to/from elevator subsystem");
					System.err.println(e.getMessage());
				} catch (ClassNotFoundException e) {
					System.err.println("== Scheduler: Could not send/receive data to/from elevator subsystem");
					System.err.println(e.getMessage());
				}
			}
		}
	}

	/**
	 * This represents the thread used to communicate between scheduler end floor subsystems
	 * @author djani
	 *
	 */
	private class SchedularToFSThread extends Thread {
		@Override
		public void run() {
			while(true) {
				try {
					byte [] floorRequestData = new byte[5000];
					DatagramPacket floorRequestPacket = new DatagramPacket(floorRequestData, floorRequestData.length);

					// Receive requests from floor subsystem
					System.out.println("== Schedular: Waiting for request from floor subsystem...");
					fsReceive.receive(floorRequestPacket);
					System.out.println("== Schedular: Floor requests received from floor subsystem");

					SchedularFloorData sfd = SchedularFloorData.deserialize(floorRequestPacket.getData());
					if(sfd.getMessage().equals(CommunicationMessage.NO_MORE_FLOOR_REQUESTS)) {
						System.out.println("== Schedular: No more floor requests from floor subsystem. Stopping communication with floor subsystem...");
						stopSystem();
					} else {
						FloorData fd = sfd.getFloorData();
						floorData.add(fd);	
					}
				} catch (IOException e) {
					System.err.println("== Schedular: Could not send/receive data to/from floor subsystem");
					System.err.println(e.getMessage());
				} catch (ClassNotFoundException e) {
					System.err.println("== Schedular: Could not send/receive data to/from floor subsystem");
					System.err.println(e.getMessage());
				}
			}

		}
	}

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		scheduler.start();

	}
}
