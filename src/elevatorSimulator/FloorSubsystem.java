package elevatorSimulator;

import java.io.*;
import java.net.*;

public class FloorSubsystem extends Thread {

	private Scheduler scheduler; // This represents the scheduler that this floor subsystem will use to fetch ang send data
	private boolean moreData;
	private DatagramPacket sPacket;
	private DatagramSocket sendSocket;

	/**
	 * Generates a new floor subsystem that communicates using the specified scheduler
	 * @param scheduler, a scheduler, represents the scheduler through which this floor subsystem communicates
	 */
	public FloorSubsystem() {
		super("Floor Subsystem");
		this.moreData = true;
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
		while(this.moreData) {
			try {
				//	Read floor data values from file
				BufferedReader br = new BufferedReader(new FileReader("floorRequests.txt")); 
				FloorData fd;

				String line; 
				while ((line = br.readLine()) != null) {
					//	Read line and convert to floor data
					fd = new FloorData(FloorData.parseString(line));
					
					byte [] floorRequestData = FloorData.seriliaze(fd);
					DatagramPacket floorRequestPacket = new DatagramPacket(floorRequestData, floorRequestData.length, InetAddress.getLocalHost(), 10);

					//	Send data to scheduler
					System.out.println("== Floor Subsystem: Elevator requested on floor " + fd.getFloor());
					this.sendSocket.send(floorRequestPacket);

					Thread.sleep(1000);
				} 

				br.close();
				this.moreData = false;

			}catch(IOException | InterruptedException e) {
				System.err.println("== FLoor SubSystem: An error occured while sending data to Schedular");
				e.printStackTrace();
			}
		}
		System.out.println("== Floor subsystem: Finished!");
	}
	
	/*
	 * @param x a FloorData type
	 * method that sends the converted FloorData to the Scheduler
	 */
	public void sendData(FloorData x) throws IOException {
		byte [] msg = FloorData.convertToByteArray(x);
		try {
			sPacket = new DatagramPacket (msg, msg.length, InetAddress.getLocalHost(), 10);
			//send the packet			
			sendSocket.send(sPacket);
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("FloorSubsystem: Packet sent to Scheduler.\n");
		System.out.println("Containing: " +  x.toString() + "\n.");
	}
	
	public static void main(String[] args) {
		FloorSubsystem s = new FloorSubsystem();
		s.start();
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
