package elevatorSimulator;

import java.util.ArrayList;

public class ElevatorSubsystem implements Runnable {

	private Scheduler scheduler; //Scheduler object to interact with the elevator subsystem to get data

	private int elevatorSize; //elevatorSize contains the number of elevators

	private ArrayList <Elevator> elevators; //elevators would contain an arraylist of elevators

	/**
	 * Default constructor for elevator subsystem class
	 * @param scheduler, argument passed through default constructor for the elevator subsystem
	 */
	public ElevatorSubsystem(Scheduler scheduler, int elevatorSize) {
		this.scheduler = scheduler;
		this.elevatorSize = elevatorSize;
		//add elevators to the arraylist based on the size
		for(int i = 0; i < elevatorSize; i++) {
			elevators.add(new Elevator());
		}
	}

	@Override
	public void run() {
		//Continuously receive and send data from/to scheduler
		try {
			while(this.scheduler.getMoreData()) {
				//Fetch data from scheduler subsystem
				FloorData fd = this.scheduler.getData();
				System.out.println("== Elevator subsystem receiving data  << " + fd + " >> from the scheduler");

				//Sleep for some time, then send data back to scheduler
				Thread.sleep(1000);
				System.out.println("== Elevator Subsystem sending data << " + fd + " >> to scheduler");
				this.scheduler.sendData(fd);

				Thread.sleep(1000);
			}
		} catch(InterruptedException e) {

		}
		System.out.println("== Elevator Subsystem finished");

	}

}
