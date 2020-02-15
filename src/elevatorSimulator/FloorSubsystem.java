package elevatorSimulator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FloorSubsystem implements Runnable {

	private Scheduler scheduler; // This represents the scheduler that this floor subsystem will use to fetch ang send data
	private boolean moreData;

	/**
	 * Generates a new floor subsystem that communicates using the specified scheduler
	 * @param scheduler, a scheduler, represents the scheduler through which this floor subsystem communicates
	 */
	public FloorSubsystem(Scheduler scheduler) {
		this.scheduler = scheduler;
		this.moreData = true;
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

					//	Send data to scheduler
					System.out.println("== Floor Subsystem: Elevator requested on floor " + fd.getFloor());
					this.scheduler.sendData(fd);

					Thread.sleep(1000);
				} 

				br.close();
				this.moreData = false;
				this.scheduler.setMoreData(false);

			}catch(IOException | InterruptedException e) {
				System.err.println("== Floor Subsystem: An error occured");
				System.err.println(e.getMessage());
			}
		}
		System.out.println("== Floor subsystem: Finished!");
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
