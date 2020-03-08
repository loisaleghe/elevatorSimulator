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
	
	private ArrayList<FloorData> floorRequests;
	private CommunicationMessage message;

	public SchedularElevatorData(CommunicationMessage message) {
		this.message = message;
	}
	
	public SchedularElevatorData(CommunicationMessage message, ArrayList<FloorData> floorRequests) {
		this(message);
		this.floorRequests = floorRequests;
	}
	
	public ArrayList<FloorData> getFloorRequests(){
		return this.floorRequests;
	}
	
	public CommunicationMessage getMessage() {
		return this.message;
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
