package elevatorSimulator;

import java.util.Random;
import java.sql.Time;
import java.util.Date;

public class FloorSubsystem implements Runnable {
	
	private Floor floor;
	private Scheduler scheduler;
	
	public FloorSubsystem(Scheduler scheduler,  Floor floor) {
		this.floor = floor;
		this.scheduler = scheduler;
	}
	
	public void run() {
		while(true) {
			Random rand = new Random();
			Time time = new Time(System.currentTimeMillis());		//Get current time
			int floorNumber = rand.nextInt((9) + 1) + 1;			//Generate a random number between 1 & 10 for the current floor
			
			int pick = new Random().nextInt(Direction.values().length);
			Direction floorButton = Direction.values()[pick];			//Generate a random direction from the "Direction" class(i.e up or down)
			
			int carButton = rand.nextInt((9) + 1) + 1;				//Generate a random number between 1 & 10 for the destination floor
			
			
			FloorData data = new FloorData(time, floorNumber, floorButton, carButton);
		}
	}
}