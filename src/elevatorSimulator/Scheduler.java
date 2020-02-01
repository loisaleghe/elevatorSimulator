package elevatorSimulator;
/*
 * The scheduler class represents the server in the system
 */

public class Scheduler implements Runnable {
    
	//creates an object of type floorData
	private FloorData floorData;
	
	private boolean canSendData;
	private boolean canGetData;
	
	//the scheduler stops when moreData is false
	private boolean moreData;
    
	/*
	 * The constructor to define the instance variables 
	 * of the scheduler
	 */
	public Scheduler() {
		this.floorData = null;
		this.canGetData = false;
		this.canSendData = true;
		this.moreData = true;
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
		this.floorData = new FloorData(fl);
		this.canGetData = true;
		this.canSendData = false;

		notifyAll();
	}
    
	/*
	 * This method is called by the Floor and Elevator Subsystems
	 * to continuously get data from the scheduler
	 */
	public synchronized FloorData getData() {
		//		Wait if there is no floor data available
		while(!this.canGetData) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

//		System.out.println( "== Adding floor " + this.floorData.getCarButton() + " to floors to visit ");
		FloorData fl = new FloorData(this.floorData);
		this.floorData = null;
		this.canSendData = true;
		this.canGetData = false;

		notifyAll();

		return fl;
	}

	@Override
	public void run() {

	}

	public static void main(String[] args) {


		Scheduler scheduler = new Scheduler();
		Thread schedulerSubsystem = new Thread(scheduler, "Scheduler");
		Thread floorSubsystem = new Thread(new FloorSubsystem(scheduler), "Floor Subsystem");
		Thread elevatorSubsystem = new Thread(new ElevatorSubsystem(scheduler), "Elevator Subsystem");

		schedulerSubsystem.start();
		floorSubsystem.start();
		elevatorSubsystem.start();


	}

}
