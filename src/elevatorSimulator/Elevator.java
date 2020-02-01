package elevatorSimulator;

public class Elevator {
	
	//creates a queue to represent the elevator moving up 
	private FloorQueue upQueue; 
	
	//creates a queue to represent the elevator moving down
	private FloorQueue downQueue;
	
	//creates a floor object to specify the floor the elevator is on
	private Floor currFloor;
	
	//specifies the direction the elevator is moving
	private Direction currDirection;
	
	/*
	 * The constructor to define the elevator
	 */
	public Elevator () {
		 this.upQueue = new FloorQueue();
		 this.downQueue = new FloorQueue();
		 this.currFloor = new Floor(1);
	}
	 
	
	public void addFloor (FloorData f) {
		
		//gets the destination of the floor that is pressed 
		int floor = f.getCarButtn();
		
		//adds the floors to the queue and sorts them for the elevator moving down
		if (floor < currFloor.getNumber()) {
			downQueue.offer(new Floor(floor));
			
			//is false to sort floors in descending order
			downQueue.sort(false);
		}
		
		else if (floor > currFloor.getNumber()) {
			upQueue.offer(new Floor(floor));
			
			//is true to sort floors in ascending order
			upQueue.sort(true);
		}
		
	}
	
	public void move () {
		
		//scenarios for the elevator moving up
		if (currDirection.equals(Direction.UP)) {
			
			//if the queue is empty, nothing should happen
			if (upQueue.isEmpty()) {
				return;
			}
			
			if (currFloor.getNumber() < upQueue.peek().getNumber()) {
				currFloor.setNumber(currFloor.getNumber() + 1);
			}
			
			
			
		}
	}
	
	

}
