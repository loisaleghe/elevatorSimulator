package elevatorSimulator;

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;

public class FloorSubsystem implements Runnable {

	private Scheduler scheduler;

	public FloorSubsystem(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	public void run() {
		try {
			//		Read floor data values from file
			BufferedReader br = new BufferedReader(new FileReader("floorRequests.txt")); 
			FloorData fd;

			String line; 
			while ((line = br.readLine()) != null) {
				//			  Read line and convert to floor data
				fd = new FloorData(FloorData.parseString(line));

				//		  Send and get data from schedular
				this.scheduler.sendData(fd);
				this.scheduler.getData();
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} 
			
			br.close();
		}catch(IOException e) {
			System.err.println(e.getMessage());
		}
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
