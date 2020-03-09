package elevatorSimulator;

import java.io.*;
import java.net.*;

public class FloorSubsystem extends Thread {

	//private Scheduler scheduler; // This represents the scheduler that this floor subsystem will use to fetch ang send data
	private boolean stopSystem;
	private DatagramSocket sendSocket;

	/**
	 * Generates a new floor subsystem that communicates using the specified scheduler
	 * @param scheduler, a scheduler, represents the scheduler through which this floor subsystem communicates
	 */
	public FloorSubsystem() {
		super("Floor Subsystem");
		this.stopSystem = false;
		try {
			//Construct a datagram socket to send UDP datagram packets
			sendSocket = new DatagramSocket ();
		}catch(SocketException e) {
			System.err.println("== FLoor SubSystem: Could not create sockets");
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while(!this.stopSystem) {
			try {
				//	Read floor data values from file
				BufferedReader br = new BufferedReader(new FileReader("floorRequests.txt")); 
				FloorData fd;	

				String line; 
				while ((line = br.readLine()) != null) {
					//	Read line and convert to floor data
					fd = new FloorData(FloorData.parseString(line));

					byte [] floorRequestData = SchedularFloorData.seriliaze(new SchedularFloorData(CommunicationMessage.MORE_FLOOR_REQUESTS, fd));
					DatagramPacket floorRequestPacket = new DatagramPacket(floorRequestData, floorRequestData.length, InetAddress.getByName("172.17.163.254"), 10);

					//	Send data to scheduler
					System.out.println("== Floor Subsystem: Elevator requested on floor " + fd.getFloor());
					this.sendSocket.send(floorRequestPacket);

					Thread.sleep(2000);
				} 

				br.close();
				this.stopSystem = true;

				// Notify the scheduler that there's no more floor requests
				byte [] endData = SchedularFloorData.seriliaze(new SchedularFloorData(CommunicationMessage.NO_MORE_FLOOR_REQUESTS));
				DatagramPacket endPacket = new DatagramPacket(endData, endData.length, InetAddress.getByName("172.17.163.254"), 10);

				//	Send data to scheduler
				System.out.println("== Floor Subsystem: Notifying schedular of no more floor requests");
				this.sendSocket.send(endPacket);
				System.out.println("== Floor subsystem: Finished!");

			}catch(IOException | InterruptedException e) {
				System.err.println("== FLoor SubSystem: An error occured while sending data to Schedular");
				e.printStackTrace();
			}
		}

		//	Close sockets
		this.sendSocket.close();
	}

	public static void main(String[] args) {
		FloorSubsystem fs = new FloorSubsystem();
		fs.start();
	}
}


//		while(true) {
//			Random rand = new Random();
//			Time time = new Time(System.currentTimeMillis());		//Get current time
//			int floorNumber = rand.nextInt((9) + 1) + 1;			//Generate a random number between 1 & 10 for the current floor
//			
//			int pick = new Random().nextInt(Direction.values().length);
//			Direction floorButton = Direction.values()[pick];			//Generate a random direction from the "Direction" class(i.e up or down)
//			
//			int carButton = rand.nextInt((9) + 1) + 1;				//Generate a random number between 1 & 10 for the destination floor
//			
//			
//			FloorData data = new FloorData(time, floorNumber, floorButton, carButton);
//			
//			scheduler.sendFloorData(data);
//			try {
//				Thread.sleep((long)5000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
