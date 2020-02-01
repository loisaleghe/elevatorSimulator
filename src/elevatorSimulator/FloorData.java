package elevatorSimulator;

import java.sql.Time;

public class FloorData {
	private Time time;
	private int floor;
	private Direction floorButton;
	private int carButton;
	
	public FloorData(Time time, int floor, Direction floorButton, int carButton ) {
		this.time = time;
		this.floor = floor;
		this.floorButton = floorButton;
		this.carButton = carButton;
	}
	
	//copy constructor 
	public FloorData(FloorData f){
		time = new Time(f.time.getTime());
		floor = f.floor;
		floorButton = f.floorButton;
		carButton = f.carButton;
	}
	
	public void setCarButton(int value) {
		this.carButton = value;
	}
	
	public int getCarButtn() {
		return this.carButton;
	}
	
	public void setFloorButton(Direction floorButtonValue) {
		this.floorButton = floorButtonValue;
	}
	
	public Direction getFloorButton() {
		return this.floorButton;
	}
	
	public void setFloor(int floorValue) {
		this.floor = floorValue;
	}
	
	public int getFloor() {
		return this.floor;
	}
	
	public void setTime(Time timeValue) {
		this.time = new Time(timeValue.getTime());
	}
	public Time getTime() {
		return this.time;
	}

}
