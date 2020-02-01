package elevatorSimulator;

import java.util.Arrays;
import java.util.List;

public class Scheduler implements Runnable {
	
	private int maxFloorDataCount;
	private List<FloorData> floorsToSend;
	
	public Scheduler(int maxFloorDataCount) {
		this.maxFloorDataCount = maxFloorDataCount;
		this.floorsToSend = Arrays.asList(new FloorData[this.maxFloorDataCount]);
	}
	
	public synchronized void sendFloorData(FloorData fl) {
//		Wait until floor data array is not full
		while(this.floorsToSend.size() >= this.maxFloorDataCount) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		this.floorsToSend.add(new FloorData(fl));
	}
	
	public synchronized FloorData getFloorData() {
		//		Wait if there are no floors to send
		while(this.floorsToSend.size() == 0) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		return this.floorsToSend.remove(this.floorsToSend.size() - 1);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
		Thread Scheduler = new Thread(new Scheduler());
		Thread FloorSubsystem = new Thread(new FloorSubsystem());
		Thread ElevatorSubsystem = new Thread(new ElevatorSubsystem())

		Scheduler.start();
		FloorSubsystem.start();
		ElevatorSubsystem.start();
	}

}
