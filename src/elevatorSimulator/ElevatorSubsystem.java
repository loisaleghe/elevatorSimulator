package elevatorSimulator;

public class ElevatorSubsystem implements Runnable {

	private Scheduler scheduler; //Scheduler object to interact with the elevator subsystem to get data

	/**
	 * Default constructor for elevator subsystem class
	 * @param scheduler, argument passed through default constructor for the elevator subsystem
	 */
	public ElevatorSubsystem(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	@Override
	public void run() {
		//Continuously receive and send data from/to scheduler
		try {
			while(this.scheduler.getMoreData()) {
				//Fetch data from scheduler subsystem
				FloorData fd = this.scheduler.getData();
				System.out.println("Receiving elevator data  << " + fd + " >> from the scheduler");

				//Sleep for some time, then send data back to scheduler
				Thread.sleep(2000);
				System.out.println("Elevator Subsystem sending data << " + fd + " >> to scheduler");
				this.scheduler.sendData(fd);

				Thread.sleep(2000);
			}
		} catch(InterruptedException e) {

		}
		System.out.println("Elevator finished");

	}

}
