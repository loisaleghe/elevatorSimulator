package elevatorSimulator;

public class ElevatorSubsystem implements Runnable {

	private Scheduler scheduler;

	public ElevatorSubsystem(Scheduler schedular) {
		this.scheduler = schedular;
	}

	@Override
	public void run() {
		//		Continuously receive and send data from/to scheduler
		try {
			while(this.scheduler.getMoreData()) {
				//			Fetch data from scheduler subsystem
				System.out.println("Elevator Subsystem fetching data from schedular");
				FloorData fd = this.scheduler.getData();

				//				Sleep for some time, then send data back to scheduler
				Thread.sleep(2000);
				System.out.println("Elevator Subsystem sending data to schedular");
				this.scheduler.sendData(fd);

				Thread.sleep(2000);
			}
		} catch(InterruptedException e) {

		}
		System.out.println("Elevator finished");

	}

}
