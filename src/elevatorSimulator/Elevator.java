package elevatorSimulator;

/**
 * @author 
 */
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
		 this.currDirection = Direction.IDLE;
	}
	
	/**
	 * Modifies the elevator's direction based on the state of it's queues
	 */
	private void adjustElevatorDirection() {
		if(this.upQueue.isEmpty() && this.downQueue.isEmpty())
			this.currDirection = Direction.IDLE;
		else if(this.upQueue.isEmpty())
			this.currDirection = Direction.DOWN;
		else if(this.downQueue.isEmpty())
			this.currDirection = Direction.UP;
	}
	 
	/*
	 * add floors to either the elevator going up or the elevator
	 * going down
	 */
	public void addFloor(Floor f) {
		
		//gets the destination of the floor that is pressed 
		int floorNumber = f.getNumber();
		int currentFloorNumber = currFloor.getNumber();
		
		//adds the floors to the queue and sorts them for the elevator moving down
		if (floorNumber < currentFloorNumber) {
			downQueue.offer(new Floor(floorNumber));
			
			//is false to sort floors in descending order
			downQueue.sort(false);
		}
		
		else if (floorNumber > currentFloorNumber) {
			upQueue.offer(new Floor(floorNumber));
			
			//is true to sort floors in ascending order
			upQueue.sort(true);
		}
		
	}
	
	/*
	 * dictates the movement of the elevator
	 */
	public void move() {
		
		//scenarios for the elevator moving up
		if (currDirection.equals(Direction.UP)) {
			
			//if the queue is empty, nothing should happen
			if (upQueue.isEmpty()) {
				return;
			}
			
			//increments the floor if the current floor is less than the floor at index 0 
			if (currFloor.getNumber() < upQueue.peek().getNumber()) {
				currFloor.setNumber(currFloor.getNumber() + 1);
			}
			
			//removes the floor from the queue if the current floor is the same
			//as the floor at that specific index
			else if (currFloor.getNumber() == upQueue.peek().getNumber()) {
				upQueue.poll();
				
				//if the queue is empty, reverse the direction
				if (upQueue.isEmpty()) {
					currDirection = Direction.DOWN;
					
				}
				
				else{
					//increments the current floor is the queue isn't empty
					currFloor.setNumber(currFloor.getNumber() + 1);
				}
			}
			
		}
		
		//scenarios for the elevator moving down
		else if (currDirection.equals(Direction.DOWN)) {
			
			//if the queue is empty, nothing should happen
			if (downQueue.isEmpty()) {
				return;
			}
			
			//decrements the floor if the current floor is greater than the floor at index 0 
			if (currFloor.getNumber() > downQueue.peek().getNumber()) {
				currFloor.setNumber(currFloor.getNumber() - 1);
			}
			
			//removes the floor from the queue if the current floor is the same
			//as the floor at that specific index
			else if (currFloor.getNumber() == downQueue.peek().getNumber()) {
				downQueue.poll();
				
				//if the queue is empty, reverse the direction
				if(downQueue.isEmpty()) {
					currDirection = Direction.UP;
				}
				
				else {
					//decrements the current floor is the queue isn't empty
					currFloor.setNumber(currFloor.getNumber() - 1);
					
				}	
			}
		}
	}
	
	

}
