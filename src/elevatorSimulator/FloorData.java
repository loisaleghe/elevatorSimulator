package elevatorSimulator;

import java.sql.Time;

public class FloorData {
	private Time Time;
	private int Floor;
	private String FloorButton;
	private int CarButton;
	
	public FloorData(Time Time, int Floor, String FloorButton, int CarButton ) {
		this.Time = Time;
		this.Floor = Floor;
		this.FloorButton = FloorButton;
		this.CarButton = CarButton;
	}
	
	//copy constructor 
	FloorData(FloorData f){
		Time = f.Time;
		Floor = f.Floor;
		FloorButton = f.FloorButton;
		CarButton = f.CarButton;
	}

}
