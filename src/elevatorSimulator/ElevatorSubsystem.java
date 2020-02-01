package elevatorSimulator;

public class ElevatorSubsystem implements Runnable {
	
	private Scheduler schedular;
	private Elevator elevator;
	
	public ElevatorSubsystem(Scheduler schedular) {
		this.schedular = schedular;
		this.elevator = new Elevator();
	}

	@Override
	public void run() {
		while(true) {
			FloorData fd = this.schedular.getFloorData();
			this.elevator.addFloor(fd.getFloor());
		}
		
	}

}
