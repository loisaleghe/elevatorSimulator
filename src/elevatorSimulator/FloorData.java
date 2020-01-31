package elevatorSimulator;

import java.sql.Time;

public class FloorData {
	private Time Time;
	private int Floor;
	private Direction FloorButton;
	private int CarButton;
	
	public FloorData(Time Time, int Floor, Direction FloorButton, int CarButton ) {
		this.Time = Time;
		this.Floor = Floor;
		this.FloorButton = FloorButton;
		this.CarButton = CarButton;
	}
	
	//copy constructor 
	public FloorData(FloorData f){
		Time = f.Time;
		Floor = f.Floor;
		FloorButton = f.FloorButton;
		CarButton = f.CarButton;
	}
	
	public void setCarButton(int value) {
		this.CarButton = value;
	}
	
	public int getCarButtn() {
		return this.CarButton;
	}
	
	public void setFloorButton(String floorButtonValue) {
		this.FloorButton = floorButtonValue;
	}
	
	public String getFloorButton() {
		return this.FloorButton;
	}
	
	public void setFloor(int floorValue) {
		this.Floor = floorValue;
	}
	
	public int getFloor() {
		return this.Floor;
	}
	
	public void setTime(Time timeValue) {
		this.Time = timeValue;
	}
	public Time getTime() {
		return this.Time;
	}

}
