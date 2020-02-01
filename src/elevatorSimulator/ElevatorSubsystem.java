package elevatorSimulator;

public class ElevatorSubsystem implements Runnable {
	
	private Scheduler schedular;
	
	public ElevatorSubsystem(Scheduler schedular) {
		this.schedular = schedular;
	}

	@Override
	public void run() {
//		Continuously receive and send data from/to schedular
		while(true) {
//			Read data and send back to floor subsystem
			FloorData fd = this.schedular.getData();
			this.schedular.sendData(fd);
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
