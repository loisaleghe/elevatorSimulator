package elevatorSimulator;

public class Scheduler implements Runnable {
	
	private FloorData floorData;
	
	public Scheduler() {
		floorData = null;
	}
	
	public void sendFloorData(FloorData fL) {
		this.floorData = new FloorData(fL);
	}
	
	public FloorData getFloorData(FloorData fL) {
		return this.floorData;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

}
