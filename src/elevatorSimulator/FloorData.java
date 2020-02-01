package elevatorSimulator;

import java.sql.Time;
import java.util.Scanner;

public class FloorData {
	/**
	 *  The purpose of this class is to contain the information which will be used by the scheduler
	 */
	public static final String DELIMITER = "#";
	private Time time;
	private int floor;
	private Direction floorButton;
	private int carButton;
/**
 * 
 * @param time, represents the time the floor data was created 
 * @param floor, represents the level or floor the elevator currently is 
 * @param floorButton, represents the direction of the floor you want to go to
 * @param carButton, represents the final destination or floor the user is going to 
 */
	public FloorData(Time time, int floor, Direction floorButton, int carButton ) {
		this.time = time;
		this.floor = floor;
		this.floorButton = floorButton;
		this.carButton = carButton;
	}

	//copy constructor
	/**
	 * 
	 * @param f, represents a FloorData object 
	 * this method is a copy constructor for anyone who wants to copy the floorDta object
	 */
	public FloorData(FloorData f){
		time = new Time(f.time.getTime());
		floor = f.floor;
		floorButton = f.floorButton;
		carButton = f.carButton;
	}

	public void setCarButton(int value) {
		this.carButton = value;
	}

	public int getCarButton() {
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

	/**
	 * Gives a string representation of this FloorData
	 * @return a String, representing this FloorData 
	 */
	public String toString() {
		return this.time + "#" + this.floor + "#" + this.floorButton + "#" + this.carButton;
	}
	
	/**
	 * 
	 * @param fd, represents a string from the file that will be converted 
	 * @return a FloorData Object, representing the conversion of the string 
	 */
	public static FloorData parseString(String fd) {
		Scanner scan = new Scanner(fd);
		scan.useDelimiter(FloorData.DELIMITER);
		scan.next(); // Skip time for now
		return new FloorData(new Time(System.currentTimeMillis()), Integer.parseInt(scan.next()), Direction.valueOf(scan.next()), Integer.parseInt(scan.next()));
	}

}
