package elevatorSimulator;

import java.util.ArrayList;

/*
 * The scheduler class represents the server in the system
 */

public class Scheduler implements Runnable {

	//creates an object of type floorData
	//private FloorData floorData;

	private ArrayList<FloorData> floorData ;		//Arraylist containing floor data from requests from the same floor. Iteration 2

	private boolean canSendData;
	private boolean canGetData;

	//the scheduler stops when moreData is false
	private boolean moreData;

	/*
	 * The constructor to define the instance variables 
	 * of the scheduler
	 */
	public Scheduler() {
		//this.floorData = null;
		this.canGetData = false;
		this.canSendData = true;
		this.moreData = true;
		this.floorData = new ArrayList<>();				//Iteration 2
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


	/*
	 * This method is called by the Floor and Elevator Subsystems
	 * to continuously send data to the scheduler
	 */
	public synchronized void sendData(FloorData fl) {
		//		Wait if flood data is being processed
		while(!this.canSendData) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("== Schedular: Receiving request on floor " + fl.getFloor());
		//this.floorData = new FloorData(fl);
		this.floorData.add(fl);				//Populating floor data arraylist with floordata then sending it to the elevator subsystem. Iteration 2
		this.canGetData = true;
		this.canSendData = false;

		notifyAll();
	}

	/*
	 * This method is called by the Floor and Elevator Subsystems
	 * to continuously get data from the scheduler
	 * 
	 * Modified to return an arraylist of floordata. Iteration 2
	 */
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

		return temp;
	}

	@Override
	public void run() {
		while(true) {

		}
	}

	public static void main(String[] args) {
		//I added an elevator size to the scheduler

		Scheduler scheduler = new Scheduler();
		Thread schedulerSubsystem = new Thread(scheduler, "Scheduler");
		Thread floorSubsystem = new Thread(new FloorSubsystem(scheduler), "Floor Subsystem");
		ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem(scheduler, 1);

		schedulerSubsystem.start();
		elevatorSubsystem.start();
		floorSubsystem.start();

	}
}
