package elevatorSimulator;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class FloorSubSystem implements Runnable {
	
	private Floor floor;
	private Elevator elevator;
	private boolean up;
	private boolean down;
	
	public void FloorSubsystem(Floor floor, Elevator elevator) {
		this.floor = floor;
		this.elevator = elevator;
	}
	
	public void getTimeStamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println(timestamp);
	}
	
	public void run() {
		
		int floorNumber = floor.getFloorNumber(floor);
		String direction = elevator.getDirection();
		int car = elevator.getCarNumber();
		
		while(elevator.ElevatorArrived()) {
			
		}
		
		while(true) {
			if(up == true) {
				
			}
		}
	}
}