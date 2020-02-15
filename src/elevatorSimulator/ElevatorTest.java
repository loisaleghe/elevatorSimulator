package elevatorSimulator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * @author Ediomoabasi Emah, Hilaire Djani
*/

class ElevatorTest {
	
	private Elevator elevator;

	@BeforeEach
	void setUp() throws Exception {
		elevator = new Elevator();
		/*
		Floor currFloor = new Floor(1);
		Direction currDirection = Direction.IDLE;
		*/
	}

	@AfterEach
	void tearDown() throws Exception {
	}
	
	/*
	 * Checks floors are added correctly in the right 
	 * queue
	 */
	@Test
	public void testAddFloorsToUpQueue() {
		elevator.setCurrentFloor(new Floor (2));
		elevator.addFloor(new Floor(4));
		elevator.addFloor(new Floor(5));
		elevator.addFloor(new Floor(3));
		assertTrue((elevator.getUpQueue().peek()).equals(new Floor(3)));	
	}
	
	/*
	 * Checks floors are added correctly in the right 
	 * queue
	 */
	@Test
	public void testAddFloorsToDownQueue() {
		elevator.setCurrentFloor(new Floor (7));
		elevator.addFloor(new Floor (2));
		elevator.addFloor(new Floor (6));
		elevator.addFloor(new Floor (4));
		elevator.addFloor(new Floor (3));
		assertTrue((elevator.getDownQueue().peek()).equals(new Floor(6)));
	}
	
	/*
	 * Prints the size of the two queues
	 */
	@Test
	public void testSizeOfElevatorFloors() {
		elevator.setCurrentFloor(new Floor (5));
		elevator.addFloor(new Floor (2));
		elevator.addFloor(new Floor (7));
		elevator.addFloor(new Floor (9));
		elevator.addFloor(new Floor (4));
		System.out.println(elevator.getUpQueue().size());
		System.out.println(elevator.getDownQueue().size());
	}
	
	@Test
	public void testMoveUp() {
		elevator.setCurrentFloor(new Floor (2));
		elevator.addFloor(new Floor (3));
		elevator.setCurrentDirection(Direction.UP);
		elevator.move();
		assertTrue((elevator.getUpQueue().isEmpty()));
	}
	
	/**
	 * Test that the elevator opens its door once it moves up to a floor it needs to visit
	 */
	@Test
	public void testMoveUpAndOpenDoor() {
		this.elevator.setCurrentFloor(new Floor(1));
		this.elevator.setCurrentDirection(Direction.UP);
		this.elevator.addFloor(new Floor(2));
		this.elevator.move();
		assertTrue(this.elevator.isDoorOpen());
	}
	
	/**
	 * Test that the elevator opens its door once it moves down to a floor it needs to visit
	 */
	@Test
	public void testMoveDownAndOpenDoor() {
		this.elevator.setCurrentFloor(new Floor(3));
		this.elevator.setCurrentDirection(Direction.DOWN);
		this.elevator.addFloor(new Floor(2));
		this.elevator.move();
		assertTrue(this.elevator.isDoorOpen());
	}
	

}
