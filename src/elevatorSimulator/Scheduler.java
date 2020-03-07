package elevatorSimulator;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

/*
 * The scheduler class represents the server in the system
 */

public class Scheduler extends Thread {

	//creates an object of type floorData
	//private FloorData floorData;

	private ArrayList<FloorData> floorData ;	//	Arraylist containing floor data from requests from the same floor. Iteration 2
	private ArrayList<Elevator> elevators;	// List of elevators
	private boolean canSendData;
	private boolean canGetData;
	
	private DatagramSocket sendSocket;
	private DatagramSocket fsReceive;
	private DatagramSocket esReceive;
	private DatagramPacket sendPacket;
	private ArrayList<DatagramPacket> receivePacket;

	//the scheduler stops when moreData is false
	private boolean moreData;
	

	/*
	 * The constructor to define the instance variables 
	 * of the scheduler
	 */
	public Scheduler() {
		super("Schedular");
		//this.floorData = null;
		this.canGetData = false;
		this.canSendData = true;
		this.moreData = true;
		this.floorData = new ArrayList<>();	
		this.elevators = new ArrayList<>();
		
		try {
			sendSocket = new DatagramSocket(30);
			esReceive = new DatagramSocket(20);
			fsReceive = new DatagramSocket(10);
		}
		catch (SocketException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		
	}

	public boolean getMoreData() {
		return this.moreData;
	}

	public void setMoreData(boolean moreData) {
		this.moreData = moreData;
	}
	
	public boolean moreFloorRequests() {
		return !this.floorData.isEmpty();
	}
	
	/**
	 * Adds the specified elevator to the list of elevators handled by this scheduler
	 * @param e an Elevator, represents the elevator to be added to the list of elevators handled by this scheduler
	 */
	public void addElevator(Elevator e) {
		this.elevators.add(e);
	}

		

	/*
	 * This method is called by the Floor and Elevator Subsystems
	 * to continuously send data to the scheduler
	 
	public synchronized void sendData(FloorData fl) {
		//		Wait if flood data is being processed
		while(!this.canSendData) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			bytesToFloorData
		}
		catch(UnknownHostException e){
			e.printStackTrace();
	         System.exit(1);
		}
		
		System.out.println("== Schedular: Received request on floor " + fl.getFloor());
		//this.floorData = new FloorData(fl);
		this.floorData.add(fl);				//Populating floor data arraylist with floordata then sending it to the elevator subsystem. Iteration 2
		this.canGetData = true;
		this.canSendData = false;

		notifyAll();
	}
	*/
	
	

	/*
	 * This method is called by the Floor and Elevator Subsystems
	 * to continuously get data from the scheduler
	 * 
	 * Modified to return an arraylist of floordata. Iteration 2
	 
	public synchronized ArrayList<Floor> getData(Floor f) {
		// Wait if there is no floor data available
		ArrayList<Floor> temp = new ArrayList<>();
		ArrayList<FloorData> newFd = new ArrayList<>();

		while(!this.canGetData && this.moreData) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		int fn;

		// If floor is null, get all requests that are the same as the first request, adding both floor and car number
		if(!this.floorData.isEmpty()) {
			if(f == null) {
				fn = (this.floorData.get(0)).getFloor();	

				for(FloorData fd: this.floorData) {
					if(fd.getFloor() == fn) {
						temp.add(new Floor(fd.getFloor()));
						temp.add(new Floor(fd.getCarButton()));
					} else {
						newFd.add(fd);
					}
				}
			} else { // else, get all requests that are same as the given floor
				fn = f.getNumber();
				for(FloorData fd: this.floorData) {
					if(fd.getFloor() == fn) {
						temp.add(new Floor(fd.getCarButton()));
					} else {
						newFd.add(fd);
					}
				}
			}		
		}

		this.floorData = newFd;
		this.canSendData = true;
		this.canGetData = !this.floorData.isEmpty();

		notifyAll();

		System.out.println("== Schedular: Sending floor requests to Elevator subsystem " + temp);
		return temp;
	}
	*/
	
	
	public void bytesToFloorData() {
		
		
		try {
			if(!receivePacket.isEmpty()) {
				for(DatagramPacket dP : receivePacket) {
					ByteArrayInputStream bais = new ByteArrayInputStream(dP.getData());
					ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new BufferedInputStream(bais)));
					
					Object o = ois.readObject();
					ois.close();
					
					if(o instanceof FloorData) {
						FloorData fd = (FloorData) o;
						System.out.println("Scheduler received floordata containing :" + fd.toString() + "\n");
					}
				}
			}
		}
		
		catch(IOException e) {
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	

	@Override
	public void run() {
		while(true) {

		}
	}

	public static void main(String[] args) {
		//I added an elevator size to the scheduler

		Scheduler scheduler = new Scheduler();
		Thread floorSubsystem = new FloorSubsystem(scheduler);
		ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(scheduler);
		
		scheduler.addElevator(elevatorSubsystem.getElevator());

		scheduler.start();
		elevatorSubsystem.start();
		floorSubsystem.start();

	}
}
