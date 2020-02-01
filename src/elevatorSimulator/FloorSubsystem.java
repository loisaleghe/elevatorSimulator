package elevatorSimulator;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FloorSubsystem implements Runnable {
	
	private Floor floor;
	private Elevator elevator;
	//private boolean up;
	//private boolean down;
	
	public void FloorSubsystem(Floor floor, Elevator elevator) {
		this.floor = floor;
		this.elevator = elevator;
	}
	
	
	public void run() {
		
	}
}