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

/**
 * @author djani
 * This class represents the data exchanged between the floor subsystem and the schedular
 */
public class SchedularFloorData implements Serializable {
	
	private FloorData floorData;
	private CommunicationMessage message;

	public SchedularFloorData(CommunicationMessage message) {
		this.message = message;
	}
	
	public SchedularFloorData(CommunicationMessage message, FloorData floorData) {
		this(message);
		this.floorData = floorData;
	}
	
	public FloorData getFloorData(){
		return this.floorData;
	}
	
	public CommunicationMessage getMessage() {
		return this.message;
	}
	
	public static byte[] seriliaze(SchedularFloorData sfd) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream (baos);
		oos.writeObject(sfd);
		return baos.toByteArray();
	}
	
	public static SchedularFloorData deserialize(byte[] data) throws IOException, ClassNotFoundException{
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ObjectInputStream ois = new ObjectInputStream(bais);
		return (SchedularFloorData) ois.readObject();
	}

}
