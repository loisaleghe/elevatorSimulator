package elevatorSimulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ElevatorData implements Serializable{
	/**
	 * The purpose of this class is to contain the information of the elevator that will be passed to the Scheduler
	 */
	
	private Direction direction;  
	private int currentFloor; 
	
	/**
	 * 
	 * @param elevatorDirection represents the direction the elevator is moving towards
	 * @param floorNumber represents the currentFloor is the exact floor the elevator is in
	 */ 
	public ElevatorData(Direction elevatorDirection, int floorNumber) {
		this.direction = elevatorDirection;
		this.currentFloor = floorNumber;
	}
	
	public ElevatorData(Elevator elevator) {
		this.direction = elevator.getCurrentDirection();
		this.currentFloor = elevator.getCurrentFloor().getNumber();
	}
	
	public void setElevatorDirection(Direction newElevatorDirection) {
		this.direction = newElevatorDirection;
		
	}

	public Direction getElevatorDirection() {
		return this.direction;
	}
	
	public void setFloor(int floorData) {
		this.currentFloor = floorData;
	}

	public int getFloor() {
		return this.currentFloor;
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
		return (ElevatorData)ois.readObject();
	}

	
}
