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

//		System.out.println("== Requesting to go from floor " + fl.getFloor() + " to floor " + fl.getCarButton());
		//this.floorData = new FloorData(fl);
		floorData.add(fl);				//Populating floor data arraylist with floordata then sending it to the elevator subsystem. Iteration 2
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
	public synchronized ArrayList<FloorData> getData(Floor f) {
		//		Wait if there is no floor data available
		ArrayList<FloorData> temp = new ArrayList<>();
		while(!this.canGetData) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//To check for requests from the same floor, then populating the floorD arraylist with the floordata. Iteration 2
			int fn = f.getNumber();
			
			
			for(FloorData fd: this.floorData) {
				if(fd.getFloor() == fn) {
					temp.add(fd);
				}
			}
		}

//		System.out.println( "== Adding floor " + this.floorData.getCarButton() + " to floors to visit ");
		//FloorData fl = new FloorData(this.floorData);
		this.floorData = null;
		this.canSendData = true;
		this.canGetData = false;

		notifyAll();
		
		//return fl;
		return temp;
	}
	
	@Override
	public void run() {

	}

	public static void main(String[] args) {
		//I added an elevator size to the scheduler

		Scheduler scheduler = new Scheduler();
		Thread schedulerSubsystem = new Thread(scheduler, "Scheduler");
		Thread floorSubsystem = new Thread(new FloorSubsystem(scheduler), "Floor Subsystem");
		Thread elevatorSubsystem = new Thread(new ElevatorSubsystem(scheduler, 5), "Elevator Subsystem");

		schedulerSubsystem.start();
		elevatorSubsystem.start();
		floorSubsystem.start();

	}

}
