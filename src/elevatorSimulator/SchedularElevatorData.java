/**
 * 
 */
package elevatorSimulator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author djani
 * This class represents the data sent by the scheduler to an elevator
 */
public class SchedularElevatorData implements Serializable {
	
	ArrayList<Floor> floorRequests;

	/**
	 * 
	 */
	public SchedularElevatorData(ArrayList<Floor> floorRequests) {
		this.floorRequests = floorRequests;
	}
	
	public ArrayList<Floor> getFloorRequests(){
		return this.floorRequests;
	}
	
	public static byte[] seriliaze(SchedularElevatorData sed) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream (baos);
		oos.writeObject(sed);
		return baos.toByteArray();
	}
	
	public static SchedularElevatorData deserialize(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (SchedularElevatorData) ois.readObject();
	}

}
