package elevatorSimulator;

public class ElevatorSubsystem implements Runnable {
	
	private Scheduler scheduler;
	
	public ElevatorSubsystem(Scheduler schedular) {
		this.scheduler = schedular;
	}

	@Override
	public void run() {
//		Continuously receive and send data from/to schedular
		while(this.scheduler.getMoreData()) {
//			Read data and send back to floor subsystem
			FloorData fd = this.scheduler.getData();
//			this.scheduler.sendData(fd);
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Elevator finished");
		
	}

}
