package elevatorSimulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ElevatorData implements Serializable {
	/**
	 * The purpose of this class is to contain the information of the elevator that will be passed to the Scheduler
	 */
	
	private Direction direction;  
	private Floor currentFloor; 
	private int number;
	
	/**
	 * 
	 * @param elevatorDirection represents the direction the elevator is moving towards
	 * @param floorNumber represents the currentFloor is the exact floor the elevator is in
	 */ 
	public ElevatorData(Direction elevatorDirection, Floor floorNumber, int number) {
		this.direction = elevatorDirection;
		this.currentFloor = floorNumber;
		this.number = number;
	}
	
	public ElevatorData(Elevator elevator) {
		this.direction = elevator.getCurrentDirection();
		this.currentFloor = elevator.getCurrentFloor();
		this.number = elevator.getNumber();
	}
	
	public void setElevatorDirection(Direction newElevatorDirection) {
		this.direction = newElevatorDirection;		
	}

	public Direction getElevatorDirection() {
		return this.direction;
	}
	
	public void setFloor(Floor floor) {
		this.currentFloor = floor;
	}

	public Floor getFloor() {
		return this.currentFloor;
	}
	
	/**
	 * Sets the number of this elevator data
	 * @param number an int, representing the number to set for this elevator data's number
	 */
	public void setNumber(int number) {
		this.number = number;
	}
	
	/**
	 * Fetch the number of this elevator data
	 * @return an int, representing the number of this elevator data
	 */
	public int getNumber() {
		return this.number;
	}
	
	public static byte[] seriliaze(ElevatorData obj) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream (baos);
		oos.writeObject(obj);
		return baos.toByteArray();
	}
	
	public static ElevatorData deserialize(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (ElevatorData) ois.readObject();
	}

	
}
